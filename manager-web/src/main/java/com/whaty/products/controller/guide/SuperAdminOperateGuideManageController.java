package com.whaty.products.controller.guide;

import com.whaty.domain.bean.OperateGuideDescription;
import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.core.framework.service.guide.impl.SuperAdminOperateGuideManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 超管操作指导描述管理controller
 * @author weipengsen
 */
@Lazy
@RestController("superAdminOperateGuideManageController")
@RequestMapping("/superAdmin/operateGuide/operateGuideManage")
public class SuperAdminOperateGuideManageController extends GridBaseController<OperateGuideDescription> {

    @Resource(name = "superAdminOperateGuideManageService")
    private SuperAdminOperateGuideManageServiceImpl superAdminOperateGuideManageService;

    @Override
    public GridService<OperateGuideDescription> getGridService() {
        return this.superAdminOperateGuideManageService;
    }

}
