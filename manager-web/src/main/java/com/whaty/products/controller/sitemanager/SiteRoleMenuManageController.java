package com.whaty.products.controller.sitemanager;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.api.exception.ApiException;
import com.whaty.core.framework.bean.CorePePriRole;
import com.whaty.core.framework.service.siteManager.ManagerControlDataOperateService;
import com.whaty.core.framework.service.siteManager.constant.SiteManagerConst;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.products.service.siteManager.SiteRoleMenuManegeService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 用户菜单权限管理controller
 * @author suoqiangqiang
 */
@Lazy
@RequestMapping("/entity/siteManager/siteRoleMenuManage")
@RestController("siteRoleMenuManageController")
public class SiteRoleMenuManageController {

    @Resource(name = "managerControlDataOperateService")
    private ManagerControlDataOperateService managerControlDataOperateService;

    @Resource(name = "siteRoleMenuManegeService")
    private SiteRoleMenuManegeService siteRoleMenuManegeService;

    /**
     * 获取当前站点id
     * @return
     */
    @ResponseBody
    @RequestMapping("/getWebSiteId")
    @OperateRecord(value = "获取当前站点",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel getWebSiteId() throws ApiException {
        Object webSite = SiteUtil.getSiteId();
        if (!this.siteRoleMenuManegeService.checkIsHavePermission()) {
            return ResultDataModel.handleFailureResult(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        return ResultDataModel.handleSuccessResult(webSite);
    }

    /**
     * 获取当前站点所有角色
     * @param paramsData
     * @return
     */
    @ResponseBody
    @RequestMapping({"/getAllRoles"})
    @OperateRecord(value = "获取当前站点所有角色",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel getAllRoles(@RequestBody ParamsDataModel paramsData) throws ApiException {
        List<CorePePriRole> roleList = this.siteRoleMenuManegeService.getAllRole(this.getPeWebSiteId(paramsData));
        if (!this.siteRoleMenuManegeService.checkIsHavePermission()) {
            return ResultDataModel.handleFailureResult(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        return ResultDataModel.handleSuccessResult(roleList);
    }

    /**
     * 获取当前站点菜单树
     * @param paramsData
     * @return
     */
    @ResponseBody
    @RequestMapping({"/treeWithSubMenu"})
    @OperateRecord(value = "获取当前站点菜单列表",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel menuTreeWithSubMenu(@RequestBody ParamsDataModel paramsData) throws ApiException {
        List nodeList = this.managerControlDataOperateService
                .getSiteMenuTreeWithSubMenu(this.getPeWebSiteId(paramsData));
        if (!this.siteRoleMenuManegeService.checkIsHavePermission()) {
            return ResultDataModel.handleFailureResult(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        return ResultDataModel.handleSuccessResult(nodeList);
    }

    /**
     * 获取当前角色菜单
     * @param paramsData
     * @return
     */
    @ResponseBody
    @RequestMapping({"/getRoleMenuIds"})
    @OperateRecord(value = "获取当前角色菜单",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel getRoleMenuIds(@RequestBody ParamsDataModel paramsData) throws ApiException{
        String siteId = this.getPeWebSiteId(paramsData);
        String roleId = paramsData.getStringParameterEmptyToNull("roleId");
        List menuIds = this.managerControlDataOperateService.getRoleMenuIds(siteId, roleId);
        if (!this.siteRoleMenuManegeService.checkIsHavePermission()) {
            return ResultDataModel.handleFailureResult(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        return ResultDataModel.handleSuccessResult(menuIds);
    }

    /**
     * 获取当前角色菜单权限
     * @param paramsData
     * @return
     */
    @ResponseBody
    @RequestMapping({"/getRoleGridMenuIds"})
    @OperateRecord(value = "获取当前角色菜单权限",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel getRoleGridMenuIds(@RequestBody ParamsDataModel paramsData) throws ApiException{
        String siteId = this.getPeWebSiteId(paramsData);
        String roleId = paramsData.getStringParameterEmptyToNull("roleId");
        List menuIds = this.managerControlDataOperateService.getRoleGridMenuIds(siteId, roleId);
        if (!this.siteRoleMenuManegeService.checkIsHavePermission()) {
            return ResultDataModel.handleFailureResult(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        return ResultDataModel.handleSuccessResult(menuIds);
    }

    /**
     * 保存当前角色菜单
     * @param paramsData
     * @return
     */
    @ResponseBody
    @RequestMapping({"/saveRoleMenuIds"})
    @OperateRecord(value = "保存当前角色菜单",
            moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
    public ResultDataModel saveRoleMenuIds(@RequestBody ParamsDataModel paramsData) throws ApiException {
        String siteId = this.getPeWebSiteId(paramsData);
        String roleId = paramsData.getStringParameterEmptyToNull("roleId");
        List menuIdList = (List) paramsData.getParameter("menuIds");
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<Integer> gridMenuIdList = (List<Integer>) paramsData.getParameter("gridMenuIds");
        this.managerControlDataOperateService.saveRoleMenuIds(siteId, roleId, menuIdList, gridMenuIdList);
        if (!this.siteRoleMenuManegeService.checkIsHavePermission()) {
            return ResultDataModel.handleFailureResult(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 从参数中提取站点id
     * @param paramsData
     * @return
     */
    private String getPeWebSiteId(ParamsDataModel paramsData) {
        return paramsData.getStringParameterEmptyToNull("webSiteId");
    }
}
