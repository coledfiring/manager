package com.whaty.products.controller.sitemanager;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.api.exception.ApiException;
import com.whaty.core.framework.service.siteManager.constant.SiteManagerConst;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.products.service.siteManager.SiteManagerPriorityManageService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员用户权限管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RequestMapping("/entity/siteManager/siteManagerPriorityManage")
@RestController("siteManagerPriorityManageController")
public class SiteManagerPriorityManageController {

    @Resource(name = "siteManagerPriorityManageService")
    private SiteManagerPriorityManageService siteManagerPriorityManageService;

    /**
     * 获取所有权限信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAllPriorityData")
    @OperateRecord(value = "获取所有权限信息",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel getAllPriorityData() throws ApiException {
        if (!this.siteManagerPriorityManageService.checkIsHavePermission()) {
            return ResultDataModel.handleFailureResult(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        return ResultDataModel.handleSuccessResult(siteManagerPriorityManageService.getAllPriorityData());
    }

    /**
     * 获取已勾选权限信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCheckedPriorityData")
    @OperateRecord(value = "获取已勾选权限信息",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel getCheckedPriorityData(@RequestBody ParamsDataModel paramsData) throws ApiException {
        if (!this.siteManagerPriorityManageService.checkIsHavePermission()) {
            return ResultDataModel.handleFailureResult(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        String managerId = paramsData.getStringParameterEmptyToNull("managerId");
        return ResultDataModel.handleSuccessResult(siteManagerPriorityManageService.getCheckedPriorityIdList(managerId));
    }

    /**
     * 获取已勾选权限信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUserName")
    @OperateRecord(value = "获取已勾选权限信息",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel getUserName(@RequestBody ParamsDataModel paramsData) throws ApiException {
        if (!this.siteManagerPriorityManageService.checkIsHavePermission()) {
            return ResultDataModel.handleFailureResult(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        String managerId = paramsData.getStringParameterEmptyToNull("managerId");
        return ResultDataModel.handleSuccessResult(siteManagerPriorityManageService.getUserName(managerId));
    }

    /**
     * 保存所有权限信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveAllPriorityData")
    @OperateRecord(value = "保存所有权限信息",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel saveAllPriorityData(@RequestBody ParamsDataModel paramsData) throws ApiException {
        String managerId = paramsData.getStringParameter("managerId");
        List<String> unitIds = (List<String>) paramsData.getParams().get("unitIds");
        if (!this.siteManagerPriorityManageService.checkIsHavePermission()) {
            return ResultDataModel.handleFailureResult(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        siteManagerPriorityManageService.saveAllPriorityData(managerId, unitIds);
        return ResultDataModel.handleSuccessResult();
    }
}
