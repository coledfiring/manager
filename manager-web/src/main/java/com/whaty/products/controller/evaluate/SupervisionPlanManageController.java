package com.whaty.products.controller.evaluate;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.evaluate.SupervisionPlan;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 教学督导计划管理
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/evaluate/supervisionPlanManage")
public class SupervisionPlanManageController extends TycjGridBaseControllerAdapter<SupervisionPlan> {

}
