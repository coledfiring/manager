package com.whaty.products.controller.vehicle;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.vehicle.VehicleApplication;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.vehicle.impl.VehicleApplicationManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 用车申请管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/vehicle/vehicleApplicationManage")
@BasicOperateRecord(value = "用车申请管理")
public class VehicleApplicationManageController extends TycjGridBaseControllerAdapter<VehicleApplication> {

    @Resource(name = "vehicleApplicationManageService")
    private VehicleApplicationManageServiceImpl vehicleApplicationManageService;

    @Override
    public GridService<VehicleApplication> getGridService() {
        return this.vehicleApplicationManageService;
    }

    /**
     * 获取车牌号列表
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/cascadePlateNums")
    public ResultDataModel getCascadePlateNums(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult(this.vehicleApplicationManageService
                .getCascadePlateNums(paramsDataModel.getParams()));
    }


    /**
     * 结账
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/settleAccounts")
    public ResultDataModel doSettleAccounts(@RequestBody ParamsDataModel paramsDataModel) {
        this.vehicleApplicationManageService.doSettleAccounts(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("操作成功");
    }


    /**
     * 使用登记
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/useRegistration")
    public ResultDataModel doUseRegistration(@RequestBody ParamsDataModel paramsDataModel) {
        this.vehicleApplicationManageService
                .doUseRegistration(this.getIds(paramsDataModel), paramsDataModel.getParams());
        return ResultDataModel.handleSuccessResult("操作成功");
    }

    /**
     * 车辆安排
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/vehicleArrange")
    public ResultDataModel doVehicleArrange(@RequestBody ParamsDataModel paramsDataModel) {
        this.vehicleApplicationManageService
                .doVehicleArrange(this.getIds(paramsDataModel), paramsDataModel.getParams());
        return ResultDataModel.handleSuccessResult("操作成功");
    }

}
