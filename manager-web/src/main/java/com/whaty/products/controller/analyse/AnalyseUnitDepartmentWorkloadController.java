package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 单位部门工作量统计详情
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/unitDepartmentWorkloadAnalyse")
public class AnalyseUnitDepartmentWorkloadController extends TycjGridBaseControllerAdapter {

    @Resource(name = "analyseUnitDepartmentWorkloadService")
    private GridService analyseUnitDepartmentWorkloadService;

    @Override
    public GridService getGridService() {
        return this.analyseUnitDepartmentWorkloadService;
    }
}
