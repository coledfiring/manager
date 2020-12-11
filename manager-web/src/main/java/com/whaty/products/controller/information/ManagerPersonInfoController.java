package com.whaty.products.controller.information;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.products.service.information.ManagerPersonInfoService;
import com.whaty.products.service.information.constant.InformationConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 管理员用户信息controller
 * @author weipengsen
 */
@Lazy
@RestController("managerPersonInfoController")
@RequestMapping("/entity/information/managerPersonInfo")
public class ManagerPersonInfoController {

    @Resource(name = "managerPersonInfoService")
    private ManagerPersonInfoService managerPersonInfoService;

    /**
     * 获取个人信息
     * @return
     */
    @RequestMapping(value = "/personInfo", method = RequestMethod.GET)
    @OperateRecord(value = "获取个人信息",
            moduleCode = OperateRecordModuleConstant.PLATFORM_MODULE_CODE)
    public ResultDataModel getPersonInfo() {
        Map<String, Object> personInfo = this.managerPersonInfoService.getPersonInfo();
        return ResultDataModel.handleSuccessResult(personInfo);
    }

    /**
     * 更新个人信息
     * @return
     */
    @RequestMapping(value = "/personInfo", method = RequestMethod.POST)
    @OperateRecord(value = "更新个人信息",
            moduleCode = OperateRecordModuleConstant.PLATFORM_MODULE_CODE, isImportant = true)
    public ResultDataModel updatePersonInfo(@RequestBody ParamsDataModel paramsDataModel) {
        Map<String, Object> personInfo = (Map<String, Object>) paramsDataModel
                .getParameter(InformationConstant.PARAM_PERSON_INFO);
        this.managerPersonInfoService.updatePersonInfo(personInfo);
        return ResultDataModel.handleSuccessResult("更新成功");
    }

    /**
     * 解绑微信
     * @return
     */
    @RequestMapping(value = "/unbindWeChat", method = RequestMethod.POST)
    @OperateRecord(value = "解绑微信",
            moduleCode = OperateRecordModuleConstant.PLATFORM_MODULE_CODE, isImportant = true)
    public ResultDataModel unbindWeChat() {
        this.managerPersonInfoService.doUnbindWeChat();
        return ResultDataModel.handleSuccessResult("微信解绑成功");
    }

    /**
     * 修改密码
     * @param paramsDataModel
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @OperateRecord(value = "修改密码",
            moduleCode = OperateRecordModuleConstant.PLATFORM_MODULE_CODE, isImportant = true)
    public ResultDataModel updatePassword(@RequestBody ParamsDataModel paramsDataModel) {
        String oldPassword = paramsDataModel.getStringParameter(InformationConstant.PARAM_OLD_PASSWORD);
        String newPassword = paramsDataModel.getStringParameter(InformationConstant.PARAM_NEW_PASSWORD);
        this.managerPersonInfoService.updatePassword(oldPassword, newPassword);
        return ResultDataModel.handleSuccessResult("修改密码成功");
    }

    /**
     * 获取单位以及单位管理员信息
     * @return
     */
    @RequestMapping(value = "/getBusinessUnitInfo", method = RequestMethod.GET)
    @OperateRecord(value = "获取单位以及单位管理员信息",
            moduleCode = OperateRecordModuleConstant.PLATFORM_MODULE_CODE)
    public ResultDataModel getBusinessUnitInfo() {
        Map<String, Object> businessUnitInfo = this.managerPersonInfoService.getBusinessUnitInfo();
        return ResultDataModel.handleSuccessResult(businessUnitInfo);
    }


    /**
     * 更新单位以及单位管理员信息
     * @return
     */
    @RequestMapping(value = "/updateBusinessUnitInfo", method = RequestMethod.POST)
    @OperateRecord(value = "更新个人信息",
            moduleCode = OperateRecordModuleConstant.PLATFORM_MODULE_CODE, isImportant = true)
    public ResultDataModel updateBusinessUnitInfo(@RequestBody ParamsDataModel paramsDataModel) {
        Map<String, Object> businessUnitInfo = (Map<String, Object>) paramsDataModel.
                getParameter(InformationConstant.PARAM_BUSINESS_UNIT_INFO);
        this.managerPersonInfoService.updateBusinessUnitInfo(businessUnitInfo);
        return ResultDataModel.handleSuccessResult("更新成功");
    }

}
