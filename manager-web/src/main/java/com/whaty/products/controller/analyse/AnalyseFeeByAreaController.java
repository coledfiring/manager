package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.AnalyseLimitTimeServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 按地区统计费用
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/analyseFeeByArea")
public class AnalyseFeeByAreaController extends TycjGridBaseControllerAdapter {

    @Resource(name = "analyseLimitTimeService")
    private AnalyseLimitTimeServiceImpl analyseLimitTimeService;

    @Override
    public GridService getGridService() {
        return this.analyseLimitTimeService;
    }
}
