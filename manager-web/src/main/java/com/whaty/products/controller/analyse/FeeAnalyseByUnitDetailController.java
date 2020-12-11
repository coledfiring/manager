package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.analyse.impl.LimitTwoLevelServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 按培训单位汇总费用详情
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/feeAnalyseByUnitDetail")
public class FeeAnalyseByUnitDetailController extends TycjGridBaseControllerAdapter {

    @Resource(name = "limitTwoLevelService")
    private LimitTwoLevelServiceImpl limitTwoLevelService;

    @Override
    public GridService getGridService() {
        return this.limitTwoLevelService;
    }
}
