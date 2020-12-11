package com.whaty.products.controller.hbgr.plan;

import com.whaty.domain.bean.hbgr.plan.PePlan;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Lazy
@RestController
@RequestMapping("/entity/energy/pePlanManage")
public class PePlanManageController extends TycjGridBaseControllerAdapter<PePlan> {
}