package com.whaty.analyse.mvc.controller;

import com.whaty.analyse.framework.domain.bean.AnalyseBoardConfig;
import com.whaty.analyse.mvc.service.impl.AnalyseBoardConfigManageServiceImpl;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 统计看板配置管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/analyse/analyseBoardConfigManage")
public class AnalyseBoardConfigManageController extends TycjGridBaseControllerAdapter<AnalyseBoardConfig> {

    @Resource(name = "analyseBoardConfigManageService")
    private AnalyseBoardConfigManageServiceImpl analyseBoardConfigManageService;

    @Override
    public GridService<AnalyseBoardConfig> getGridService() {
        return this.analyseBoardConfigManageService;
    }
}
