package com.whaty.products.controller.message.send;

import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.domain.bean.message.SendMessageGroup;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.impl.SendManageGroupManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发送消息组管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/message/sendMessageGroupManage")
@BasicOperateRecord(value = "发送消息组",
        moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
public class SendMessageGroupManageController extends TycjGridBaseControllerAdapter<SendMessageGroup> {

    @Resource(name = "sendManageGroupManageService")
    private SendManageGroupManageServiceImpl sendManageGroupManageService;

    @Override
    public GridService<SendMessageGroup> getGridService() {
        return this.sendManageGroupManageService;
    }
}
