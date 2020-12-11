package com.whaty.products.controller.message.send;

import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.domain.bean.message.SendMessageSite;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.impl.SendMessageSiteManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发送消息站点关联管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/message/sendMessageSiteManage")
@BasicOperateRecord(value = "发送消息站点关联",
        moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
public class SendMessageSiteManageController extends TycjGridBaseControllerAdapter<SendMessageSite> {

    @Resource(name = "sendMessageSiteManageService")
    private SendMessageSiteManageServiceImpl sendMessageSiteManageService;

    @Override
    public GridService<SendMessageSite> getGridService() {
        return this.sendMessageSiteManageService;
    }
}
