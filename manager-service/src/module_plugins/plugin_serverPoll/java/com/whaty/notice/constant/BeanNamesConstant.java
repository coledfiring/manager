package com.whaty.notice.constant;

/**
 * 通知推送中使用的spring bean name常量
 * @author weipengsen
 */
public interface BeanNamesConstant {

    /**
     * 推送通知需要的bean
     */
    String NOTICE_BEAN_NAME_GENERAL_DAO = "generalDao";
    /**
     * 推送通知需要的bean
     */
    String NOTICE_BEAN_NAME_REDIS_SERVICE = "redisCacheService";
    /**
     * 推送通知需要的bean
     */
    String NOTICE_BEAN_NAME_NOTICE_FACTORY = "noticeServerPollFactory";
    /**
     * 推送通知服务类bean
     */
    String NOTICE_SERVICE_BEAN_NAME = "noticeServerPollService";
}
