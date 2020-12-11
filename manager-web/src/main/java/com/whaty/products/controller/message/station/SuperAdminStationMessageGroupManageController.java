package com.whaty.products.controller.message.station;

import com.whaty.domain.bean.message.StationMessageGroup;
import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.products.service.message.impl.SuperAdminStationMessageGroupManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 超管站内信组管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController("superAdminStationMessageGroupManageController")
@RequestMapping("/superAdmin/message/superAdminStationMessageGroupManage")
public class SuperAdminStationMessageGroupManageController extends GridBaseController<StationMessageGroup> {

    @Resource(name = "superAdminStationMessageGroupManageService")
    private SuperAdminStationMessageGroupManageServiceImpl superAdminStationMessageGroupManageService;

    @Override
    public GridService<StationMessageGroup> getGridService() {
        return this.superAdminStationMessageGroupManageService;
    }

}
