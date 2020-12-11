package com.whaty.products.controller.message.send;

import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.grid.base.controller.TycjBaseControllerOperateSupport;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.products.service.message.domain.SendMessageParameter;
import com.whaty.products.service.message.impl.SendAllMessageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发送所有类型消息controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/message/sendAllMessage")
public class SendAllMessageController extends TycjBaseControllerOperateSupport {

    @Resource(name = "sendAllMessageService")
    private SendAllMessageServiceImpl sendAllMessageService;

    /**
     * 获取发送消息配置
     * @param code
     * @return
     */
    @RequestMapping("/messageConfig/{code}")
    @OperateRecord(value = "获取发送消息配置",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
    public ResultDataModel getMessageConfig(@PathVariable(MessageConstants.PARAM_TEMPLATE_CODE) String code) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return ResultDataModel.handleSuccessResult(this.sendAllMessageService.getMessageConfig(code));
    }

    /**
     * 发送消息
     * @return
     */
    @RequestMapping("/sendMessage")
    @OperateRecord(value = "发送消息",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
    public ResultDataModel sendMessage(@RequestBody SendMessageParameter sendMessageParameter) throws Exception {
        this.sendAllMessageService.sendMessage(sendMessageParameter);
        return ResultDataModel.handleSuccessResult("设置成功");
    }

}
