package com.whaty.products.service.message.impl;

import com.whaty.domain.bean.message.WeChatTemplateMessageColumn;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 微信消息模板字段管理
 *
 * @author weipengsen
 */
@Lazy
@Service("weChatTemplateColumnManageService")
public class WeChatTemplateColumnManageServiceImpl extends TycjGridServiceAdapter<WeChatTemplateMessageColumn> {
}
