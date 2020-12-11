package com.whaty.framework.api.grant.strategy.checksign;

import java.util.Map;

/**
 * 签名校验抽象策略
 *
 * @author suoqiangqiang
 */
public abstract class AbstractCheckSignStrategy {

    /**
     * 校验签名
     * @param paramsMap
     * @param sign
     * @return
     */
    public abstract boolean checkSign(Map<String, String> paramsMap, String sign);
}
