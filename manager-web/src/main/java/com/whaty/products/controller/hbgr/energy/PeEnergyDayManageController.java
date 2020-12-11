package com.whaty.products.controller.hbgr.energy;


import com.whaty.domain.bean.hbgr.energy.PeEnergyDay;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Lazy
@RestController
@RequestMapping("/entity/energy/peEnergyDayManage")
public class PeEnergyDayManageController extends TycjGridBaseControllerAdapter<PeEnergyDay> {
}
