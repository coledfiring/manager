package com.whaty.products.service.hbgr.warning.strategy.factory;

import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.products.service.hbgr.warning.strategy.AbstractWarningStrategy;

public class WarningFactory {

    /**
     * 金融报警预警策略
     * @param payType
     * @return
     */
    public static AbstractWarningStrategy newInstance(String payType) {
        switch (payType) {
            case "1":
                return (AbstractWarningStrategy) SpringUtil.getBean("sceneOneStrategy");
            default:
                throw new ParameterIllegalException();
        }
    }
}
