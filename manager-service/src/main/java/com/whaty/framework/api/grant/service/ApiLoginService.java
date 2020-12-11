package com.whaty.framework.api.grant.service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录接口service
 *
 * @author weipengsen
 */
public interface ApiLoginService {

    /**
     * 模拟登录接口
     * @param requestMap
     * @param response
     * @return
     * @throws Exception
     */
    Map<String, Object> doSimulateLogin(Map<String, String> requestMap, HttpServletResponse response) throws Exception;

    /**
     * 清除用户登录缓存
     *
     * @param requestMap
     */
    void clearLoginCache(Map<String, String> requestMap);
}
