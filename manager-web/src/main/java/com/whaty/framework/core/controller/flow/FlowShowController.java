package com.whaty.framework.core.controller.flow;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.core.flow.service.FlowConfigService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 流程显示controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/flow/flowShow")
public class FlowShowController {

    @Resource(name = "flowConfigService")
    private FlowConfigService flowConfigService;

    @GetMapping("/flowConfig/{flowId}")
    public ResultDataModel getFlowConfig(@PathVariable("flowId") String flowId) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return ResultDataModel.handleSuccessResult(this.flowConfigService.getShowFlowConfig(flowId));
    }

}
