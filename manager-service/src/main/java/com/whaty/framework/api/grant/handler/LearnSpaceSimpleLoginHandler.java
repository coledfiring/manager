package com.whaty.framework.api.grant.handler;

import com.whaty.framework.api.grant.constant.GrantConstant;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 课程空间简单登录处理类
 *
 * @author suoqiangqiang
 */
@Lazy
@Component("learnSpaceSimpleLoginHandler")
public class LearnSpaceSimpleLoginHandler {

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    /**
     * 简单登录
     *
     * @param siteCode
     * @param loginId
     * @return
     */
    public void simpleLogin(String siteCode, String loginId) {
        String domain = SiteUtil.getSite(siteCode).getDomain();
        String loginKey = String.format(GrantConstant.LEARN_SPACE_SIMPLE_LOGIN_CACHE_KEY_FORMAT, domain, loginId);
        redisCacheService.putToCache(loginKey, loginKey, 7200);
    }

    /**
     * 退出登录
     * @param siteCode
     * @param loginId
     */
    public void exitSimpleLogin(String siteCode, String loginId) {
        String domain = SiteUtil.getSite(siteCode).getDomain();
        String loginKey = String.format(GrantConstant.LEARN_SPACE_SIMPLE_LOGIN_CACHE_KEY_FORMAT, domain, loginId);
        redisCacheService.remove(loginKey);
    }

    /**
     * 模拟登录
     * @param siteCode
     * @param loginId
     */
    public void simulateLogin(String siteCode, String loginId) {
        String key = String.format(GrantConstant.LEARN_SPACE_SIMULATE_LOGIN_CACHE_KEY_FORMAT,
                SiteUtil.getSite(siteCode).getDomain(), loginId);
        this.redisCacheService.putToCache(key, System.currentTimeMillis(), 60 * 60);
    }
}
