package com.whaty.schedule.util;

import com.whaty.constant.CommonConstant;
import com.whaty.core.bean.AbstractBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 通用工具方法
 *
 * @author weipengsen
 */
public class CommonUtils {

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
     * 将list拼接为sql的in条件。 list达到1000会分成多段
     *
     * @param list    list
     * @param tableId sql id
     * @return tableid in ('....','...','..')
     */
    public static String madeSqlIn(List<String> list, String tableId) {
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
    public static String madeSqlIn(String[] ids, String tableId) {
        return madeSqlIn(Arrays.asList(ids), tableId);
    }

    /**
     * 将ids拼接为sql的in条件。 达到1000会分成多段
     *
     * @param ids
     * @param tableId
     * @return tableid in ('....','...','..')
     */
    public static String madeSqlIn(Collection<String> ids, String tableId) {
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
     * 将字符串集合拼接成`tableId` `not in` (a, b, c)的格式
     *
     * @param ids
     * @param tableId
     * @return
     */
    public static String madeSqlNotIn(Collection<String> ids, String tableId) {
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
    private static String madeSqlForSign(Collection<String> ids, String tableId, String sign) {
        if (CollectionUtils.isEmpty(ids)) {
            return " " + tableId + " " + sign + " ('') ";
        }

        // 分段临界值，超过num会分段
        int num = 1000;
        StringBuilder sb = new StringBuilder();
        if (ids.size() < num) {
            sb.append(" ").append(tableId).append(" ").append(sign).append(" (");
            ids.forEach(e -> sb.append("'").append(e.trim()).append("', "));
            sb.delete(sb.length() - 2, sb.length());
            sb.append(")");
        } else {
            // 每段大小
            int subLength = 900;
            sb.append("( ");
            int index = 0;
            for (String id : ids) {
                if (index % subLength == 0) {
                    sb.append(tableId).append(" ").append(sign).append(" (");
                }
                sb.append("'").append(id.trim()).append("'");
                if ((index + 1) % subLength == 0 || index == ids.size() - 1) {
                    sb.append(")");
                    if (index != ids.size() - 1) {
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
     * 获取基础url
     * @return
     */
    public static String getBasicUrl() {
        return getRequest().getScheme() + "://" + getRequest().getServerName() +
                (getRequest().getServerPort() == 80 ? "" : (":" + getRequest().getServerPort()));
    }

    /**
     * 获取应用路径
     * @return
     */
    public static String getContextUrl() {
        return getBasicUrl() + ("/".equals(getRequest().getContextPath()) ? "" : getRequest().getContextPath());
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
}
