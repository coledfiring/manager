package com.whaty.products.controller.message.station;

import com.whaty.domain.bean.message.StationMessageGroupSign;
import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.products.service.message.impl.SuperAdminStationMessageSignManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 超管站内信占位符管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("superAdminStationMessageSignManageController")
@RequestMapping("/superAdmin/message/superAdminStationMessageSignManage")
public class SuperAdminStationMessageSignManageController extends GridBaseController<StationMessageGroupSign> {

    @Resource(name = "superAdminStationMessageSignManageService")
    private SuperAdminStationMessageSignManageServiceImpl superAdminStationMessageSignManageService;

    @Override
    public GridService<StationMessageGroupSign> getGridService() {
        return this.superAdminStationMessageSignManageService;
    }

}
