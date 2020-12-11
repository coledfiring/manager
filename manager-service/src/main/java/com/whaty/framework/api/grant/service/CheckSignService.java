package com.whaty.framework.api.grant.service;

import java.util.Map;

/**
 * 签名校验服务接口
 *
 * @author weipengsen
 */
public interface CheckSignService {

    /**
     * 签名校验
     * @param params
     * @param sign
     * @return
     */
    boolean checkSign(Map<String, String> params, String sign);

}
