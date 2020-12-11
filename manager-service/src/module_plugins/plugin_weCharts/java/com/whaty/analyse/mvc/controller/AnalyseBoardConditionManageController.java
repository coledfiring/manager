package com.whaty.analyse.mvc.controller;

import com.whaty.analyse.framework.domain.bean.AnalyseBoardCondition;
import com.whaty.analyse.mvc.service.impl.AnalyseBoardConditionManageServiceImpl;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 统计看板条件配置管理
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/analyse/analyseBoardConditionManage")
public class AnalyseBoardConditionManageController extends TycjGridBaseControllerAdapter<AnalyseBoardCondition> {

    @Resource(name = "analyseBoardConditionManageService")
    private AnalyseBoardConditionManageServiceImpl analyseBoardConditionManageService;

    @Override
    public GridService<AnalyseBoardCondition> getGridService() {
        return this.analyseBoardConditionManageService;
    }
}
