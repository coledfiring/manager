package com.whaty.products.controller.analyse;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 单位人员工作量统计详情
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/unitPersonnelWorkloadAnalyse")
public class AnalyseUnitPersonnelWorkloadController extends TycjGridBaseControllerAdapter {

    @Resource(name = "analyseUnitPersonnelWorkloadService")
    private GridService analyseUnitPersonnelWorkloadService;

    @Override
    public GridService getGridService() {
        return this.analyseUnitPersonnelWorkloadService;
    }
}
