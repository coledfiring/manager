package com.whaty.products.controller.superadmin;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.wechat.PeWeChatBasic;
import com.whaty.framework.grid.supergrid.controller.SuperAdminGridController;
import com.whaty.products.service.superadmin.impl.WeChatSiteBasicManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信站点基础信息管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController("weChatSiteBasicManageController")
@RequestMapping("/superAdmin/siteManage/weChatSiteBasicManage")
public class WeChatSiteBasicManageController extends SuperAdminGridController<PeWeChatBasic> {
    @Resource(name = "weChatSiteBasicManageService")
    private WeChatSiteBasicManageServiceImpl weChatSiteBasicManageService;

    @Override
    public GridService<PeWeChatBasic> getGridService() {
        return this.weChatSiteBasicManageService;
    }
}
