package com.whaty.products.controller.hbgr.energy;


        import com.whaty.domain.bean.hbgr.energy.PeEnergyTotal;
        import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
        import org.springframework.context.annotation.Lazy;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;

@Lazy
@RestController
@RequestMapping("/entity/energy/peEnergyTotalManage")
public class PeEnergyTotalManageController extends TycjGridBaseControllerAdapter<PeEnergyTotal> {
}
