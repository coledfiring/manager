package com.whaty.products.controller.message.wechat;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.message.WeChatTemplateMessageSite;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.impl.WeChatTemplateSiteManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信模板消息站点关联管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/message/weChatTemplateSiteManage")
public class WeChatTemplateSiteManageController extends TycjGridBaseControllerAdapter<WeChatTemplateMessageSite> {

    @Resource(name = "weChatTemplateSiteManageService")
    private WeChatTemplateSiteManageServiceImpl weChatTemplateSiteManageService;

    @Override
    public GridService<WeChatTemplateMessageSite> getGridService() {
        return this.weChatTemplateSiteManageService;
    }
}
