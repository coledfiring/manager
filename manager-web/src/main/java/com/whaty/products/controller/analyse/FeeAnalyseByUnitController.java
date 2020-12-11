package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.FeeAnalyseByUnitServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 按单位汇总费用
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/feeAnalyseByUnit")
public class FeeAnalyseByUnitController extends TycjGridBaseControllerAdapter {

    @Resource(name = "feeAnalyseByUnitService")
    private FeeAnalyseByUnitServiceImpl feeAnalyseByUnitService;

    @Override
    public GridService getGridService() {
        return this.feeAnalyseByUnitService;
    }
}
