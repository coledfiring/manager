package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeTeacher;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.AnalyseTeacherInfoServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 教师信息统计
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/teacherInfoAnalyse")
public class AnalyseTeacherInfoController extends TycjGridBaseControllerAdapter<PeTeacher> {
    @Resource(name = "analyseTeacherInfoService")
    private AnalyseTeacherInfoServiceImpl analyseTeacherInfoService;

    @Override
    public GridService<PeTeacher> getGridService() {
        return this.analyseTeacherInfoService;
    }
}
