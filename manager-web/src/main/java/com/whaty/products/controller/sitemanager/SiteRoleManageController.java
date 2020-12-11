package com.whaty.products.controller.sitemanager;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PePriRole;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.siteManager.impl.SiteRoleManageImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.ROLE_BASIC_INFO;

/**
 * 用户角色管理controller
 * @author suoqiangqiang
 */
@Lazy
@RequestMapping("/entity/siteManager/siteRoleManage")
@RestController("siteRoleManageControllerOld")
@BasicOperateRecord(value = "用户角色管理",
        moduleCode = OperateRecordModuleConstant.SITE_MANAGER_MODULE_CODE, isImportant = true)
@SqlRecord(namespace = "pePriRole", sql = ROLE_BASIC_INFO)
public class SiteRoleManageController extends TycjGridBaseControllerAdapter<PePriRole> {

    @Resource(name = "siteRoleManageService")
    private SiteRoleManageImpl siteRoleManageService;

    @Override
    public GridService<PePriRole> getGridService() {
        return this.siteRoleManageService;
    }
}
