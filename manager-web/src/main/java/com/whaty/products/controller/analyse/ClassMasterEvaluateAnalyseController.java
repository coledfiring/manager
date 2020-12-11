package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeTeacher;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.ClassMasterEvaluateAnalyseServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 班主任开班情况统计
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/classMasterEvaluateAnalyse")
public class ClassMasterEvaluateAnalyseController extends TycjGridBaseControllerAdapter<PeTeacher> {
    @Resource(name = "classMasterEvaluateAnalyseService")
    private ClassMasterEvaluateAnalyseServiceImpl classMasterEvaluateAnalyseService;

    @Override
    public GridService<PeTeacher> getGridService() {
        return this.classMasterEvaluateAnalyseService;
    }
}