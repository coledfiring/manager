package com.whaty.analyse.mvc.controller;

import com.whaty.analyse.framework.domain.bean.AnalyseBlockConfig;
import com.whaty.analyse.mvc.service.impl.AnalyseBlockConfigManageServiceImpl;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 统计块配置管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/analyse/analyseBlockConfigManage")
public class AnalyseBlockConfigManageController extends TycjGridBaseControllerAdapter<AnalyseBlockConfig> {

    @Resource(name = "analyseBlockConfigManageService")
    private AnalyseBlockConfigManageServiceImpl analyseBlockConfigManageService;

    @Override
    public GridService<AnalyseBlockConfig> getGridService() {
        return this.analyseBlockConfigManageService;
    }
}
