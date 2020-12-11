package com.whaty.products.controller.message.wechat;

import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.message.WeChatGroupNoticeService;
import com.whaty.products.service.message.constant.MessageConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 微信群发通知controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("weChatGroupNoticeController")
@RequestMapping("/entity/message/weChatGroupNotice")
public class WeChatGroupNoticeController {

    @Resource(name = "weChatGroupNoticeServiceImpl")
    private WeChatGroupNoticeService weChatGroupNoticeService;

    /**
     * 列举模板
     * @return
     */
    @RequestMapping("/listTemplates")
    @OperateRecord(value = "列举微信群发通知模板",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE)
    public ResultDataModel listTemplates() {
        List<Map<String, Object>> templates = this.weChatGroupNoticeService.listTemplates();
        return ResultDataModel.handleSuccessResult(templates);
    }

    /**
     * 保存模板
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/saveTemplate")
    @OperateRecord(value = "保存微信群发通知模板",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
    public ResultDataModel saveTemplate(@RequestBody ParamsDataModel paramsDataModel) {
        String name = paramsDataModel.getStringParameter(MessageConstants.PARAM_NAME);
        String content = paramsDataModel.getStringParameter(MessageConstants.PARAM_CONTENT);
        this.weChatGroupNoticeService.saveTemplate(name, content);
        return ResultDataModel.handleSuccessResult("保存成功");
    }

    /**
     * 修改模板
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/updateTemplate")
    @OperateRecord(value = "修改微信群发通知模板",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
    public ResultDataModel updateTemplate(@RequestBody ParamsDataModel paramsDataModel) {
        String id = paramsDataModel.getStringParameter(MessageConstants.PARAM_ID);
        String name = paramsDataModel.getStringParameter(MessageConstants.PARAM_NAME);
        String content = paramsDataModel.getStringParameter(MessageConstants.PARAM_CONTENT);
        this.weChatGroupNoticeService.updateTemplate(id, name, content);
        return ResultDataModel.handleSuccessResult("修改成功");
    }

    /**
     * 删除模板
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/deleteTemplate")
    @OperateRecord(value = "删除微信群发通知模板",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
    public ResultDataModel deleteTemplate(@RequestBody ParamsDataModel paramsDataModel) {
        String id = paramsDataModel.getStringParameter(MessageConstants.PARAM_ID);
        this.weChatGroupNoticeService.deleteTemplate(id);
        return ResultDataModel.handleSuccessResult("删除成功");
    }

}
