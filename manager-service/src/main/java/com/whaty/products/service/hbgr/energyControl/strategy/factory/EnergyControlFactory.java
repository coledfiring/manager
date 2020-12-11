package com.whaty.products.service.hbgr.energyControl.strategy.factory;

import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.products.service.hbgr.energyControl.strategy.AbstractEnergyControlStrategy;

public class EnergyControlFactory {

    /**
     * 基础控制策略
     * @param payType
     * @return
     */
    public static AbstractEnergyControlStrategy newInstance(String payType) {
        switch (payType) {
            case "1":
                return (AbstractEnergyControlStrategy) SpringUtil.getBean("sceneOneControlStrategy");
            default:
                throw new ParameterIllegalException();
        }
    }
}
