package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.AnalyseTeacherWorkloadDetailServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 教师工作量统计详情
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/teacherWorkloadAnalyseDetail")
public class AnalyseTeacherWorkloadDetailController extends TycjGridBaseControllerAdapter {

    @Resource(name = "analyseTeacherWorkloadDetailService")
    private AnalyseTeacherWorkloadDetailServiceImpl analyseTeacherWorkloadDetailService;

    @Override
    public GridService getGridService() {
        return this.analyseTeacherWorkloadDetailService;
    }
}
