package com.whaty.products.controller.vehicle;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.vehicle.Motorcade;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.vehicle.impl.MotorcadeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 车队管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/vehicle/motorcadeManage")
@BasicOperateRecord(value = "车队管理")
public class MotorcadeManageController extends TycjGridBaseControllerAdapter<Motorcade> {

    @Resource(name = "motorcadeManageService")
    private MotorcadeManageServiceImpl motorcadeManageService;

    @Override
    public GridService<Motorcade> getGridService() {
        return this.motorcadeManageService;
    }

}
