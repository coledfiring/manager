package com.whaty.utils;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.hibernate.dao.impl.ControlGeneralDao;
import com.whaty.core.framework.oauth.RedisTokenStore;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.core.framework.util.MySpringUtil;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.products.service.common.UtilService;
import com.whaty.ucenter.oauth2.sdk.service.UCenterService;

/**
 * 静态bean获取工具类
 *
 * @author weipengsen
 */
public class StaticBeanUtils {

    private static GeneralDao generalDao;

    private static GeneralDao openGeneralDao;

    private static RedisCacheService redisCacheService;

    private static UtilService utilService;

    private static UserService userService;

    private static ControlGeneralDao controlGeneralDao;

    private static LearningSpaceWebService learningSpaceWebService;

    private static UCenterService uCenterService;

    private static RedisTokenStore redisTokenStore;

    public static GeneralDao getGeneralDao() {
        if(generalDao == null) {
            generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.GENERAL_DAO_BEAN_NAME);
        }
        return generalDao;
    }

    public static RedisCacheService getRedisCacheService() {
        if(redisCacheService == null) {
            redisCacheService = (RedisCacheService) SpringUtil.getBean(CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME);
        }
        return redisCacheService;
    }

    public static UtilService getUtilService() {
        if(utilService == null) {
            utilService = (UtilService) SpringUtil.getBean(CommonConstant.UTIL_SERVICE_BEAN_NAME);
        }
        return utilService;
    }

    public static UserService getUserService() {
        if(userService == null) {
            userService = (UserService) SpringUtil.getBean(BeanNames.USER_SERVICE);
        }
        return userService;
    }

    public static ControlGeneralDao getControlGeneralDao() {
        if(controlGeneralDao == null) {
            controlGeneralDao = (ControlGeneralDao) SpringUtil.getBean(CommonConstant.CONTROL_GENERAL_DAO_BEAN_NAME);
        }
        return controlGeneralDao;
    }

    public static LearningSpaceWebService getLearningSpaceWebService() {
        if(learningSpaceWebService == null) {
            learningSpaceWebService = (LearningSpaceWebService) SpringUtil
                    .getBean(CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME);
        }
        return learningSpaceWebService;
    }

    public static GeneralDao getOpenGeneralDao() {
        if(openGeneralDao == null) {
            openGeneralDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
        }
        return openGeneralDao;
    }

    public static UCenterService getUCenterService() {
        if(uCenterService == null) {
            uCenterService = MySpringUtil.getBeanOfType(UCenterService.class);
        }
        return uCenterService;
    }

    public static RedisTokenStore getRedisTokenStore() {
        if (redisTokenStore == null) {
            redisTokenStore = MySpringUtil.getBeanOfType(RedisTokenStore.class);
        }
        return redisTokenStore;
    }
}
