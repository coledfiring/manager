package com.whaty.products.controller.message.wechat;

import com.whaty.domain.bean.message.WeChatTemplateMessageGroup;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.impl.WeChatTemplateGroupManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信模板消息组管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/message/weChatTemplateGroupManage")
public class WeChatTemplateGroupManageController extends TycjGridBaseControllerAdapter<WeChatTemplateMessageGroup> {

    @Resource(name = "weChatTemplateGroupManageService")
    private WeChatTemplateGroupManageServiceImpl weChatTemplateGroupManageService;

    @Override
    public GridService<WeChatTemplateMessageGroup> getGridService() {
        return this.weChatTemplateGroupManageService;
    }
}
