package com.whaty.products.controller.superadmin;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.wechat.PeWeChatSite;
import com.whaty.framework.grid.supergrid.controller.SuperAdminGridController;
import com.whaty.products.service.superadmin.impl.WeChatSiteManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信站点管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController("weChatSiteManageController")
@RequestMapping("/superAdmin/siteManage/weChatSiteManage")
public class WeChatSiteManageController extends SuperAdminGridController<PeWeChatSite> {
    @Resource(name = "weChatSiteManageService")
    private WeChatSiteManageServiceImpl weChatSiteManageService;

    @Override
    public GridService<PeWeChatSite> getGridService() {
        return this.weChatSiteManageService;
    }
}
