package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.AnalyseLimitTimeServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 项目分派统计
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/analyseItemApportion")
public class AnalyseItemApportionController extends TycjGridBaseControllerAdapter {

    @Resource(name = "analyseLimitTimeService")
    private AnalyseLimitTimeServiceImpl analyseLimitTimeService;

    @Override
    public GridService getGridService() {
        return this.analyseLimitTimeService;
    }
}
