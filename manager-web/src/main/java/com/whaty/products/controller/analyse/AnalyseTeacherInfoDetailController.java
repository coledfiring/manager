package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.AnalyseTeacherInfoDetailServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 教师评估统计详情
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/teacherInfoAnalyseDetail")
public class AnalyseTeacherInfoDetailController extends TycjGridBaseControllerAdapter {
    @Resource(name = "analyseTeacherInfoDetailService")
    private AnalyseTeacherInfoDetailServiceImpl analyseTeacherInfoDetailService;

    @Override
    public GridService getGridService() {
        return this.analyseTeacherInfoDetailService;
    }
}
