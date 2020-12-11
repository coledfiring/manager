package com.whaty.products.service.wechat;

import java.util.Map;

/**
 * @author weipengsen
 */
public interface ApiWeChatPublicService {

    void doSendTemplateMsg (Map<String, String> requestMap, String domain, String ip);
}
