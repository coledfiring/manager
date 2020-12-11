package com.whaty.framework.api.grant.service;

import java.util.Map;

/**
 * api接口token服务类接口
 *
 * @author weipengsen
 */
public interface ApiTokenService {

    /**
     * 获取token
     *
     * @throws Exception
     * @return
     */
    Map<String, Object> getToken() throws Exception;

}
