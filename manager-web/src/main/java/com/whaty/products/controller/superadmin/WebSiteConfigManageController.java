package com.whaty.products.controller.superadmin;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeWebSiteConfig;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.superadmin.impl.WebSiteConfigManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 站点配置管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/webSiteConfigManage")
public class WebSiteConfigManageController extends TycjGridBaseControllerAdapter<PeWebSiteConfig> {

    @Resource(name = "webSiteConfigManageService")
    private WebSiteConfigManageServiceImpl webSiteConfigManageService;

    @Override
    public GridService<PeWebSiteConfig> getGridService() {
        return this.webSiteConfigManageService;
    }
}
