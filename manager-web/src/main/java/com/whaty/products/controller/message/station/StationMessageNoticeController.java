package com.whaty.products.controller.message.station;

import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.message.StationMessageNoticeService;
import com.whaty.products.service.message.constant.MessageConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户发送站内信的信息查询controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("stationMessageNoticeController")
@RequestMapping("/entity/message/stationMessageNotice")
public class StationMessageNoticeController extends TycjGridBaseControllerAdapter {

    @Resource(name = "stationMessageNoticeService")
    private StationMessageNoticeService stationMessageNoticeService;

    /**
     * 获取站内信配置信息
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/messageGroupInfo/{groupId}", method = RequestMethod.GET)
    @OperateRecord(value = "获取站内信配置信息",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE)
    public ResultDataModel getMessageGroupInfoById(@PathVariable(MessageConstants.PARAM_GROUP_ID) String groupId) {
        Map<String, Object> groupInfo = this.stationMessageNoticeService.getMessageGroupInfoById(groupId);
        return ResultDataModel.handleSuccessResult(groupInfo);
    }

    /**
     * 根据code获取站内信组
     * @param groupCode
     * @return
     */
    @RequestMapping(value = "/messageGroupInfo", method = RequestMethod.GET)
    @OperateRecord(value = "根据code获取站内信组",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE)
    public ResultDataModel getMessageGroupInfoByCode(@RequestParam(MessageConstants.PARAM_GROUP_CODE) String groupCode) {
        Map<String, Object> groupInfo = this.stationMessageNoticeService.getMessageGroupInfoByCode(groupCode);
        return ResultDataModel.handleSuccessResult(groupInfo);
    }

    /**
     * 列举信息组
     * @return
     */
    @RequestMapping(value = "/messageGroup", method = RequestMethod.GET)
    @OperateRecord(value = "列举站内信信息组",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE)
    public ResultDataModel listMessageGroup() {
        List<Map<String, Object>> messageGroups = this.stationMessageNoticeService.listMessageGroup();
        return ResultDataModel.handleSuccessResult(messageGroups);
    }

    /**
     * 保存模板
     * @return
     */
    @RequestMapping(value = "/messageTemplate", method = RequestMethod.POST)
    @OperateRecord(value = "保存站内信模板",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
    public ResultDataModel saveMessageTemplate(@RequestBody ParamsDataModel paramsDataModel) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        this.stationMessageNoticeService.saveMessageTemplate(this.getParamsDataModelMap(paramsDataModel));
        return ResultDataModel.handleSuccessResult("保存成功");
    }

    /**
     * 删除模板
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/deleteMessageTemplate", method = RequestMethod.POST)
    @OperateRecord(value = "删除站内信模板",
            moduleCode = OperateRecordModuleConstant.MESSAGE_MODULE_CODE, isImportant = true)
    public ResultDataModel deleteMessageTemplate(@RequestBody ParamsDataModel paramsDataModel) {
        String templateId = paramsDataModel.getStringParameter(MessageConstants.PARAM_TEMPLATE_CODE);
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        this.stationMessageNoticeService.deleteMessageTemplate(templateId);
        return ResultDataModel.handleSuccessResult("删除成功");
    }

}
