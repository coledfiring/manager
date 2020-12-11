package com.whaty.framework.learnspace.impl;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.framework.learnspace.LearningSpaceStandardWebService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 课程空间接口
 * @author chenatu
 */
@Service("learningSpaceStandardWebService")
public class LearningSpaceStandardWebServiceImpl implements LearningSpaceStandardWebService {

    @Resource(name = "redisCacheService")
    private RedisCacheService redisCacheService;

    /**
     * 供learnspace调用，判断用户是否登录
     *
     * @param loginId  column LOGIN_ID in table sso_user
     * @param learnSpaceSiteCode 课程空间中的站点code
     * @return 登录返回true，未登录返回false
     * @throws Exception
     */
    @Override
    public boolean validateUserOnlineStatus(String loginId, String learnSpaceSiteCode) throws Exception {
        return true;
//        if (StringUtils.isBlank(learnSpaceSiteCode) || StringUtils.isBlank(loginId)) {
//            return false;
//        }
//        String siteCode = null;
//        for (Map.Entry<String, LearnSpaceConfig> entry : BasicApplicationContext.LEARN_SPACE_CONFIG_MAP.entrySet()) {
//            if (learnSpaceSiteCode.equals(entry.getValue().getLeanSpaceSiteCode())) {
//                siteCode = entry.getKey();
//                break;
//            }
//        }
//
//        // 没有对应的mySiteCode
//        if (null == siteCode) {
//            return false;
//        }
//
//        // 从缓存中查找用户正常登录时存放的值
//        String host = BasicApplicationContext.CODE_KEY_SITE_MAP.get(siteCode).getDomain();
//        return null != this.getRedisCacheService().getFromCache(host + "@@<" + loginId + ">@@")
//                || null != this.getRedisCacheService().getFromCache(host + "::simulate<" + loginId + ">::");
    }

    public RedisCacheService getRedisCacheService() {
        return redisCacheService;
    }

    public void setRedisCacheService(RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }

}

