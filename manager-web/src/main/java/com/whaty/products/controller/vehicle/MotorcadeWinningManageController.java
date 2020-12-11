package com.whaty.products.controller.vehicle;

import com.whaty.domain.bean.vehicle.Motorcade;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 车辆中标管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/vehicle/motorcadeWinningManage")
public class MotorcadeWinningManageController extends TycjGridBaseControllerAdapter<Motorcade> {

}
