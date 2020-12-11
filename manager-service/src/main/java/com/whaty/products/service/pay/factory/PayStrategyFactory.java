package com.whaty.products.service.pay.factory;

import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.products.service.pay.strategy.AbstractPayStrategy;

/**
 * 交费策略工厂
 *
 * @author weipengsen
 */
public class PayStrategyFactory {

    /**
     * 生产支付策略
     * @param payType
     * @return
     */
    public static AbstractPayStrategy newInstance(String payType) {
        switch (payType) {
            case "wxh5":
                return (AbstractPayStrategy) SpringUtil.getBean("weChatH5PayStrategy");
            case "wxjsapi":
                return (AbstractPayStrategy) SpringUtil.getBean("weChatJsApiPayStrategy");
            case "wxApplet":
                return (AbstractPayStrategy) SpringUtil.getBean("weChatMiniProgramPayStrategy");
            case "wxNative":
                return (AbstractPayStrategy) SpringUtil.getBean("weChatNativePayStrategy");
            case "wxApp":
                return (AbstractPayStrategy) SpringUtil.getBean("weChatAppPayStrategy");
            case "alipayNative":
                return (AbstractPayStrategy) SpringUtil.getBean("alipayNativeStrategy");
            case "alipayApp":
                return (AbstractPayStrategy) SpringUtil.getBean("alipayAppStrategy");
            default:
                throw new ParameterIllegalException();
        }
    }

}
