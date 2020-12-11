package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.evaluate.OverallEvaluateOption;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.AnalyseClassEvaluateServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 班级评价统计
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/classEvaluateAnalyse")
public class AnalyseClassEvaluateController extends TycjGridBaseControllerAdapter<OverallEvaluateOption> {

    @Resource(name = "analyseClassEvaluateService")
    private AnalyseClassEvaluateServiceImpl analyseClassEvaluateService;

    @Override
    public GridService<OverallEvaluateOption> getGridService() {
        return this.analyseClassEvaluateService;
    }
}
