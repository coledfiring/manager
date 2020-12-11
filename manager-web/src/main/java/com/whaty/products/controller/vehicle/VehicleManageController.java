package com.whaty.products.controller.vehicle;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.vehicle.Vehicle;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.constant.TrainingConstant;
import com.whaty.products.service.vehicle.impl.VehicleManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 车辆管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/vehicle/vehicleManage")
@BasicOperateRecord(value = "车辆管理")
public class VehicleManageController extends TycjGridBaseControllerAdapter<Vehicle> {

    @Resource(name = "vehicleManageService")
    private VehicleManageServiceImpl vehicleManageService;

    @Override
    public GridService<Vehicle> getGridService() {
        return this.vehicleManageService;
    }


    /**
     * 更新车辆状态
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/updateVehicleStatus")
    public ResultDataModel updateVehicleStatus(@RequestBody ParamsDataModel paramsDataModel) {
        this.vehicleManageService.updateVehicleStatus(this.getIds(paramsDataModel), paramsDataModel
                .getStringParameter(TrainingConstant.PARAM_PRICE));
        return ResultDataModel.handleSuccessResult("操作成功");
    }

}
