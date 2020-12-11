package com.whaty.products.controller.evaluate;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.evaluate.OverallEvaluateOption;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.evaluate.OverallEvaluateOptionServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 综合评价选项管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/evaluate/overallEvaluateOption")
public class OverallEvaluateOptionController extends TycjGridBaseControllerAdapter<OverallEvaluateOption> {
    @Resource(name = "overallEvaluateOptionService")
    private OverallEvaluateOptionServiceImpl overallEvaluateOptionService;

    @Override
    public GridService<OverallEvaluateOption> getGridService() {
        return this.overallEvaluateOptionService;
    }
}
