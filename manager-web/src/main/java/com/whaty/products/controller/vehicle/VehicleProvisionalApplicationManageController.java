package com.whaty.products.controller.vehicle;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.vehicle.VehicleApplication;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.vehicle.impl.VehicleProvisionalApplicationManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 临时用车申请管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/vehicle/vehicleProvisionalApplicationManage")
@BasicOperateRecord(value = "临时用车申请管理")
public class VehicleProvisionalApplicationManageController extends TycjGridBaseControllerAdapter<VehicleApplication> {

    @Resource(name = "vehicleProvisionalApplicationManageService")
    private VehicleProvisionalApplicationManageServiceImpl vehicleProvisionalApplicationManageService;

    @Override
    public GridService<VehicleApplication> getGridService() {
        return this.vehicleProvisionalApplicationManageService;
    }

}
