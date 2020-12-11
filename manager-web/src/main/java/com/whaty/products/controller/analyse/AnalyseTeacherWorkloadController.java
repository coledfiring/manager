package com.whaty.products.controller.analyse;

import com.whaty.core.framework.bean.GridBasicConfig;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 教师工作量统计
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/teacherWorkloadAnalyse")
public class AnalyseTeacherWorkloadController extends TycjGridBaseControllerAdapter {

    @Resource(name = "analyseTeacherWorkloadService")
    private GridService analyseTeacherWorkloadService;

    @Override
    public GridService getGridService() {
        return this.analyseTeacherWorkloadService;
    }
}
