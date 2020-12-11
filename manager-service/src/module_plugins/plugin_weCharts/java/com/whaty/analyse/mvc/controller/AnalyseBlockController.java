package com.whaty.analyse.mvc.controller;

import com.whaty.analyse.mvc.service.AnalyseBlockService;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ResultDataModel;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 统计块显示
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/analyseBlock")
public class AnalyseBlockController {

    @Resource(name = "analyseBlockService")
    private AnalyseBlockService analyseBlockService;

    /**
     * 获取块配置
     * @param id
     * @return
     */
    @GetMapping("/block/{id}")
    public ResultDataModel getBlock(@PathVariable String id) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return ResultDataModel.handleSuccessResult(this.analyseBlockService.getBlock(id));

    }


}
