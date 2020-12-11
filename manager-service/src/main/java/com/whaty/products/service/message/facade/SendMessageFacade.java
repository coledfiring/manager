package com.whaty.products.service.message.facade;

import com.whaty.constant.CommonConstant;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.products.service.message.MessageNoticeService;
import com.whaty.products.service.message.constant.MessageConstants;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送消息外观
 *
 * @author weipengsen
 */
public class SendMessageFacade {

    private MessageNoticeService messageNoticeService;

    public SendMessageFacade() {
        this.messageNoticeService = (MessageNoticeService) SpringUtil.getBean("messageNoticeServiceImpl");
    }

    /**
     * 推送微信模板消息
     * @param code
     * @param ids
     * @param templateData
     * @param extraParams
     * @throws Exception
     */
    public void noticeWeChatTemplate(String code, String ids, Map<String, String> templateData,
                                     Map<String, Object> extraParams) throws Exception {
        Map<String, Object> params = new HashMap<>(4);
        params.put(CommonConstant.PARAM_IDS, ids);
        params.put(MessageConstants.PARAM_MESSAGE_TYPE, "weChatTemplate");
        params.put(MessageConstants.PARAM_TEMPLATE_CODE, code);
        if (MapUtils.isNotEmpty(templateData)) {
            params.put("templateData", templateData);
        }
        if (MapUtils.isNotEmpty(extraParams)) {
            params.putAll(extraParams);
        }
        this.messageNoticeService.notice(params);
    }
}
