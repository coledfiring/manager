package com.whaty.analyse.mvc.controller;

import com.whaty.analyse.framework.domain.AnalyseParam;
import com.whaty.analyse.mvc.service.impl.AnalyseConfigServiceImpl;
import com.whaty.constant.SiteConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 统计配置
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/analyseConfig")
public class AnalyseConfigController extends TycjGridBaseControllerAdapter {

    @Resource(name = "analyseConfigService")
    private AnalyseConfigServiceImpl analyseConfigService;

    /**
     * 获取统计数据
     *
     * @param param
     * @return
     */
    @PostMapping("/analyseData")
    public ResultDataModel getAnalyseData(@RequestBody AnalyseParam param) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return ResultDataModel.handleSuccessResult(this.analyseConfigService.getAnalyseData(param));
    }

    @Override
    public GridService getGridService() {
        return this.analyseConfigService;
    }
}
