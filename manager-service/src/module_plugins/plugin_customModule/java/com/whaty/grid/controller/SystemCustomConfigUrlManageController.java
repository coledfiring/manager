package com.whaty.grid.controller;

import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.custom.bean.SystemCustomConfigUrl;
import com.whaty.grid.service.impl.SystemCustomConfigUrlManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 定制配置与url关联管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/custom/systemCustomConfigUrlManage")
public class SystemCustomConfigUrlManageController extends GridBaseController<SystemCustomConfigUrl> {

    @Resource(name = "systemCustomConfigUrlManageService")
    private SystemCustomConfigUrlManageServiceImpl systemCustomConfigUrlManageService;

    @Override
    public GridService<SystemCustomConfigUrl> getGridService() {
        return this.systemCustomConfigUrlManageService;
    }
}
