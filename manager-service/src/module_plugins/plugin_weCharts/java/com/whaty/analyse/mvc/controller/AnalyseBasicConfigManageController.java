package com.whaty.analyse.mvc.controller;

import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.mvc.service.impl.AnalyseBasicConfigManageServiceImpl;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 超管的统计基础配置管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/analyse/analyseBasicConfigManage")
public class AnalyseBasicConfigManageController extends TycjGridBaseControllerAdapter<AnalyseBasicConfig> {

    @Resource(name = "analyseBasicConfigManageService")
    private AnalyseBasicConfigManageServiceImpl analyseBasicConfigManageService;

    @Override
    public GridService<AnalyseBasicConfig> getGridService() {
        return this.analyseBasicConfigManageService;
    }
}
