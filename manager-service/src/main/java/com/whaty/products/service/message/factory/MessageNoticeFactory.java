package com.whaty.products.service.message.factory;

import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.products.service.message.strategy.AbstractNoticeStrategy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 信息通知工厂
 *
 * @author weipengsen
 */
@Lazy
@Component("messageNoticeFactory")
public class MessageNoticeFactory {

    /**
     * 获取实现对象
     * @param messageType
     * @return
     */
    public static AbstractNoticeStrategy newInstance(String messageType) {
        switch (messageType) {
            case "weChatGroup":
                return (AbstractNoticeStrategy) SpringUtil.getBean("weChatGroupNoticeStrategy");
            case "weChatTemplate":
                return (AbstractNoticeStrategy) SpringUtil.getBean("weChatTemplateNoticeStrategy");
            case "stationMessage":
                return (AbstractNoticeStrategy) SpringUtil.getBean("stationMessageNoticeStrategy");
            case "emailMessage":
                return (AbstractNoticeStrategy) SpringUtil.getBean("emailNoticeStrategy");
            case "smsMessage":
                return (AbstractNoticeStrategy) SpringUtil.getBean("smsMessageNoticeStrategy");
            default:
                throw new ParameterIllegalException();
        }
    }

}
