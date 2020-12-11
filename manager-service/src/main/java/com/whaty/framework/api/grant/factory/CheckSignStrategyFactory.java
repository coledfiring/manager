package com.whaty.framework.api.grant.factory;

import com.whaty.framework.api.grant.constant.GrantConstant;
import com.whaty.framework.api.grant.strategy.checksign.AbstractCheckSignStrategy;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.common.spring.SpringUtil;

/**
 * 签名校验策略工厂
 *
 * @author suoqiangqiang
 */
public class CheckSignStrategyFactory {
    /**
     * 生产签名校验策略
     *
     * @param checkType
     * @return
     */
    public static AbstractCheckSignStrategy newInstance(String checkType) {
        switch (checkType) {
            case GrantConstant.CHECK_SIGN_TYPE_SORT_MD5:
                return (AbstractCheckSignStrategy) SpringUtil.getBean("sortMd5CheckSignStrategy");
            default:
                throw new ParameterIllegalException();
        }
    }
}
