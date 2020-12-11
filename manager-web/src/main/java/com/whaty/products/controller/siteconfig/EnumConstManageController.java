package com.whaty.products.controller.siteconfig;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.supergrid.controller.SuperAdminGridController;
import com.whaty.products.service.siteconfig.impl.EnumConstManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * enumConst管理controller
 * @author suoqiangqiang
 */
@Lazy
@RequestMapping("/superAdmin/siteConfig/enumConstManage")
@RestController("enumConstManageController")
public class EnumConstManageController extends SuperAdminGridController<EnumConst> {

    @Resource(name = "enumConstManageService")
    private EnumConstManageServiceImpl enumConstManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.enumConstManageService;
    }
}
