package com.whaty.notice.util;

import com.alibaba.fastjson.JSON;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.User;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.SsoUser;
import com.whaty.file.excel.upload.constant.ExcelConstant;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.DataOperateException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.notice.bean.PeNotice;
import com.whaty.notice.bean.PrUserNotice;
import com.whaty.notice.constant.BeanNamesConstant;
import com.whaty.notice.constant.NoticeServerPollConstant;
import com.whaty.notice.exception.NoticeServerPollException;
import com.whaty.notice.factory.NoticeServerPollFactory;
import com.whaty.notice.server.NoticeServerPollServer;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;

/**
 * 推送个人中心信息工具类
 *
 * @author weipengsen
 */
public class NoticeServerPollUtils {

    private static final Logger logger = LoggerFactory.getLogger(NoticeServerPollUtils.class);

    /**
     * 通用service数据层
     */
    public static GeneralDao generalDao;

    /**
     * 通知推送线程注册池
     */
    private static final Map<String, Thread> THREAD_REGISTER_POOL = new HashMap<>();

    /**
     * 使用提示信息和文件地址拼接下载字符串，如<a href="filePath">info</a>
     *
     * @param info
     * @param filePath
     */
    public static String addDownStr(String info, String filePath) {
        return String.format(NoticeServerPollConstant.NOTICE_PLACE_HOLDER_LINK, filePath, info);
    }

    /**
     * 给自己推送通知
     *
     * @author weipengsen
     */
    public static void selfNotice(String content, String noticeType) {
        if (getUserId() != null) {
            noticeAll(content, noticeType, Collections.singletonList(getUserId()));
        }
    }

    /**
     * 通知所有人且带有路由跳转
     * @param content
     * @param noticeType
     * @param receivers
     * @param params
     */
    public static void noticeAllToRouter(String content, String noticeType, List<String> receivers,
                                         Map<String, Object> params) {
        content += "，" + addRouterStr("点击跳转", params, UUID.randomUUID().toString().replace("-", ""));
        noticeAll(content, noticeType, receivers);
    }

    /**
     * 添加路由跳转
     * @param info
     * @param config
     * @return
     */
    private static String addRouterStr(String info, Map<String, Object> config, String id) {
        return String.format(NoticeServerPollConstant.NOTICE_PLACE_ROUTER_LINK, info, JSON.toJSONString(config),
                "a" + id);
    }

    /**
     * 给所有传入的人推送通知
     * @param content
     * @param noticeType
     * @param receivers
     */
    public static void noticeAll(String content, String noticeType, List<String> receivers) {
        //获取请求对象
        HttpServletRequest request = CommonUtils.getRequest();
        if (request == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("The request object do not found, so can not execute notice");
            }
            return;
        }
        //获取操作人ip
        String operateIP = CommonUtils.getIpAddress();
        //获取操作者用户id
        String sendUserId = getUserId();
        if (StringUtils.isBlank(sendUserId)) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("not found user, the notice of content: %s,noticeType: %s", content, noticeType));
            }
            return;
        }
        //获取请求url
        String requestURL = request.getRequestURL().toString();
        //调用方法
        notice(content, operateIP, NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_UNICAST_TYPE, noticeType,
                sendUserId, requestURL, receivers);
    }

    /**
     * 获得用户id
     *
     * @return
     */
    private static String getUserId() {
        UserService userService = (UserService) SpringUtil.getBean(BeanNames.USER_SERVICE);
        if (userService == null) {
            return null;
        }
        return userService.getCurrentUser() == null ? null : userService.getCurrentUser().getId();
    }

    /**
     * 设置用户所有通知都为已读
     *
     * @param userId
     */
    public static void allRead(String userId) {
        //初始化通用dao对象
        initGeneralDao();
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE pr_user_notice notice                                             ");
        sql.append(" INNER JOIN enum_const re ON re.namespace = 'flagReaded'                  ");
        sql.append(" AND re. CODE = '1'                                                       ");
        sql.append(" SET notice.FLAG_READED = re.ID                                           ");
        sql.append(" WHERE                                                                    ");
        sql.append("    notice.FK_SSO_USER_ID = '" + userId + "'                              ");
        generalDao.executeBySQL(sql.toString());
        //唤醒用户相关注册客户端接收改变的数据
        activeUserNotice(userId);
        String filePath = getRealPath("/templatefile/graduateDegree/excel/degreeCertificateNo.xls");
        new File(filePath).delete();
    }

    /**
     * 删除所有已读未星标的通知
     *
     * @param userId
     */
    public static void deleteAllReadNoStarNotice(String userId) {
        //初始化通用dao对象
        initGeneralDao();
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE pun                                                ");
        sql.append(" FROM                                                      ");
        sql.append(" 	pr_user_notice pun                                     ");
        sql.append(" INNER JOIN enum_const st ON st.id = pun.FLAG_IS_STAR      ");
        sql.append(" INNER JOIN enum_const re ON re.id = pun.FLAG_READED       ");
        sql.append(" INNER JOIN pe_notice pn ON pn.id = pun.fk_notice_id       ");
        sql.append(" WHERE                                                     ");
        sql.append(" 	re. CODE = '1'                                         ");
        sql.append(" AND st. CODE = '0'                                        ");
        sql.append(" AND pun.FK_SSO_USER_ID = '" + userId + "'                 ");
        generalDao.executeBySQL(sql.toString());
        //唤醒用户相关注册客户端接收改变的数据
        activeUserNotice(userId);
    }

    /**
     * 删除时间超过配置的通知
     */
    public static void delTimeoutNotice() {
        //初始化通用service对象
        initGeneralDao();
        Session session = generalDao.getMyHibernateTemplate().getSessionFactory()
                .openSession();
        boolean tranFlag = false;
        try {
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
                tranFlag = true;
            }
            //拿出通知中对应的文件
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT                                                            ");
            sql.append(" 	pn.CONTENT                                                     ");
            sql.append(" FROM                                                              ");
            sql.append(" 	pe_notice pn                                                   ");
            sql.append(" INNER JOIN pr_user_notice un ON un.fk_notice_id = pn.id           ");
            sql.append(" INNER JOIN enum_const st ON st.id = un.FLAG_IS_STAR               ");
            sql.append(" INNER JOIN enum_const re ON re.id = un.FLAG_READED                ");
            sql.append(" WHERE                                                             ");
            sql.append(" 	(                                                              ");
            sql.append(" 		UNIX_TIMESTAMP(now()) - UNIX_TIMESTAMP(pn.CREATE_TIME)     ");
            sql.append(" 	) >= " + NoticeServerPollConstant.NOTICE_DEL_SECTION + "       ");
            sql.append(" AND st. CODE = '0'                                                ");
            sql.append(" AND pn.CONTENT REGEXP '{a:.*:.*}'                                 ");
            sql.append(" AND re.code = '1'                                                 ");
            List<Object> list = session.createSQLQuery(sql.toString()).list();
            //删除超过时间的通知
            List<String> sqlList = new ArrayList<>();
            sql.delete(0, sql.length());
            sql.append(" DELETE                                                                     ");
            sql.append(" 	un                                                                      ");
            sql.append(" FROM                                                                       ");
            sql.append(" 	pe_notice pn                                                            ");
            sql.append(" INNER JOIN pr_user_notice un ON un.fk_notice_id = pn.id                    ");
            sql.append(" INNER JOIN enum_const st on st.id = un.FLAG_IS_STAR                        ");
            sql.append(" INNER JOIN enum_const re ON re.id = un.FLAG_READED                         ");
            sql.append(" WHERE                                                                      ");
            sql.append(" 	(                                                                       ");
            sql.append(" 		UNIX_TIMESTAMP(now()) - UNIX_TIMESTAMP(pn.CREATE_TIME)              ");
            sql.append(" 	) >= " + NoticeServerPollConstant.NOTICE_DEL_SECTION + "                ");
            sql.append(" AND st.code = '0'                                                          ");
            sql.append(" AND re.code = '1'                                                          ");
            sqlList.add(sql.toString());
            sql.delete(0, sql.length());
            sql.append(" DELETE                                                            ");
            sql.append(" 	pn                                                             ");
            sql.append(" FROM                                                              ");
            sql.append(" 	pe_notice pn                                                   ");
            sql.append(" LEFT JOIN pr_user_notice un ON un.fk_notice_id = pn.id            ");
            sql.append(" WHERE                                                             ");
            sql.append("    un.id is null                                                  ");
            sqlList.add(sql.toString());
            sqlList.forEach(e -> session.createSQLQuery(e).executeUpdate());
            //删除通知中对应的文件
            if (CollectionUtils.isNotEmpty(list)) {
                for (Object content : list) {
                    delNoticeFile((String) content);
                }
            }
            if (tranFlag) {
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            if (tranFlag) {
                session.getTransaction().rollback();
            }
            throw new UncheckException(e);
        }
    }

    /**
     * 删除通知信息中的文件
     *
     * @param noticeContent
     */
    private static void delNoticeFile(String noticeContent) {
        Matcher m = NoticeServerPollConstant.NOTICE_FILE_LINK_PATTERN.matcher(noticeContent);
        while (m.find()) {
            //拿到匹配到的通配符
            String fileLink = m.group();
            //去除通配符
            noticeContent.replace(fileLink, "");
            //拿到通配符中的链接
            fileLink = fileLink.substring(fileLink.indexOf(":") + 1,
                    fileLink.lastIndexOf(":"));
            String realPath = getRealPath(fileLink);
            //删除文件
            File file = new File(realPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 不通过request获取绝对地址
     * @param path
     * @return
     */
    private static String getRealPath(String path) {
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        classPath = classPath.replace("/WEB-INF/classes/", "");
        path = classPath + (path.startsWith("/") ? path : ("/" + path));
        return path.replace("/", File.separator);
    }

    /**
     * 生成新通知方法
     *
     * @param content
     * @param operateIP
     * @param scopeType
     * @param noticeType
     * @param sendUserId
     * @param requestURL
     * @author weipengsen
     */
    public static void notice(String content, String operateIP, String scopeType,
                              String noticeType, String sendUserId, String requestURL, List<String> receives) {
        //保存新通知
        saveNewNotice(content, operateIP, scopeType, noticeType,
                sendUserId, requestURL, receives);
        //唤醒通知相关用户
        activeUsersNotice(receives);
    }

    /**
     * 保存新的通知对象
     *
     * @author weipengsen
     */
    private static void saveNewNotice(String content, String operateIP, String scopeType,
                                      String noticeType, String sendUserId, String requestURL, List<String> receives) {
        String currentDataSource = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
        //初始化通用service对象
        initGeneralDao();
        Session session = generalDao.getMyHibernateTemplate().getSessionFactory()
                .openSession();
        boolean tranFlag = false;
        if (!session.getTransaction().isActive()) {
            session.beginTransaction();
            tranFlag = true;
        }
        try {
            //创建通知对象
            PeNotice notice = new PeNotice();
            notice.setContent(content);
            notice.setCreateTime(new Date());
            notice.setSiteCode(SiteUtil.getSiteCode());
            EnumConst flagNoticeType = getEnumConstByNamespaceCode(session,
                    NoticeServerPollConstant.NOTICE_ENUM_CONST_NAMESPACE_NOTICE_TYPE, noticeType);
            notice.setEnumConstByFlagNoticeType(flagNoticeType);
            EnumConst flagScopeType = getEnumConstByNamespaceCode(session,
                    NoticeServerPollConstant.NOTICE_ENUM_CONST_NAMESPACE_SCOPE_TYPE, scopeType);
            notice.setEnumConstByFlagScopeType(flagScopeType);
            notice.setOperateIP(operateIP);
            notice.setRequestURL(requestURL);
            SsoUser ssoUser = (SsoUser) session.get(SsoUser.class, sendUserId);
            notice.setSendUser(ssoUser);
            //保存通知对象
            session.save(notice);
            //创建通知用户对应对象
            List<PrUserNotice> prUserNoticeList = new ArrayList<>();
            //未读常量
            EnumConst noReaded = getEnumConstByNamespaceCode(session,
                    NoticeServerPollConstant.NOTICE_ENUM_CONST_NAMESPACE_READ,
                    NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_NO_READ);
            //非星标信息常量
            EnumConst noStar = getEnumConstByNamespaceCode(session,
                    NoticeServerPollConstant.NOTICE_ENUM_CONST_NAMESPACE_STAR,
                    NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_NO_STAR);
            for (String receive : receives) {
                PrUserNotice userNotice = new PrUserNotice();
                userNotice.setPeNotice(notice);
                SsoUser receiveUser = (SsoUser) session.get(SsoUser.class, receive);
                userNotice.setSsoUser(receiveUser);
                userNotice.setEnumConstByFlagReaded(noReaded);
                userNotice.setEnumConstByFlagIsStar(noStar);
                prUserNoticeList.add(userNotice);
            }
            //保存所有的通知关联
            prUserNoticeList.forEach(e -> session.save(e));
            if (tranFlag) {
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            if (tranFlag) {
                session.getTransaction().rollback();
            }
            throw new UncheckException(e);
        } finally {
            if (tranFlag && session != null) {
                session.close();
            }
            MasterSlaveRoutingDataSource.setDbType(currentDataSource);
        }
    }

    /**
     * 根据namespace，code获得常量
     *
     * @param namespace
     * @param code
     * @return
     */
    private static EnumConst getEnumConstByNamespaceCode(Session session, String namespace, String code) {
        String hql = "from EnumConst e where e.namespace='" + namespace + "' and e.code='" + code + "'";
        List list;
        try {
            list = session.createQuery(hql).list();
        } catch (RuntimeException e) {
            throw new DataOperateException(e);
        }
        if (list != null && list.size() == 1) {
            return (EnumConst) list.get(0);
        }
        return null;
    }

    /**
     * 激活制定的全部用户id的已注册服务器端
     *
     * @author weipengsen
     */
    private static void activeUsersNotice(List<String> userIds) {
        for (String userId : userIds) {
            activeUserNotice(userId);
        }
    }

    /**
     * 激活制定用户id的已注册服务器端
     *
     * @author weipengsen
     */
    private static void activeUserNotice(String userId) {
        //拼接缓存key
        String key = String.format(NoticeServerPollConstant.NOTICE_USER_KEY, SiteUtil.getSiteCode(), userId);
        RedisCacheService redisCacheService = (RedisCacheService) SpringUtil
                .getBean(CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME);
        //获取用户id对应的register集合
        Map<String, Object> registerMap = redisCacheService.getMap(key);
        if (MapUtils.isNotEmpty(registerMap)) {
            //循环register集合判断标志存在并致为活动
            for (String register : registerMap.keySet()) {
                /**
                 * 防止在更改活动标志位的时候标志位被移除,与销毁进程成互斥
                 */
                synchronized (register.intern()) {
                    if (redisCacheService.getFromMap(key, register) != null) {
                        //如果缓存中现在还存在这个key则致标志位为活动
                        redisCacheService.putIntoMap(key, register, true);
                    }
                }
            }
        }
    }

    /**
     * 初始化通用数据服务层
     *
     * @return
     */
    private static void initGeneralDao() {
        if (generalDao == null) {
            generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.GENERAL_DAO_BEAN_NAME);
        }
    }

    /**
     * 导入操作通知错误信息，并获得返回给前台错误提示信息
     *
     * @param infoMap
     * @return
     */
    public static void noticeUploadError(Map<String, Object> infoMap) {
        //拿到错误信息表的路径
        String filePath = (String) infoMap.get(ExcelConstant.UPLOAD_RETURN_FILE_PATH_INFO);
        //返回信息的字符串对象
        StringBuilder info = new StringBuilder();
        info.append(infoMap.get(ExcelConstant.UPLOAD_RETURN_PARAM_INFO));
        if (StringUtils.isNotBlank(filePath)
                && new File(CommonUtils.getRealPath(filePath)).exists()) {
            //存在文件则生成相应通知推送和信息提示
            info.append(",请在导入面板或通知推送中下载错误信息表");
            NoticeServerPollUtils.selfNotice(infoMap.get(ExcelConstant.UPLOAD_RETURN_PARAM_INFO) + "," +
                            NoticeServerPollUtils.addDownStr("点击下载错误信息表",
                                    (String) infoMap.get(ExcelConstant.UPLOAD_RETURN_FILE_PATH_INFO)),
                    NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_USER_OPERATE_TYPE);
        }
        infoMap.put(ExcelConstant.UPLOAD_RETURN_PARAM_INFO, info.toString());
    }

    /**
     * 创建服务端并监听通知
     *
     * @return
     * @author weipengsen
     */
    public static Map<String, Object> startListenNotice(Map<String, Object> params) throws NoticeServerPollException {
        THREAD_REGISTER_POOL.put((String) params.get(NoticeServerPollConstant.NOTICE_PARAM_REGISTER_ID),
                Thread.currentThread());
        NoticeServerPollServer server = ((NoticeServerPollFactory) SpringUtil.getBean(BeanNamesConstant.NOTICE_BEAN_NAME_NOTICE_FACTORY))
                .newInstance(params);
        return server.run(params);
    }

    /**
     * 杀死指定的注册线程
     *
     * @param register
     */
    public static void killRegisterThread(String register) {
        Thread thread = THREAD_REGISTER_POOL.get(register);
        if (thread != null) {
            thread.interrupt();
            THREAD_REGISTER_POOL.remove(register);
        }
    }

    /**
     * 设置指定通知已读
     *
     * @param noticeId
     * @param userId
     * @throws NoticeServerPollException
     */
    public static void singleRead(String noticeId, String userId) throws NoticeServerPollException {
        //初始化通用service对象
        initGeneralDao();
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE pr_user_notice notice                                             ");
        sql.append(" INNER JOIN enum_const re ON re.namespace = 'flagReaded'                  ");
        sql.append(" AND re. CODE = '1'                                                       ");
        sql.append(" SET notice.FLAG_READED = re.ID                                           ");
        sql.append(" WHERE                                                                    ");
        sql.append(" 	notice.FK_NOTICE_ID = '" + noticeId + "'                              ");
        sql.append(" AND notice.FK_SSO_USER_ID = '" + userId + "'                             ");
        generalDao.executeBySQL(sql.toString());
        //唤醒用户相关注册客户端接收改变的数据
        activeUserNotice(userId);
    }

    /**
     * 删除通知信息失败
     *
     * @param userId
     * @param noticeId
     */
    public static void delNotice(String userId, String noticeId) throws NoticeServerPollException {
        initGeneralDao();
        Session session = generalDao.getMyHibernateTemplate().getSessionFactory()
                .openSession();
        boolean tranFlag = false;
        if (!session.getTransaction().isActive()) {
            session.beginTransaction();
            tranFlag = true;
        }
        try {
            //删除用户通知关联
            session.createSQLQuery(String.format("DELETE FROM pr_user_notice WHERE FK_NOTICE_ID = '%s' AND FK_SSO_USER_ID = '%s'",
                    noticeId, userId)).executeUpdate();
            //检查这条通知是否没有人与他关联了
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT                                                   ");
            sql.append(" 	pn.content as content                                 ");
            sql.append(" FROM                                                     ");
            sql.append(" 	pe_notice pn                                          ");
            sql.append(" LEFT JOIN pr_user_notice pun ON pn.id = pun.FK_NOTICE_ID ");
            sql.append(" WHERE                                                    ");
            sql.append(" 	pn.ID = '%s'                                          ");
            sql.append(" AND pun.ID IS NULL                                       ");
            List<Object> contentList = session.createSQLQuery(String.format(sql.toString(), noticeId, userId)).list();
            if (CollectionUtils.isNotEmpty(contentList)) {
                //删除通知
                session.createSQLQuery(String.format("DELETE FROM pe_notice WHERE id = %s", noticeId)).executeUpdate();
                String content = (String) contentList.get(0);
                //删除通知文件
                delNoticeFile(content);
            }
            if (tranFlag) {
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            if (tranFlag) {
                session.getTransaction().rollback();
            }
            throw new UncheckException(e);
        } finally {
            if (tranFlag) {
                session.close();
            }
        }
        activeUserNotice(userId);
    }

    /**
     * 设置星标或取消星标
     * @param userId
     * @param noticeId
     */
    public static void setStar(String userId, String noticeId) {
        //初始化通用service对象
        initGeneralDao();
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE pr_user_notice pun                                         ");
        sql.append(" INNER JOIN pe_notice pn ON pn.id = pun.fk_notice_id               ");
        sql.append(" INNER JOIN enum_const star ON star.namespace = 'FlagIsStar'       ");
        sql.append(" AND star. CODE = '1'                                              ");
        sql.append(" INNER JOIN enum_const noStar ON noStar.namespace = 'FlagIsStar'   ");
        sql.append(" AND noStar. CODE = '0'                                            ");
        sql.append(" INNER JOIN enum_const re ON re.namespace = 'flagReaded'           ");
        sql.append(" AND re. CODE = '1'                                                ");
        sql.append(" SET pun.FLAG_READED = re.ID,                                      ");
        sql.append("    pun.FLAG_IS_STAR =                                             ");
        sql.append(" IF (                                                              ");
        sql.append(" 	pun.FLAG_IS_STAR = star.id,                                    ");
        sql.append(" 	noStar.id,                                                     ");
        sql.append(" 	star.id                                                        ");
        sql.append(" )                                                                 ");
        sql.append(" WHERE                                                             ");
        sql.append(" 	pun.FK_NOTICE_ID = '" + noticeId + "'                          ");
        sql.append(" AND pun.FK_SSO_USER_ID = '" + userId + "'                         ");
        generalDao.executeBySQL(sql.toString());
        //唤醒用户相关注册客户端接收改变的数据
        activeUserNotice(userId);
    }
}
