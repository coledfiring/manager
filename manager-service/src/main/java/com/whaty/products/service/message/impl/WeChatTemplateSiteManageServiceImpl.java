package com.whaty.products.service.message.impl;

import com.whaty.domain.bean.message.WeChatTemplateMessageSite;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 微信模板消息站点组关联管理
 *
 * @author weipengsen
 */
@Lazy
@Service("weChatTemplateSiteManageService")
public class WeChatTemplateSiteManageServiceImpl extends TycjGridServiceAdapter<WeChatTemplateMessageSite> {
}
