package com.whaty.grid.controller;

import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.custom.bean.SystemSiteCustomConfig;
import com.whaty.grid.service.impl.SystemSiteCustomConfigManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 定制配置与站点关联管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/custom/systemSiteCustomConfigManage")
public class SystemSiteCustomConfigManageController extends GridBaseController<SystemSiteCustomConfig> {

    @Resource(name = "systemSiteCustomConfigManageService")
    private SystemSiteCustomConfigManageServiceImpl systemSiteCustomConfigManageService;

    @Override
    public GridService<SystemSiteCustomConfig> getGridService() {
        return this.systemSiteCustomConfigManageService;
    }
}
