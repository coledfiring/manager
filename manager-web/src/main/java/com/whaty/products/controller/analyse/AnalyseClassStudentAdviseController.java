package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.evaluate.OverallEvaluateOption;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.AnalyseClassStudentAdviseServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 学员反馈详情
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/classStudentAdviseAnalyse")
public class AnalyseClassStudentAdviseController extends TycjGridBaseControllerAdapter<OverallEvaluateOption> {
    @Resource(name = "analyseClassStudentAdviseService")
    private AnalyseClassStudentAdviseServiceImpl analyseClassStudentAdviseService;

    @Override
    public GridService<OverallEvaluateOption> getGridService() {
        return this.analyseClassStudentAdviseService;
    }
}
