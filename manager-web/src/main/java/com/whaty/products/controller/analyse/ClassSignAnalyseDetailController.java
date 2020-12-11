package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeClass;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.ClassSignAnalyseDetailServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 班级签到统计详情
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/classSignAnalyseDetail")
public class ClassSignAnalyseDetailController extends TycjGridBaseControllerAdapter<PeClass> {
    @Resource(name = "classSignAnalyseDetailService")
    private ClassSignAnalyseDetailServiceImpl classSignAnalyseDetailService;

    @Override
    public GridService<PeClass> getGridService() {
        return this.classSignAnalyseDetailService;
    }
}
