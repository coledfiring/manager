package com.whaty.framework.api.grant.service.impl;

import com.whaty.cache.CacheKeys;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.api.grant.constant.GrantConstant;
import com.whaty.framework.api.grant.service.ApiLoginService;
import com.whaty.framework.login.support.SimulateLoginSupport;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录接口服务实现类
 *
 * @author weipengsen
 */
@Lazy
@Service("apiLoginService")
public class ApiLoginServiceImpl implements ApiLoginService {

    @Resource(name = "simulateLoginSupport")
    private SimulateLoginSupport simulateLoginSupport;

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    @Override
    @LogAndNotice("模拟登录")
    public Map<String, Object> doSimulateLogin(Map<String, String> requestMap, HttpServletResponse response)
            throws Exception {
        return this.simulateLoginSupport.simulate(requestMap.get("loginId"), requestMap.get("userId"), response);
    }

    @Override
    public void clearLoginCache(Map<String, String> requestMap) {
        this.redisCacheService.remove(CacheKeys.LOGIN_CACHE_KEYS
                .getKeyWithParams(CommonUtils.getServerName(), requestMap.get(GrantConstant.PARAM_LOGIN_ID)));
    }
}
