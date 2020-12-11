package com.whaty.framework.api.custom.applet.service;

import java.util.Map;

/**
 * 第三方小程序登录验证接口
 *
 * @author shanshuai
 */
public interface CustomAppletUserAuthService {

    /**
     * 验证用户信息
     * @param paramsMap
     * @return
     */
    Map<String, Object> validateUserInfo(Map<String, String> paramsMap);

}
