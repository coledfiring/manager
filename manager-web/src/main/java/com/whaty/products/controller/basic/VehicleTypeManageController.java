package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 车辆类型管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/vehicleTypeManage")
public class VehicleTypeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "vehicleTypeManageService")
    private GridService vehicleTypeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.vehicleTypeManageService;
    }
}
