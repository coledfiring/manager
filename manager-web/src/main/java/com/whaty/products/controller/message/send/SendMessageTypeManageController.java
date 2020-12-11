package com.whaty.products.controller.message.send;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.message.SendMessageType;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.impl.SendMessageTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发送消息类型管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/message/sendMessageTypeManage")
@BasicOperateRecord(value = "发送消息类型",
        moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
public class SendMessageTypeManageController extends TycjGridBaseControllerAdapter<SendMessageType> {

    @Resource(name = "sendMessageTypeManageService")
    private SendMessageTypeManageServiceImpl sendMessageTypeManageService;

    /**
     * 添加消息到站点
     * @return
     */
    @RequestMapping("/addToSite")
    public ResultDataModel addToSite(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        String siteId = paramsDataModel.getStringParameter("siteId");
        this.sendMessageTypeManageService.addToSite(ids, siteId);
        return ResultDataModel.handleSuccessResult("添加成功");
    }

    @Override
    public GridService<SendMessageType> getGridService() {
        return this.sendMessageTypeManageService;
    }
}
