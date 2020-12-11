package com.whaty.util;

import com.whaty.constant.CommonConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.framework.annotation.ExcludeField;
import com.whaty.framework.annotation.FieldOrder;
import com.whaty.framework.exception.UncheckException;
import com.whaty.function.Tuple;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.misc.BASE64Encoder;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 通用工具方法
 *
 * @author weipengsen
 */
public class CommonUtils {

    private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private final static String[] CHINESE_NUM = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    /**
     * 根据web地址返回绝对地址
     *
     * @param webPath
     * @return
     */
    public static String getRealPath(String webPath) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest().getSession().getServletContext().getRealPath(webPath);
    }

    /**
     * 在非请求过程中得到绝对地址
     *
     * @param webPath
     * @return
     */
    public static String getRealPathNoRequest(String webPath) {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath(webPath);
    }
    /**
     * 四舍五入把double转化int整型
     * @param number
     * @return
     */
    public static int parseIntForRound(double number) {
        return new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    /**
     * 将list拼接为sql的in条件。 list达到1000会分成多段
     *
     * @param list    list
     * @param tableId sql id
     * @return tableid in ('....','...','..')
     */
    public static String madeSqlIn(List<?> list, String tableId) {
        return madeSqlForSign(list, tableId, "in");
    }

    /**
     * 将ids拼接为sql的in条件。 list达到1000会分成多段
     *
     * @param ids
     * @param tableId
     * @return tableid in ('....','...','..')
     */
    public static String madeSqlIn(String ids, String tableId) {
        return madeSqlIn(Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN)), tableId);
    }

    /**
     * 将ids拼接为sql的in条件。 list达到1000会分成多段
     *
     * @param ids
     * @param tableId
     * @return tableid in ('....','...','..')
     */
    public static String madeSqlIn(Object[] ids, String tableId) {
        return madeSqlIn(Arrays.asList(ids), tableId);
    }

    /**
     * 将ids拼接为sql的in条件。 达到1000会分成多段
     *
     * @param ids
     * @param tableId
     * @return tableid in ('....','...','..')
     */
    public static String madeSqlIn(Collection<?> ids, String tableId) {
        return madeSqlIn(new ArrayList<>(ids), tableId);
    }

    /**
     * 将ids拼接为sql的in条件。 list达到1000会分成多段,tableId默认为“”
     *
     * @param ids
     * @return
     */
    public static String madeSqlIn(String ids) {
        return madeSqlIn(ids, "");
    }

    /**
     * 判断客户端ip，可以过滤反向代理
     *
     * @return
     */
    public static String getIpAddress() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        String fromSource = "X-Real-IP";
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            fromSource = "X-Forwarded-For";
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            fromSource = "Proxy-Client-IP";
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            fromSource = "WL-Proxy-Client-IP";
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            fromSource = "request.getRemoteAddr";
        }
        if (logger.isDebugEnabled()) {
            logger.debug("App Client IP: " + ip + ", fromSource: " + fromSource);
        }
        return ip;
    }

    /**
     * 获得当前线程绑定的request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return RequestContextHolder.getRequestAttributes() == null ? null
                : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 日期的格式化方法
     *
     * @param d    时间
     * @param type 例如yyyy-MM-dd, yyyy-MM-dd HH:mm:ss:ms
     * @return
     */
    @Deprecated
    public static String getStringDate(Date d, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(d);
    }

    /**
     * 获取域名
     *
     * @return
     */
    public static String getServerName() {
        if (getRequest() == null) {
            return null;
        }
        return getRequest().getServerName();
    }

    /**
     * 备份文件
     * @param srcFiles
     * @return
     */
    public static List<File> bakWebPathFiles(List<String> srcFiles) {
        return srcFiles.stream().map(CommonUtils::getRealPath).map(File::new)
                .filter(File::exists).map(srcFile -> {
            try {
                return CommonUtils.bakFile(srcFile);
            } catch (IOException e) {
                throw new UncheckException(e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 备份文件
     * @param srcFile
     * @return
     */
    public static File bakFile(File srcFile) throws IOException {
        File target = new File(srcFile.getAbsolutePath() + ".bak");
        FileUtils.copyFile(srcFile, target);
        return target;
    }

    /**
     * 还原文件
     * @param srcFile
     * @return
     * @throws IOException
     */
    public static File rollbackFile(File srcFile) throws IOException {
        File target = new File(srcFile.getAbsolutePath().substring(0, srcFile.getAbsolutePath().lastIndexOf(".bak")));
        FileUtils.copyFile(srcFile, target);
        return target;
    }

    /**
     * 还原文件
     * @param srcFiles
     * @return
     * @throws IOException
     */
    public static List<File> rollbackFiles(List<File> srcFiles) {
        return srcFiles.stream().peek(file -> {
            try {
                CommonUtils.rollbackFile(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }).collect(Collectors.toList());
    }

    /**
     * 对比日期，且对比详细时间
     * <p>
     * 如果date1在date2前面则返回true，否则返回false
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean compareDateDetail(Date date1, Date date2) {
        return date1.before(date2);
    }

    /**
     * 将集合中的元素拼接成一个字符串
     *
     * @param collection
     * @param separator
     * @param wrapper
     * @return
     */
    public static String join(Collection<String> collection, String separator, String wrapper) {
        return join(collection.toArray(new String[collection.size()]), separator, wrapper);
    }

    /**
     * 将数组中的元素拼接成一个字符串
     *
     * @param strArr
     * @param separator 分隔符,默认为","
     * @param wrapper   元素包括符,为空则不包括
     * @return
     */
    public static String join(String[] strArr, String separator, String wrapper) {
        String defaultSeparator = ",";
        StringBuilder sBuilder = new StringBuilder();
        for (String str : strArr) {
            if (wrapper != null) {
                sBuilder.append(wrapper);
            }
            sBuilder.append(str);
            if (wrapper != null) {
                sBuilder.append(wrapper);
            }
            if (separator != null) {
                sBuilder.append(separator);
            } else {
                sBuilder.append(defaultSeparator);
            }
        }
        return sBuilder.length() > 0 ? sBuilder.deleteCharAt(sBuilder.length() - 1).toString()
                : sBuilder.toString();
    }

    /**
     * 首字母大写
     *
     * @param originString
     * @return
     */
    public static String upperFirstLetter(String originString) {
        char[] letterChar = originString.toCharArray();
        char upperFirst = letterChar[0] -= 32;
        letterChar[0] = upperFirst;
        return String.valueOf(letterChar);
    }

    /**
     * 删除文件夹下的所有文件
     *
     * @param path
     */
    public static void deleteFiles(String path) {
        try {
            Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("delete file: " + file.toString());
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    System.out.println("delete dir: " + dir.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 首字母小写
     * @param originString
     * @return
     */
    private static String lowerFirstLetter(String originString) {
        char[] letterChar = originString.toCharArray();
        char upperFirst = letterChar[0] += 32;
        letterChar[0] = upperFirst;
        return String.valueOf(letterChar);
    }

    /**
     * 将字符串进行md5加密
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static String md5(String s) {
        return md5(s, "UTF-8");
    }

    /**
     * 将字符串进行md5加密
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static String md5(String s, String charset) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes(charset);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置响应类型为download
     *
     * @param response
     */
    public static void setContentToDownload(HttpServletResponse response, String fileName)
            throws UnsupportedEncodingException {
        response.reset();
        response.setContentType(fileName.contains(".pdf") ? "application/pdf" : "application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" +
                new String(fileName.getBytes("utf-8"), "iso-8859-1"));
    }

    /**
     * 判断文件的路径是否存在，不存在则创建目录
     *
     * @param realPath
     * @return
     */
    public static String mkDir(String realPath) {
        //创建文件夹
        File file = new File(realPath.substring(0,
                realPath.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }
        return realPath;
    }

    /**
     * 将multipartFile转换成file
     *
     * @param originFile
     * @return
     */
    public static File convertMultipartFileToFile(MultipartFile originFile) throws Exception {
        String fileName = UUID.randomUUID().toString() + ".tmp";
        return convertMultipartFileToFile(originFile,
                getRealPath(CommonConstant.TEMP_FILE_PATH + "/" + fileName));
    }

    /**
     * 将multipartFile转换成file
     *
     * @param originFile
     * @param targetPath
     * @return
     */
    public static File convertMultipartFileToFile(MultipartFile originFile, String targetPath) throws Exception {
        DiskFileItem fileItem = (DiskFileItem) ((CommonsMultipartFile) originFile).getFileItem();
        File temp = new File(mkDir(targetPath));
        if (fileItem.isInMemory()) {
            fileItem.write(temp);
        } else {
            FileUtils.copyFile(fileItem.getStoreLocation(), temp);
        }
        return temp;
    }

    /**
     * 给数字左边补零
     *
     * @param num
     * @param length
     * @return
     * @author weipengsen
     */
    public static String leftAddZero(int num, int length) {
        return leftAddZero(String.valueOf(num), length);
    }

    /**
     * 给数字左边补零
     *
     * @param num
     * @param length
     * @return
     * @author weipengsen
     */
    public static String leftAddZero(String num, int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length - num.length(); i++) {
            result.append("0");
        }
        result.append(num);
        return result.toString();
    }

    /**
     * 将上传的时间转换成规定格式
     * <p>
     * 可以将yyyy-MM-dd 和 yyyy/MM/dd 和 yyyy.MM.dd 格式转为 yyyy-MM-dd
     *
     * @param oldDate
     * @return
     */
    public static String timeFormatConversion(String oldDate) {
        Matcher m = ValidateUtils.DATE_REG_PATTERN.matcher(oldDate);
        String newDate = oldDate;
        if (m.find()) {
            String[] dates = null;
            if (oldDate.contains("-")) {
                dates = oldDate.split("-");
            } else if (oldDate.contains("/")) {
                dates = oldDate.split("/");
            } else if (oldDate.contains(".")) {
                dates = oldDate.split("\\.");
            }
            if (dates != null) {
                if (dates.length > 2) {
                    newDate = dates[0] + "-" + dates[1] + "-" + dates[2];
                } else {
                    newDate = oldDate;
                }
            } else {
                newDate = oldDate;
            }
        }
        return newDate;
    }

    /**
     * 时间转换成yyyy-MM-dd字符串
     *
     * @param d
     * @return
     */
    public static String changeDateToString(Date d) {
        if (d == null) {
            return null;
        }
        return convertDateToLocalDateTime(d).format(CommonConstant.DEFAULT_DATE_FORMAT);
    }

    /**
     * 时间转换成指定格式字符串
     *
     * @param d
     * @return
     */
    public static String changeDateToString(Date d, String dateFormat) {
        if (d == null) {
            return null;
        }
        return convertDateToLocalDateTime(d).format(DateTimeFormatter.ofPattern(dateFormat));
    }

    /**
     * 时间转换成指定格式
     *
     * @param d
     * @return
     */
    public static String changeDateToString(Date d, DateTimeFormatter dateFormat) {
        if (d == null) {
            return null;
        }
        return convertDateToLocalDateTime(d).format(dateFormat);
    }

    /**
     * 将date转换成LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 根据日期得到大写中文数组
     *
     * @param date
     * @return
     */
    public static String[] dateToChinese(Date date) {
        String str = changeDateToString(date);
        String o[] = str.split("-");
        String u[] = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String temp[] = {"", "", ""};
        for (int i = 0; i < o.length; i++) {
            for (int j = 0; j < o[i].length(); j++) {
                String s = o[i].substring(j, j + 1);
                if (i == 1 && j == 0) {
                    if (u[Integer.parseInt(s)].equals("一")) {
                        temp[i] = temp[i] + "十";
                    }
                    continue;
                }
                if (i == 2 && j == 0) {
                    if (u[Integer.parseInt(s)].equals("一")) {
                        temp[i] = temp[i] + "十";
                    }
                    if (u[Integer.parseInt(s)].equals("二")) {
                        temp[i] = temp[i] + "二十";
                    }
                    if (u[Integer.parseInt(s)].equals("三")) {
                        temp[i] = temp[i] + "三十";
                    }
                    continue;
                }
                temp[i] = temp[i] + u[Integer.parseInt(s)];
                if (temp[i].equals("十〇") || temp[i].equals("二十〇") || temp[i].equals("三十〇")) {
                    temp[i] = temp[i].substring(0, temp[i].indexOf("〇"));
                }
                if (temp[i].length() == 1) {
                    temp[i] = "  " + temp[i];
                }
            }
        }
        return temp;
    }

    /**
     * 生成指定位随机数
     *
     * @param n
     * @return
     */
    public static String getRandom(int n) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < n; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

    /**
     * 获得端口号
     *
     * @return
     */
    public static int getServerPort() throws MalformedObjectNameException {
        if (getRequest() == null) {
            return getServerPortWhenLaunch();
        }
        return getRequest().getServerPort();
    }

    /**
     * 在启动时获取端口号
     * @return
     * @throws MalformedObjectNameException
     */
    public static int getServerPortWhenLaunch() throws MalformedObjectNameException {
        Set<ObjectName> objSet = ManagementFactory.getPlatformMBeanServer()
                .queryNames(new ObjectName("*:type=Connector,*"),
                        Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        if (CollectionUtils.isEmpty(objSet) || objSet.size() > 1) {
            throw new IllegalStateException("expect one objectName but not find or find more");
        }
        return Integer.parseInt(objSet.toArray(new ObjectName[1])[0].getKeyProperty("port"));
    }

    /**
     * 提供精确的浮点数加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 比较日期的年月日 忽略时间
     * 当 date2年月日 > date1年月日 返回true
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean compareDate(Date date1, Date date2) {
        date1 = DateUtils.setHours(date1, 0);
        date1 = DateUtils.setMinutes(date1, 0);
        date1 = DateUtils.setSeconds(date1, 0);
        date2 = DateUtils.setHours(date2, 0);
        date2 = DateUtils.setMinutes(date2, 0);
        date2 = DateUtils.setSeconds(date2, 0);

        return date1.before(date2);
    }

    /**
     * 将参数设置到bean中
     *
     * @param bean
     * @param propertyName
     * @param propertyValue
     */
    public static void setPropertyToBean(Object bean, String propertyName, Object propertyValue) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        if (propertyName.contains(".")) {
            int propertySignIndex = propertyName.indexOf(".");
            String propertyTargetName = propertyName.substring(0, propertySignIndex);
            Object property = setPropertyIfAbsent(bean, propertyTargetName);
            setPropertyToBean(property, propertyName.substring(propertySignIndex + 1), propertyValue);
        } else {
            Class clazz = bean.getClass();
            Method setMethod = clazz.getMethod("set" + CommonUtils.upperFirstLetter(propertyName),
                    propertyValue.getClass());
            setMethod.invoke(bean, propertyValue);
        }
    }

    /**
     * 返回成员对象,如果成员不存在则设置成员
     */
    public static Object setPropertyIfAbsent(Object bean, String propertyName) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        Class clazz = bean.getClass();
        //找到对应的get方法
        Method getMethod;
        try {
            getMethod = clazz.getMethod("get" + CommonUtils.upperFirstLetter(propertyName));
        } catch (NoSuchMethodException e) {
            getMethod = clazz.getMethod("is" + CommonUtils.upperFirstLetter(propertyName));
        }
        //判断成员如果不为空则设置新成员
        Object property = getMethod.invoke(bean);
        if (property == null) {
            Class resultClazz = getMethod.getReturnType();
            Constructor constructor = resultClazz.getConstructor();
            property = constructor.newInstance();
            Method setMethod = clazz.getMethod("set" + CommonUtils.upperFirstLetter(propertyName), resultClazz);
            setMethod.invoke(bean, property);
        }
        return property;
    }

    /**
     * 拿到文件的格式
     *
     * @return
     */
    public static String getFileType(String fileName) {
        if (fileName.lastIndexOf(".") == -1) {
            throw new IllegalArgumentException("不合法文件名");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 拿到obj对象的arg名变量的值
     *
     * @param obj
     * @return
     */
    public static <T extends AbstractBean> T get(Object obj) throws Exception {
        if (obj instanceof HibernateProxy) {
            HibernateProxy proxy = (HibernateProxy) obj;
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            obj = li.getImplementation();
        }
        return (T) obj;
    }

    /**
     * 获取服务器ip
     *
     * @return
     */
    public static String getServerIp() throws SocketException, UnknownHostException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
                    .nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    if (ip.getHostAddress().equals("127.0.0.1")) {
                        continue;
                    }
                    return ip.getHostAddress();
                }
            }
        }
        return null;
    }

    /**
     * 根据反射获取值
     *
     * @param target
     * @param name
     * @return
     */
    public static Object getValueByReflect(Object target, String name) throws IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        if (target == null) {
            throw new NullPointerException();
        }
        String tmpName = name.contains(".") ? name.substring(0, name.indexOf(".")) : name;
        Object value;
        try {
            value = getFieldValueByReflect(target, tmpName);
        } catch (NoSuchFieldException e) {
            value = getMethodValueByReflect(target, tmpName);
        }
        return name.contains(".") ? getValueByReflect(value, name.substring(name.indexOf(".") + 1)) : value;
    }

    /**
     * 根据反射获取字段值
     *
     * @param target
     * @param name
     * @return
     */
    public static Object getFieldValueByReflect(Object target, String name) throws NoSuchFieldException,
            IllegalAccessException {
        Class clazz = target.getClass();
        Field f;
        try {
            f = clazz.getField(name);
        } catch (NoSuchFieldException e) {
            while (true) {
                clazz = clazz.getSuperclass();
                if (clazz == null) {
                    throw new NoSuchFieldException();
                }
                try {
                    f = clazz.getField(name);
                } catch (NoSuchFieldException e1) {
                    continue;
                }
                break;
            }
        }
        f.setAccessible(true);
        return f.get(target);
    }

    /**
     * 根据反射获取方法值
     *
     * @param target
     * @param name
     * @return
     */
    public static Object getMethodValueByReflect(Object target, String name) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        String methodName = "get" + upperFirstLetter(name);
        Class clazz = target.getClass();
        Method m;
        try {
            m = clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            while (true) {
                clazz = clazz.getSuperclass();
                if (clazz == null) {
                    throw new NoSuchMethodException();
                }
                try {
                    m = clazz.getMethod(methodName);
                } catch (NoSuchMethodException e1) {
                    continue;
                }
                break;
            }
        }
        m.setAccessible(true);
        return m.invoke(target);
    }

    /**
     * 获得没有-符号的uuid
     *
     * @return
     */
    public static String generateUUIDNoSign() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 打包成zip文件
     *
     * @param out
     */
    public static void packageToZip(Map<String, File> fileMap, ZipOutputStream out) {
        fileMap.entrySet().stream().filter(e-> e.getValue().exists()).forEach(e-> {
            try(InputStream in = new FileInputStream(e.getValue())) {
                out.putNextEntry(new ZipEntry(e.getKey()));
                int len;
                byte[] buffer = new byte[1024];
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.closeEntry();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * 打包成zip文件
     *
     * @param files
     * @param out
     */
    public static void packageToZip(List<File> files, ZipOutputStream out) throws IOException {
        for (File file : files) {
            if (!file.exists()) {
                continue;
            }
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                out.putNextEntry(new ZipEntry(file.getName()));
                int len;
                byte[] buffer = new byte[1024];
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.closeEntry();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
    }

    /**
     * 将字符串转换为日期
     *
     * @param suspendSchoolingEndDate
     * @return
     */
    public static Date changeStringToDate(String suspendSchoolingEndDate) {
        return changeStringToDate(suspendSchoolingEndDate, CommonConstant.DEFAULT_DATE_STR);
    }

    /**
     * 将字符串转换为日期
     *
     * @param suspendSchoolingEndDate
     * @return
     */
    public static Date changeStringToDateTime(String suspendSchoolingEndDate) {
        return changeStringToDateTime(suspendSchoolingEndDate, CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR);
    }

    /**
     * 将UTC格式时间字符串转换为日期
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date stringToUTCDate(String date) throws ParseException {
        final SimpleDateFormat utcSdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        utcSdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d = utcSdf.parse(date);
        return d;
    }

    /**
     * 将字符串转换为日期
     *
     * @param suspendSchoolingEndDate
     * @param format
     * @return
     */
    public static Date changeStringToDate(String suspendSchoolingEndDate, String format) {
        LocalDate localDate = LocalDate.from(DateTimeFormatter.ofPattern(format)
                .parse(suspendSchoolingEndDate));
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将字符串转换为日期
     *
     * @param suspendSchoolingEndDate
     * @param format
     * @return
     */
    public static Date changeStringToDateTime(String suspendSchoolingEndDate, String format) {
        LocalDateTime localDate = LocalDateTime.from(DateTimeFormatter.ofPattern(format)
                .parse(suspendSchoolingEndDate));
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 复制文件
     *
     * @param input
     * @param path
     * @throws IOException
     */
    public static void copyFile(InputStream input, String path) throws IOException {
        CommonUtils.mkDir(path);
        try (OutputStream out = new FileOutputStream(path)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.flush();
        }
    }

    /**
     * 日期数字转换为汉字数字
     * 如：2016转为二〇一六 12转化为十二 07转化为七
     *
     * @param number
     * @return
     */
    public static String dateNumberToChinese(int number) {
        String str = String.valueOf(number);
        String[] chineseNumber = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        StringBuilder target = new StringBuilder();
        while (str.startsWith("0")) {
            str = str.substring(1);
        }
        if (str.length() > 2) {
            for (int i = 0; i < str.length(); i++) {
                String ch = str.substring(i, i + 1);
                target.append(chineseNumber[Integer.parseInt(ch)]);
            }
        } else if (str.length() == 1) {
            target.append(chineseNumber[Integer.parseInt(str)]);
        } else {
            if (!"1".equals(str.substring(0, 1))) {
                target.append(chineseNumber[Integer.parseInt(str.substring(0, 1))]);
            }
            target.append("十");
            if (!"0".equals(str.substring(1))) {
                target.append(chineseNumber[Integer.parseInt(str.substring(1))]);
            }
        }
        return target.toString();
    }

    /**
     * 将map转换成xml
     *
     * @param params
     * @return
     */
    public static String convertMapToXML(Map<String, String> params) {
        StringBuilder xmlStr = new StringBuilder();
        xmlStr.append("<xml>");
        params.forEach((k, v) ->
                xmlStr.append("<").append(k).append("><![CDATA[").append(v).append("]]></").append(k).append(">")
        );
        xmlStr.append("</xml>");
        return xmlStr.toString();
    }

    /**
     * 从request的reader中获取数据
     *
     * @param request
     * @return
     */
    public static String getRequestReaderString(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String inputLine;
        StringBuilder readerString = new StringBuilder();
        while ((inputLine = request.getReader().readLine()) != null) {
            readerString.append(inputLine);
        }
        request.getReader().close();
        return readerString.toString();
    }

    /**
     * 将xml字符串转换为map
     *
     * @param xmlString
     * @return
     */
    public static Map<String, Object> convertXmlStringToMap(String xmlString) throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlString.trim());
        List<Element> elements = doc.getRootElement().elements();
        Map<String, Object> target = new HashMap<>(16);
        elements.forEach(e -> target.put(e.getName(), e.getTextTrim()));
        return target;
    }

    /**
     * 将object转换成map，暂时只支持浅层转换，深层再扩展
     *
     * @param object
     * @return
     */
    public static Map<String, Object> convertObjectToMap(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(e -> !Modifier.isStatic(e.getModifiers()))
                .map(e -> new Tuple<>(e.getName(), getValueByField(e, object)))
                .filter(e -> Objects.nonNull(e.t1))
                .collect(Collectors.toMap(e -> e.t0, e -> e.t1));
    }

    /**
     * 通过field获取值
     * @param field
     * @param object
     * @return
     */
    private static Object getValueByField(Field field, Object object) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e1) {
            return null;
        } finally {
            field.setAccessible(accessible);
        }
    }

    /**
     * 金额数字转换为汉字表示
     * 如：35.00 转换为 叁拾伍元整
     *
     * @param num
     * @return
     */
    public static String feeNumberToChinese(Double num) {
        if (num > 99999999999999.99 || num < -99999999999999.99) {
            throw new IllegalArgumentException(
                    "参数值超出允许范围 (-99999999999999.99 ～ 99999999999999.99)！");
        }
        boolean negative = false;// 正负标号
        if (num < 0) {
            negative = true;
            num = num * (-1);
        }
        long temp = Math.round(num * 100);
        int numFen = (int) (temp % 10);// 分
        temp = temp / 10;
        int numJiao = (int) (temp % 10);// 角
        temp = temp / 10;
        // 此时temp只包含整数部分
        int[] parts = new int[20];// 将金额整数部分分为在0-9999之间数的各个部分
        int numParts = 0;// 记录把原来金额整数部分分割为几个部分
        for (int i = 0; ; i++) {
            if (temp == 0) {
                break;
            }
            int part = (int) (temp % 10000);
            parts[i] = part;
            temp = temp / 10000;
            numParts++;
        }
        boolean beforeWanIsZero = true;// 标志位，记录万的下一级是否为0
        String chineseStr = "";
        for (int i = 0; i < numParts; i++) {
            String partChinese = partConvert(parts[i]);
            if (i % 2 == 0) {
                if ("".equals(partChinese)) {
                    beforeWanIsZero = true;
                } else {
                    beforeWanIsZero = false;
                }
            }
            if (i != 0) {
                if (i % 2 == 0)// 亿的部分
                    chineseStr = "亿" + chineseStr;
                else {
                    if ("".equals(partChinese) && !beforeWanIsZero)// 如果“万”对应的
                        // part 为
                        // 0，而“万”下面一级不为
                        // 0，则不加“万”，而加“零”
                        chineseStr = "零" + chineseStr;
                    else {
                        if (parts[i - 1] < 1000 && parts[i - 1] > 0)// 如果万的部分不为0，而万前面的部分小于1000大于0，则万后面应该跟零
                            chineseStr = "零" + chineseStr;
                        chineseStr = "万" + chineseStr;
                    }
                }
            }
            chineseStr = partChinese + chineseStr;
        }
        // 整数部分为0，则表示为零元
        if ("".equals(chineseStr)) {
            chineseStr = CHINESE_NUM[0];
        } else if (negative) { // 整数部分部位0，但是为负数
            chineseStr = "负" + chineseStr;
        }
        chineseStr = chineseStr + "元";
        if (numFen == 0 && numJiao == 0) {
            chineseStr = chineseStr + "整";
        } else if (numFen == 0) {// 0分
            chineseStr = chineseStr + CHINESE_NUM[numJiao] + "角";
        } else {
            if (numJiao == 0) {
                chineseStr = chineseStr + "零" + CHINESE_NUM[numFen] + "分";
            } else {
                chineseStr = chineseStr + CHINESE_NUM[numJiao] + "角"
                        + CHINESE_NUM[numFen] + "分";
            }
        }
        return chineseStr;
    }

    /**
     * 转换拆分后的每个部分，0-9999之间
     */
    public static String partConvert(int partNum) {
        if (partNum < 0 || partNum > 10000) {
            throw new IllegalArgumentException("参数必须是大于等于0或小于10000的整数");
        }
        String[] units = new String[]{"", "拾", "佰", "仟"};
        int temp = partNum;
        String partResult = new Integer(partNum).toString();
        int partResultLength = partResult.length();
        boolean lastIsZero = true;// 记录上一位是否为0
        String chineseStr = "";
        for (int i = 0; i < partResultLength; i++) {
            // 高位无数字
            if (temp == 0) {
                break;
            }
            int digit = temp % 10;
            if (digit == 0) {
                if (!lastIsZero)// 如果前一个数字不是0则在当前汉字串前加零
                    chineseStr = "零" + chineseStr;
                lastIsZero = true;
            } else {
                chineseStr = CHINESE_NUM[digit] + units[i] + chineseStr;
                lastIsZero = false;
            }
            temp = temp / 10;
        }
        return chineseStr;
    }

    /**
     * 获取项目基准url
     *
     * @return
     */
    public static String getBasicUrl() {
        return getRequest().getScheme() + "://" + getRequest().getServerName() + ":" +
                getRequest().getServerPort() + "/";
    }

    /**
     * 将一串用`CommonConstant.SPLIT_ID_SIGN`分割的字符串拼接成`tableId` `not in` (a, b, c)的格式
     *
     * @param ids
     * @param tableId
     * @return
     */
    public static String madeSqlNotIn(String ids, String tableId) {
        return madeSqlNotIn(ids.split(CommonConstant.SPLIT_ID_SIGN), tableId);
    }

    /**
     * 将字符串数组拼接成`tableId` `not in` (a, b, c)的格式
     *
     * @param ids
     * @param tableId
     * @return
     */
    public static String madeSqlNotIn(Object[] ids, String tableId) {
        return madeSqlNotIn(Arrays.asList(ids), tableId);
    }

    /**
     * 将字符串集合拼接成`tableId` `not in` (a, b, c)的格式
     *
     * @param ids
     * @param tableId
     * @return
     */
    public static String madeSqlNotIn(Collection ids, String tableId) {
        return madeSqlForSign(ids, tableId, "not in");
    }

    /**
     * 将字符串集合拼接成 `tableId` `sign` (a, b, c)的格式
     *
     * @param ids
     * @param tableId
     * @param sign
     * @return
     */
    private static String madeSqlForSign(Collection<?> ids, String tableId, String sign) {
        List<String> strList = ids.stream().filter(Objects::nonNull)
                .map(String::valueOf).map(String::trim)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(strList)) {
            return " 1 = 0 ";
        }
        // 分段临界值，超过num会分段
        int num = 1000;
        StringBuilder sb = new StringBuilder();
        if (strList.size() < num) {
            sb.append(" ").append(tableId).append(" ").append(sign).append(" (");
            strList.forEach(e -> sb.append("'").append(e.trim()).append("', "));
            sb.delete(sb.length() - 2, sb.length());
            sb.append(")");
        } else {
            // 每段大小
            int subLength = 900;
            sb.append("( ");
            int index = 0;
            for (String element : strList) {
                if (index % subLength == 0) {
                    sb.append(tableId).append(" ").append(sign).append(" (");
                }
                sb.append("'").append(element.trim()).append("'");
                if ((index + 1) % subLength == 0 || index == strList.size() - 1) {
                    sb.append(")");
                    if (index != strList.size() - 1) {
                        sb.append(" or ");
                    }
                } else {
                    sb.append(", ");
                }
                index++;
            }
            sb.append(" )");
        }

        return sb.toString();
    }

    /**
     * 将解决路径改为相对路径
     *
     * @param path
     * @return
     */
    public static String getRelativePath(String path) {
        path = path.replace(getRealPath("/"), "");
        return path.replace(File.separator, "/");
    }

    /**
     * 从请求体中获取参数
     *
     * @param request
     * @param clazz
     * @return
     */
    public static <T> T readParamsFromRequestBody(HttpServletRequest request,
                                                  Class<T> clazz) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonString = IOUtils.toString(reader);
        return StringUtils.isBlank(jsonString) ? null
                : (T) JSONObject.toBean(JSONObject.fromObject(jsonString), clazz);
    }

    /**
     * 获取网络协议
     * @return
     */
    public static String getScheme() {
        return CommonUtils.getRequest().getScheme() + "://";
    }

    /**
     * 将某个对象转换成另外一种类型的对象
     *
     * @param origin
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T convertBean(Object origin, Class<T> targetClass)
            throws IllegalAccessException, InstantiationException {
        T target = targetClass.newInstance();
        Arrays.stream(origin.getClass().getMethods())
                .filter(e -> e.getName().startsWith("get") && checkFieldHasAnnotation(origin.getClass(),
                        lowerFirstLetter(e.getName().substring(3)), ExcludeField.class))
                .sorted(Comparator.comparingInt(e -> exactSortFromField(origin.getClass(),
                        lowerFirstLetter(e.getName().substring(3)))))
                .forEach(e -> {
                    String fieldName = lowerFirstLetter(e.getName().substring(3));
                    try {
                        Object value = e.invoke(origin);
                        if (value != null) {
                            Method setMethod = getMethod(targetClass, "set" + upperFirstLetter(fieldName),
                                    true, value.getClass());
                            setMethod.invoke(target, value);
                        }
                    } catch (NoSuchMethodException e1) {
                        if (!"class".equals(fieldName)) {
                            logger.warn(String.format("no such set method for field %s", fieldName));
                        }
                    } catch (Exception e1) {
                        logger.warn("copy field failure", e1);
                    }
                });
        return target;
    }

    /**
     * 提取字段的顺序
     * @param clazz
     * @param name
     * @return
     */
    private static int exactSortFromField(Class<?> clazz, String name) {
        Field field = getField(clazz, name);
        return field == null || field.getAnnotation(FieldOrder.class) == null
                ? 0 : field.getAnnotation(FieldOrder.class).value();
    }

    /**
     * 检查字段是否有此注解
     *
     * @param clazz
     * @param name
     * @param annotation
     * @return
     */
    public static boolean checkFieldHasAnnotation(Class clazz, String name, Class annotation) {
        Field field = getField(clazz, name);
        return field == null || field.getAnnotation(annotation) == null;
    }

    /**
     * 获取字段
     * @param clazz
     * @param name
     * @return
     */
    public static Field getField(Class clazz, String name) {
        if (clazz == null) {
            return null;
        }
        Field field;
        try {
            field = clazz.getField(name);
        } catch (NoSuchFieldException e1) {
            try {
                field = clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e2) {
                field = getField(clazz.getSuperclass(), name);
            }
        }
        return field;
    }

    /**
     * 将map中的数值放到实体类中
     * @param target
     * @param valueMap
     * @throws IllegalArgumentException
     */
    public static void setField(Object target, Map<String, ?> valueMap) throws IllegalArgumentException {
        valueMap.forEach((k, v) -> {
            Field field = getField(target.getClass(), k);
            field.setAccessible(true);
            try {
                field.set(target, v);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }

    /**
     * 获取方法
     *
     * @param clazz
     * @param name
     * @param convertPackageClass
     * @param args
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getMethod(Class clazz, String name, boolean convertPackageClass, Class args)
            throws NoSuchMethodException {
        try {
            return getMethod(clazz, name, args);
        } catch (NoSuchMethodException e) {
            if (convertPackageClass && PackageClass.isPackageClass(args)) {
                return getMethod(clazz, name, PackageClass.getBasicClass(args));
            }
            throw e;
        }
    }

    /**
     * 获取方法
     *
     * @param clazz
     * @param name
     * @param args
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getMethod(Class clazz, String name, Class args) throws NoSuchMethodException {
        if (clazz == null) {
            throw new NoSuchMethodException(String.format("no such method for class %s", name));
        }
        try {
            return clazz.getMethod(name, args);
        } catch (NoSuchMethodException e) {
            try {
                return clazz.getDeclaredMethod(name, args);
            } catch (NoSuchMethodException e1) {
                return getMethod(clazz.getSuperclass(), name, args);
            }
        }
    }

    /**
     * 导出照片到zip文件
     * @param list
     * @param outputStream
     */
    public static void exportPictureToZipFile(List<Object[]> list,OutputStream outputStream) {
        Set set = new HashSet();
        try (ZipOutputStream out = new ZipOutputStream(outputStream);
             BufferedOutputStream outa = new BufferedOutputStream(out)) {
            for (int i = 0; i < list.size(); i++) {
                String photoLinkRecruit = String.valueOf(list.get(i)[0]);
                if (photoLinkRecruit == null) {
                    continue;
                }
                String photoLink = photoLinkRecruit;
                // 获取当时绝对路径
                if (set.add(photoLink)) {
                    int buffer = 2048;
                    byte[] data = new byte[buffer];
                    File in = new File(CommonUtils.getRealPath(photoLink));
                    if (!in.exists()) {
                        continue;
                    }
                    InputStream fi = new FileInputStream(in);
                    try (BufferedInputStream origin = new BufferedInputStream(fi, buffer)) {
                        String picName = String.valueOf(list.get(i)[1]) + photoLinkRecruit.substring(
                                photoLinkRecruit.lastIndexOf("."), photoLinkRecruit.length());
                        ZipEntry entry = new ZipEntry(picName);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, buffer)) != -1) {
                            outa.write(data, 0, count);
                        }
                    }
                    outa.flush();
                }
            }
        } catch (IOException e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 将utc日期转化成字符串
     * @param time
     * @return
     */
    public static Date convertUTCDate(String time) {
        Date utcDate = changeStringToDateTime(time, CommonConstant.UTC_DATE_FORMAT_STRING);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(utcDate);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        return calendar.getTime();
    }

    private enum PackageClass {

        INTEGER(Integer.class, int.class),
        LONG(Long.class, long.class),
        DOUBLE(Double.class, double.class),
        FLOAT(Float.class, float.class),
        SHORT(Short.class, short.class),
        BOOLEAN(Boolean.class, boolean.class),
        ;

        private Class packageClass;

        private Class basicClass;

        PackageClass(Class packageClass, Class basicClass) {
            this.packageClass = packageClass;
            this.basicClass = basicClass;
        }

        public static boolean isPackageClass(Class clazz) {
            return Arrays.stream(values()).map(e -> e.packageClass)
                    .collect(Collectors.toList()).contains(clazz);
        }

        public static Class getBasicClass(Class clazz) {
            return Arrays.stream(values()).filter(e -> e.packageClass == clazz)
                    .map(e -> e.basicClass).findFirst().orElseGet(null);
        }
    }

    /**
     * 将日期的时间改变为23:59:59
     * @param origin
     * @return
     */
    public static Date convertDateToDayEnd(Date origin) {
        return changeStringToDateTime(changeDateToString(origin) + " 23:59:59");
    }

    /**
     * 获取文件大小
     * @param size
     * @return
     */
    public static String getFileSize(Long size) {
        if (size < 1024 * 1024) {
            return (size / 1024) + "K";
        } else {
            return new BigDecimal(size).divide(new BigDecimal(1024 * 1024), 2, RoundingMode.HALF_UP) + "M";
        }
    }

    /**
     * 复制成员属性
     * <p>
     * 此方法只复制非final的和非static的成员属性，且为浅层复制
     *
     * @param origin
     * @param target
     * @param <T>
     * @throws IllegalArgumentException
     */
    public static <T> void copyMember(T origin, T target) throws IllegalAccessException {
        if (!origin.getClass().equals(target.getClass())
                && !origin.getClass().isAssignableFrom(target.getClass())) {
            throw new IllegalArgumentException(String.format("class for %s is not assignable from %s",
                    target.getClass().getName(), origin.getClass().getName()));
        }
        Class clazz = origin.getClass();
        while (!Object.class.equals(clazz)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(origin);
                if (value != null) {
                    field.set(target, value);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 把map转换成对象
     * @param map
     * @param clazz
     * @return
     *
     * 把Map转换成指定类型
     */
    @SuppressWarnings("rawtypes")
    public static <T> T convertMapToBean(Map map, Class<T> clazz) {
        if(map == null) {
            return null;
        }
        try {
            /*
             * 1. 通过参数clazz创建实例
             * 2. 使用BeanUtils.populate把map的数据封闭到bean中
             */
            T bean = clazz.newInstance();
            ConvertUtils.register(new DateConverter(), java.util.Date.class);
            BeanUtils.populate(bean, map);
            return bean;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成二维码
     *
     * @param origin
     * @return
     */
    public static String generateQRCode(String origin, int width, int height) {
        ByteArrayOutputStream out = QRCode.from(origin).withSize(width, height).to(ImageType.PNG).stream();
        byte[] data = out.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        return data != null ? encoder.encode(data) : "";
    }

    /**
     * kmp算法实现的字符串搜索
     *
     * @param source
     * @param pattern
     * @return
     */
    public static int kmpIndexOf(String source, String pattern) {
        return kmpIndexOf(source.toCharArray(), pattern.toCharArray());
    }

    /**
     * kmp算法实现的字符串搜索
     * @param source
     * @param pattern
     * @return
     */
    public static int kmpIndexOf(char[] source, char[] pattern) {
        int[] next = generateNextArr(pattern);
        int i = 0;
        int j = 0;
        while (i < source.length && j < pattern.length) {
            if (j == -1 || source[i] == pattern[j]) {
                i ++;
                j ++;
            } else {
                j = next[j];
            }
        }
        return j == pattern.length ? (i - j) : -1;
    }

    /**
     * 寻找传入的原数组每个元素的最大公共前缀后缀
     * @param origin
     * @return
     */
    private static int[] generateNextArr(char[] origin) {
        int[] next = new int[origin.length];
        next[0] = -1;
        for (int i = 1; i < origin.length; i++) {
            int fromIndex = 0;
            int commonFix = 0;
            while (fromIndex <= i) {
                /*
                 * 查找最大公共前缀后缀
                 */
                fromIndex = contains(origin, origin[0], fromIndex + 1, i + 1);
                if (fromIndex < 0) {
                    break;
                }
                // 后缀
                char[] suffix = Arrays.copyOfRange(origin, fromIndex, i);
                // 前缀
                char[] prefix = Arrays.copyOfRange(origin, 0, i - fromIndex);
                if (Arrays.equals(suffix, prefix)) {
                    commonFix = prefix.length;
                    break;
                }
            }
            next[i] = commonFix;
        }
        return next;
    }

    /**
     * 查找元素在数组中的位置
     * @param source
     * @param target
     * @param fromIndex
     * @param toIndex
     * @return
     */
    private static int contains(char[] source, char target, int fromIndex,  int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            if (source[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 替换同名文件
     * @param file
     * @param oldFileUrl
     * @param newFileUrl
     * @throws IOException
     */
    public static void replaceSameNameFile(File file, String oldFileUrl, String newFileUrl) throws IOException {
        File bakFile = null;
        if (StringUtils.isNotBlank(oldFileUrl)) {
            File oldFile = new File(CommonUtils.getRealPath(oldFileUrl));
            if (oldFile.exists()) {
                bakFile = CommonUtils.bakFile(oldFile);
                oldFile.delete();
            }
        }
        try {
            FileUtils.copyFile(file, new File(CommonUtils.getRealPath(newFileUrl)));
        } catch (Exception e) {
            if (bakFile != null) {
                CommonUtils.rollbackFile(bakFile);
            }
            throw e;
        } finally {
            if (bakFile != null) {
                bakFile.delete();
            }
        }
    }

    /**
     * 保留几位小数
     *
     * @param originNumber 源数据
     * @param digit  保留位数
     * @return
     */
    public static double keepDigit(double originNumber, int digit) {
        if(StringUtils.isBlank(String.valueOf(originNumber))) {
            return 0.0;
        }
        BigDecimal bg = new BigDecimal(originNumber);
        return bg.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     *  byte 转换成2进制字符串
     *
     * @param b
     * @return
     */
    public static String getBinaryStrFromByte(byte b){
        String result ="";
        byte a = b; ;
        for (int i = 0; i < 8; i++){
            byte c=a;
            a=(byte)(a>>1);//每移一位如同将10进制数除以2并去掉余数。
            a=(byte)(a<<1);
            if(a==c){
                result="0"+result;
            }else{
                result="1"+result;
            }
            a=(byte)(a>>1);
        }
        return StringUtils.reverse(result);
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }

            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }

        return obj;
    }
}
