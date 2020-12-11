package com.whaty.products.controller.message;

import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.MessageNoticeService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 信息通知controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("messageNoticeController")
@RequestMapping("/entity/message/messageNotice")
public class MessageNoticeController extends TycjGridBaseControllerAdapter {

    @Resource(name = "messageNoticeServiceImpl")
    private MessageNoticeService messageNoticeService;

    /**
     * 信息通知
     * @return
     */
    @RequestMapping("/notice")
    @OperateRecord(value = "发送信息通知",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
    public ResultDataModel notice(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        this.messageNoticeService.notice(this.getParamsDataModelMap(paramsDataModel));
        return ResultDataModel.handleSuccessResult("发送成功");
    }

}
