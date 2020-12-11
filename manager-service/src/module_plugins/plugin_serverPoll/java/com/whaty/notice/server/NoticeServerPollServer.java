package com.whaty.notice.server;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.util.JsonUtil;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.notice.constant.BeanNamesConstant;
import com.whaty.notice.constant.NoticeServerPollConstant;
import com.whaty.notice.exception.NoticeServerPollException;
import com.whaty.serverpoll.server.AbstractServerPollServer;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心信息推送服务器端
 *
 * @author weipengsen
 */
@Lazy
@Service(BeanNamesConstant.NOTICE_SERVICE_BEAN_NAME)
@Scope("prototype")
public class NoticeServerPollServer extends AbstractServerPollServer {


    /**
     * 注册id
     */
    private String registerId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 对应的缓存的key
     */
    private String cacheKey;

    /**
     * 单次请求超时时间
     */
    private long waitTimeout;
    /**
     * 循环查询睡眠时间
     */
    private long checkTime;

    /**
     * 持久化操作对象
     */
    private GeneralDao generalDao;

    /**
     * redis缓存服务对象
     */
    private RedisCacheService redisCacheService;

    /**
     * 空参构造方法
     */
    protected NoticeServerPollServer() {
        super();
        this.generalDao = (GeneralDao) SpringUtil
                .getBean(BeanNamesConstant.NOTICE_BEAN_NAME_GENERAL_DAO);
        this.redisCacheService = (RedisCacheService) SpringUtil
                .getBean(BeanNamesConstant.NOTICE_BEAN_NAME_REDIS_SERVICE);
    }

    /**
     * 常用构造方法
     *
     * @param userId
     * @param registerId
     * @param waitTimeout
     * @param checkTime
     */
    public NoticeServerPollServer(String userId, String registerId, long waitTimeout, long checkTime, String cacheKey) {
        this();
        this.userId = userId;
        this.cacheKey = cacheKey;
        this.registerId = registerId;
        this.waitTimeout = waitTimeout;
        this.checkTime = checkTime;
    }

    /**
     * 初次初始化对象
     *
     * @param userId
     * @param registerId
     * @param waitTimeout
     * @param checkTime
     */
    public void initServer(String userId, String registerId, long waitTimeout, long checkTime, String cacheKey) {
        this.userId = userId;
        this.cacheKey = cacheKey;
        this.registerId = registerId;
        this.waitTimeout = waitTimeout;
        this.checkTime = checkTime;
    }

    /**
     * 重新初始化对象
     *
     * @param userId
     * @param registerId
     */
    public void reInitServer(String userId, String registerId, String cacheKey) {
        this.registerId = registerId;
        this.userId = userId;
        this.cacheKey = cacheKey;
    }

    @Override
    public Map<String, Object> run(Map<String, Object> args) throws NoticeServerPollException {
        Map<String, Object> result = new LinkedHashMap<>(3);
        //客户端传递的是否此次必定获得信息的标志
        Boolean getInfo = args.get(NoticeServerPollConstant.NOTICE_PARAM_GET_INFO) != null
                && Boolean.parseBoolean((String) args.get(NoticeServerPollConstant.NOTICE_PARAM_GET_INFO));
        long startTime = System.currentTimeMillis();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                //缓存map的key，一个userId对应一个注册id集合，每个注册id对应一个通知共享标志位
                String key = (String) args.get(NoticeServerPollConstant.NOTICE_PARAM_CACHE_KEY);
                //查询缓存标志位
                Boolean hasNew = (Boolean) this.redisCacheService.getFromMap(key, registerId);
                //判断缓存标志位是否为空和是否有新消息
                if (hasNew == null || hasNew || getInfo) {
                    //缓存标志位为空或有新消息则查询信息
                    this.getUserNotice(result, userId, args);
                    //获取的对象的md5值用于校验是否同样的数据一直重复发送
                    String md5Check = CommonUtils.md5(JsonUtil.toJSONString(result));
                    result.put(NoticeServerPollConstant.NOTICE_RESULT_CHECK_CODE, md5Check);
                    //设置标志位为false
                    this.redisCacheService.putIntoMap(key, registerId, false);
                    //跳出循环
                    break;
                } else {
                    //标志位为false则检查是否超时
                    long now = System.currentTimeMillis();
                    if (now >= startTime + waitTimeout) {
                        //超时设置循环控制标志位为false
                        break;
                    } else {
                        //未超时睡眠500ms
                        Thread.sleep(checkTime);
                    }
                }
            }
        } catch (InterruptedException e) {
            result.put("getInfo", true);
        }
        //设置上次请求时间
        this.lastRequestTime = System.currentTimeMillis();
        //设置使用中标志为false
        this.isUsing = false;
        return result;
    }

    /**
     * 获得用户信息
     *
     * @return 返回map集合，count为未读信息数量，info为最新信息List集合
     */
    private void getUserNotice(Map<String, Object> result, String userId, Map<String, Object> args) throws NoticeServerPollException {

        StringBuilder sql = new StringBuilder();
        try {
            //查询所有信息条数
            sql.append(" SELECT                                                              ");
            sql.append(" 	count(pn.id) as allNum,                                          ");
            sql.append(" 	count(if(re.code='0', 1, null)) as noRead,                       ");
            sql.append(" 	count(if(re.code='1', 1, null)) as `read`,                       ");
            sql.append(" 	count(if(st.code='1', 1, null)) as star                          ");
            sql.append(" FROM                                                                ");
            sql.append(" 	pr_user_notice pun                                               ");
            sql.append(" INNER JOIN enum_const re ON re.id = pun.FLAG_READED                 ");
            sql.append(" INNER JOIN enum_const st ON st.id = pun.FLAG_IS_STAR                ");
            sql.append(" INNER JOIN pe_notice pn ON pun.FK_NOTICE_ID = pn.id                 ");
            sql.append(" WHERE                                                               ");
            sql.append(" 	pun.FK_SSO_USER_ID = '" + userId + "'                            ");
            sql.append(" ORDER BY                                                            ");
            sql.append("    re.code,                                                         ");
            sql.append("    st.code desc                                                     ");
            Map<String, Object> countMap = generalDao.getMapBySQL(sql.toString()).get(0);
            //查询所有未读
            sql.delete(0, sql.length());
            sql.append(" SELECT                                                              ");
            sql.append(" 	pn.id as id,                                                     ");
            sql.append(" 	pn.CONTENT as content,                                           ");
            sql.append(" 	date_format(pn.CREATE_TIME, '%Y年%m月%d日 %T') as createTime,     ");
            sql.append(" 	if(re. CODE = '0', false, true) as isRead,                       ");
            sql.append(" 	if(st. CODE = '0', false, true) as isStar,                       ");
            sql.append(" 	noType. NAME as type                                             ");
            sql.append(" FROM                                                                ");
            sql.append(" 	pe_notice pn                                                     ");
            sql.append(" INNER JOIN pr_user_notice pun ON pun.FK_NOTICE_ID = pn.id           ");
            sql.append(" INNER JOIN enum_const re ON re.id = pun.FLAG_READED                 ");
            sql.append(" INNER JOIN enum_const st ON st.id = pun.FLAG_IS_STAR                ");
            sql.append(" INNER JOIN enum_const noType ON noType.id = pn.FLAG_NOTICE_TYPE     ");
            sql.append(" WHERE                                                               ");
            sql.append(" 	pun.FK_SSO_USER_ID = '" + userId + "'                            ");
            sql.append(" AND re.code = '0'                                                   ");
            sql.append(" ORDER BY                                                            ");
            sql.append(" 	pn.id DESC                                                       ");
            sql.append(" LIMIT 0, " + (args.get(NoticeServerPollConstant.NOTICE_PARAM_NO_READ_LIMIT) == null
                    ? 10 : args.get(NoticeServerPollConstant.NOTICE_PARAM_NO_READ_LIMIT)));
            List<Map<String, Object>> resultList = generalDao.getMapBySQL(sql.toString());
            // 判断是否显示更多按钮
            int total = Integer.parseInt(String.valueOf(countMap.get("noRead")));
            int searchNum = CollectionUtils.isEmpty(resultList) ? 0 : resultList.size();
            Map<String, Object> noReadNotice = new HashMap<>(4);
            noReadNotice.put("id", "no-read-notice");
            noReadNotice.put("title", "未读");
            noReadNotice.put("showMoreBtn", total > searchNum);
            noReadNotice.put("data", resultList);
            noReadNotice.put("noReadNum", total);
            result.put("noReadNotice", noReadNotice);
            //查询所有已读
            sql.delete(0, sql.length());
            sql.append(" SELECT                                                              ");
            sql.append(" 	pn.id as id,                                                     ");
            sql.append(" 	pn.CONTENT as content,                                           ");
            sql.append(" 	date_format(pn.CREATE_TIME, '%Y年%m月%d日 %T') as createTime,     ");
            sql.append(" 	if(re. CODE = '0', false, true) as isRead,                       ");
            sql.append(" 	if(st. CODE = '0', false, true) as isStar,                       ");
            sql.append(" 	noType. NAME as type                                             ");
            sql.append(" FROM                                                                ");
            sql.append(" 	pe_notice pn                                                     ");
            sql.append(" INNER JOIN pr_user_notice pun ON pun.FK_NOTICE_ID = pn.id           ");
            sql.append(" INNER JOIN enum_const re ON re.id = pun.FLAG_READED                 ");
            sql.append(" INNER JOIN enum_const st ON st.id = pun.FLAG_IS_STAR                ");
            sql.append(" INNER JOIN enum_const noType ON noType.id = pn.FLAG_NOTICE_TYPE     ");
            sql.append(" WHERE                                                               ");
            sql.append(" 	pun.FK_SSO_USER_ID = '" + userId + "'                            ");
            sql.append(" AND re.code = '1'                                                   ");
            sql.append(" ORDER BY                                                            ");
            sql.append(" 	pn.id DESC                                                       ");
            sql.append(" LIMIT 0, " + (args.get(NoticeServerPollConstant.NOTICE_PARAM_READ_LIMIT) == null
                    ? 10 : args.get(NoticeServerPollConstant.NOTICE_PARAM_READ_LIMIT)));
            resultList = generalDao.getMapBySQL(sql.toString());
            // 判断是否显示更多按钮
            total = Integer.parseInt(String.valueOf(countMap.get("read")));
            searchNum = CollectionUtils.isEmpty(resultList) ? 0 : resultList.size();
            Map<String, Object> readNotice = new HashMap<>(4);
            readNotice.put("id", "read-notice");
            readNotice.put("title", "已读");
            readNotice.put("showMoreBtn", total > searchNum);
            readNotice.put("data", resultList);
            result.put("readNotice", readNotice);
            //查询所有星标
            sql.delete(0, sql.length());
            sql.append(" SELECT                                                              ");
            sql.append(" 	pn.id as id,                                                     ");
            sql.append(" 	pn.CONTENT as content,                                           ");
            sql.append(" 	date_format(pn.CREATE_TIME, '%Y年%m月%d日 %T') as createTime,     ");
            sql.append(" 	if(re. CODE = '0', false, true) as isRead,                       ");
            sql.append(" 	if(st. CODE = '0', false, true) as isStar,                       ");
            sql.append(" 	noType. NAME as type                                             ");
            sql.append(" FROM                                                                ");
            sql.append(" 	pe_notice pn                                                     ");
            sql.append(" INNER JOIN pr_user_notice pun ON pun.FK_NOTICE_ID = pn.id           ");
            sql.append(" INNER JOIN enum_const re ON re.id = pun.FLAG_READED                 ");
            sql.append(" INNER JOIN enum_const st ON st.id = pun.FLAG_IS_STAR                ");
            sql.append(" INNER JOIN enum_const noType ON noType.id = pn.FLAG_NOTICE_TYPE     ");
            sql.append(" WHERE                                                               ");
            sql.append(" 	pun.FK_SSO_USER_ID = '" + userId + "'                            ");
            sql.append(" AND st.code = '1'                                                   ");
            sql.append(" ORDER BY                                                            ");
            sql.append(" 	pn.id DESC                                                       ");
            sql.append(" LIMIT 0, " + (args.get(NoticeServerPollConstant.NOTICE_PARAM_STAR_LIMIT) == null
                    ? 10 : args.get(NoticeServerPollConstant.NOTICE_PARAM_STAR_LIMIT)));
            resultList = generalDao.getMapBySQL(sql.toString());
            // 判断是否显示更多按钮
            total = Integer.parseInt(String.valueOf(countMap.get("star")));
            searchNum = CollectionUtils.isEmpty(resultList) ? 0 : resultList.size();
            Map<String, Object> starNotice = new HashMap<>(4);
            starNotice.put("id", "star-notice");
            starNotice.put("title", "重要");
            starNotice.put("showMoreBtn", total > searchNum);
            starNotice.put("data", resultList);
            result.put("starNotice", starNotice);

            //查询所有通知
            sql.delete(0, sql.length());
            sql.append(" SELECT                                                              ");
            sql.append(" 	pn.id as id,                                                     ");
            sql.append(" 	pn.CONTENT as content,                                           ");
            sql.append(" 	date_format(pn.CREATE_TIME, '%Y年%m月%d日 %T') as createTime,     ");
            sql.append(" 	if(re. CODE = '0', false, true) as isRead,                       ");
            sql.append(" 	if(st. CODE = '0', false, true) as isStar,                       ");
            sql.append(" 	noType. NAME as type                                             ");
            sql.append(" FROM                                                                ");
            sql.append(" 	pe_notice pn                                                     ");
            sql.append(" INNER JOIN pr_user_notice pun ON pun.FK_NOTICE_ID = pn.id           ");
            sql.append(" INNER JOIN enum_const re ON re.id = pun.FLAG_READED                 ");
            sql.append(" INNER JOIN enum_const st ON st.id = pun.FLAG_IS_STAR                ");
            sql.append(" INNER JOIN enum_const noType ON noType.id = pn.FLAG_NOTICE_TYPE     ");
            sql.append(" WHERE                                                               ");
            sql.append(" 	pun.FK_SSO_USER_ID = '" + userId + "'                            ");
            sql.append(" ORDER BY                                                            ");
            sql.append(" 	pn.id DESC                                                       ");
            sql.append(" LIMIT 0, " + (args.get(NoticeServerPollConstant.NOTICE_PARAM_ALL_LIMIT) == null
                    ? 10 : args.get(NoticeServerPollConstant.NOTICE_PARAM_ALL_LIMIT)));
            resultList = generalDao.getMapBySQL(sql.toString());
            // 判断是否显示更多按钮
            total = Integer.parseInt(String.valueOf(countMap.get("allNum")));
            searchNum = CollectionUtils.isEmpty(resultList) ? 0 : resultList.size();
            Map<String, Object> allNotice = new HashMap<>(4);
            allNotice.put("id", "all-notice");
            allNotice.put("title", "全部");
            allNotice.put("showMoreBtn", total > searchNum);
            allNotice.put("data", resultList);
            result.put("allNotice", allNotice);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoticeServerPollException(NoticeServerPollConstant.NOTICE_INFO_GET_INFO_FAILURE);
        }
    }

    @Override
    public void destroy() {
        //清空缓存
        //用户下没有任何客户端则删除用户缓存
        this.redisCacheService.removeFromMap(cacheKey, registerId);
        //如果用户没有一个注册客户端则删除用户对应的map
        if (MapUtils.isEmpty(this.redisCacheService.getMap(cacheKey))) {
            this.redisCacheService.remove(cacheKey);
        }
        this.cacheKey = null;
        //清空变量
        this.userId = null;
        this.registerId = null;
        this.lastRequestTime = 0;
    }

    @Override
    public String getKey() {
        return this.registerId;
    }

}
