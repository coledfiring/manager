package com.whaty.products.controller.siteconfig;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.SystemVariables;
import com.whaty.framework.grid.supergrid.controller.SuperAdminGridController;
import com.whaty.products.service.siteconfig.impl.SystemVariablesManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * systemVariables管理controller
 * @author suoqiangqiang
 */
@Lazy
@RequestMapping("/superAdmin/siteConfig/systemVariablesManage")
@RestController("systemVariablesManageController")
public class SystemVariablesManageController extends SuperAdminGridController<SystemVariables> {
    @Resource(name = "systemVariablesManageService")
    private SystemVariablesManageServiceImpl systemVariablesManageService;

    @Override
    public GridService<SystemVariables> getGridService() {
        return this.systemVariablesManageService;
    }
}
