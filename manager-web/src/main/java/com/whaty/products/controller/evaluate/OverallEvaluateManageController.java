package com.whaty.products.controller.evaluate;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.evaluate.OverallEvaluate;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.evaluate.OverallEvaluateManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 班级综合评价管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/evaluate/overallEvaluateManage")
public class OverallEvaluateManageController extends TycjGridBaseControllerAdapter<OverallEvaluate> {

    @Resource(name = "overallEvaluateManageService")
    private OverallEvaluateManageServiceImpl overallEvaluateManageService;

    @Override
    public GridService<OverallEvaluate> getGridService() {
        return this.overallEvaluateManageService;
    }
}
