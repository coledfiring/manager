package com.whaty.products.controller.hbgr.plan;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.hbgr.plan.PePlanDetail;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.hbgr.plan.PePlanDetailManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Lazy
@RestController
@RequestMapping("/entity/energy/pePlanDetailManage")
public class PePlanDetailManageController extends TycjGridBaseControllerAdapter<PePlanDetail> {

    @Resource(name = "pePlanDetailManageService")
    private PePlanDetailManageServiceImpl pePlanDetailManageService;

    @Override
    public GridService<PePlanDetail> getGridService() {
        return this.pePlanDetailManageService;
    }
}