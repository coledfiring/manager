package com.whaty.products.controller.sitemanager;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.bean.CorePePriRole;
import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.core.framework.service.siteManager.impl.SuperAdminSiteRoleManageServiceImpl;
import com.whaty.framework.exception.ServiceException;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 超管端站点角色管理controller
 * @author suoqiangqiang
 */
@Lazy
@RestController("superAdminSiteRoleManageController")
@RequestMapping("/superAdmin/siteSuperManager/superAdminSiteRoleManage")
public class SuperAdminSiteRoleManageController extends GridBaseController<CorePePriRole> {

    @Resource(name = "superAdminSiteRoleManageService")
    private SuperAdminSiteRoleManageServiceImpl superAdminSiteRoleManageService;

    /**
     * 设为站点超管
     * @param paramsDataModel
     * @return
     */
    @ResponseBody
    @RequestMapping("/setSiteSuperAdmin")
    public ResultDataModel setSiteSuperAdmin(@RequestBody ParamsDataModel paramsDataModel) throws ServiceException {
        String ids = this.getIds(paramsDataModel);
        this.superAdminSiteRoleManageService.setSiteSuperAdmin(ids);
        return ResultDataModel.handleSuccessResult("操作成功");
    }

    /**
     * 设为普通管理员
     * @param paramsDataModel
     * @return
     */
    @ResponseBody
    @RequestMapping("/setOrdinaryAdmin")
    public ResultDataModel setOrdinaryAdmin(@RequestBody ParamsDataModel paramsDataModel) throws ServiceException {
        String ids = this.getIds(paramsDataModel);
        this.superAdminSiteRoleManageService.setOrdinaryAdmin(ids);
        return ResultDataModel.handleSuccessResult("操作成功");
    }
    @Override
    public GridService<CorePePriRole> getGridService() {
        return this.superAdminSiteRoleManageService;
    }
}
