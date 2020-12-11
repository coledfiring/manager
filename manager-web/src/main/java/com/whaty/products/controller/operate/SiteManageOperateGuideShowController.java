package com.whaty.products.controller.operate;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.operate.impl.SiteManageOperateGuideShowServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 站点管理端，查看操作日志记录controller
 * @author weipengsen
 */
@Lazy
@RestController("siteManageOperateGuideShowController")
@RequestMapping("/siteManage/operateRecord/siteManageOperateGuideShow")
public class SiteManageOperateGuideShowController extends TycjGridBaseControllerAdapter {

    @Resource(name = "siteManageOperateGuideShowService")
    private SiteManageOperateGuideShowServiceImpl siteManageOperateGuideShowService;

    @Override
    public GridService getGridService() {
        return this.siteManageOperateGuideShowService;
    }
}
