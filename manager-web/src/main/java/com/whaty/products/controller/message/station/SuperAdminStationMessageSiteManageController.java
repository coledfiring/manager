package com.whaty.products.controller.message.station;

import com.whaty.domain.bean.message.StationMessageGroupSite;
import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.products.service.message.impl.SuperAdminStationMessageSiteManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 站内信站点管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("superAdminStationMessageSiteManageController")
@RequestMapping("/superAdmin/message/superAdminStationMessageSiteManage")
public class SuperAdminStationMessageSiteManageController extends GridBaseController<StationMessageGroupSite> {

    @Resource(name = "superAdminStationMessageSiteManageService")
    private SuperAdminStationMessageSiteManageServiceImpl superAdminStationMessageSiteManageService;

    @Override
    public GridService<StationMessageGroupSite> getGridService() {
        return this.superAdminStationMessageSiteManageService;
    }

}
