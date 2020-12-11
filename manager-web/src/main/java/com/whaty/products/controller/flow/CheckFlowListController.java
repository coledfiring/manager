package com.whaty.products.controller.flow;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.flow.CheckFlowListService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 审核列表
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/flow/checkFlowList")
public class CheckFlowListController {

    @Resource(name = "checkFlowListService")
    private CheckFlowListService checkFlowListService;

    /**
     * 获取审核流程
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/getCheckFlow")
    public ResultDataModel getCheckFlow(@RequestBody ParamsDataModel paramsDataModel) {
        String checkStatus = paramsDataModel.getStringParameter("checkStatus");
        String needCheck = paramsDataModel.getStringParameter("needCheck");
        String flowType = paramsDataModel.getStringParameter("flowType");
        String search = paramsDataModel.getStringParameter("search");
        String showType = paramsDataModel.getStringParameter("showType");
        Map<String, Object> page = (Map<String, Object>) paramsDataModel.getParameter("page");
        return ResultDataModel.handleSuccessResult(this.checkFlowListService
                .getCheckFlow(checkStatus, needCheck, flowType, search, showType, page));
    }

    /**
     * 统计待我审核的项目数量
     * @return
     */
    @RequestMapping(value = "/countWaitCheck", method = RequestMethod.GET)
    public ResultDataModel countWaitCheck() {
        return ResultDataModel.handleSuccessResult(this.checkFlowListService.countWaitCheck());
    }

}
