package com.whaty.products.service.message;

import java.util.Map;

/**
 * 微信模板消息服务类
 *
 * @author weipengsen
 */
public interface WeChatTemplateNoticeService {

    /**
     * 获取模板配置
     * @param templateCode
     * @return
     */
    Map<String, Object> getTemplateConfig(String templateCode);
}
