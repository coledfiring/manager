package com.whaty.analyse.mvc.controller;

import com.whaty.analyse.mvc.service.AnalyseBoardService;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ResultDataModel;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 统计看板
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/analyseBoard")
public class AnalyseBoardController {

    @Resource(name = "analyseBoardService")
    private AnalyseBoardService analyseBoardService;

    /**
     * 获取看板
     * @param id
     * @return
     */
    @GetMapping("/board/{id}")
    public ResultDataModel getBoard(@PathVariable String id,
                                    @RequestParam(required = false) Map<String, Object> params) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return ResultDataModel.handleSuccessResult(this.analyseBoardService.getBoard(id, params));
    }

}
