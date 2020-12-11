package com.whaty.grid.controller;

import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.custom.bean.SystemCustomConfig;
import com.whaty.grid.service.impl.SystemCustomConfigManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 定制配置管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/custom/systemCustomConfigManage")
public class SystemCustomConfigManageController extends GridBaseController<SystemCustomConfig> {

    @Resource(name = "systemCustomConfigManageService")
    private SystemCustomConfigManageServiceImpl systemCustomConfigManageService;

    @Override
    public GridService<SystemCustomConfig> getGridService() {
        return this.systemCustomConfigManageService;
    }
}
