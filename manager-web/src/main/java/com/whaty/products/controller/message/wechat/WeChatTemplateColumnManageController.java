package com.whaty.products.controller.message.wechat;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.message.WeChatTemplateMessageColumn;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.impl.WeChatTemplateColumnManageServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信模板字段管理controller
 *
 * @author weipengsen
 */

@RestController("weChatTemplateColumnManage")
@RequestMapping("/superAdmin/message/weChatTemplateColumnManage")
public class WeChatTemplateColumnManageController extends TycjGridBaseControllerAdapter<WeChatTemplateMessageColumn> {

    @Resource(name = "weChatTemplateColumnManageService")
    private WeChatTemplateColumnManageServiceImpl weChatTemplateColumnManageService;

    @Override
    public GridService<WeChatTemplateMessageColumn> getGridService() {
        return this.weChatTemplateColumnManageService;
    }
}
