package com.whaty.products.controller.workspace;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PolicyRegulation;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.constant.TrainingConstant;
import com.whaty.products.service.workspace.PolicyRegulationServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 政策法规管理
 *
 * @author shangyu
 */
@Lazy
@RestController
@RequestMapping("/entity/workspace/policyRegulation")
public class PolicyRegulationController extends TycjGridBaseControllerAdapter<PolicyRegulation> {

    @Resource(name = "policyRegulationService")
    private PolicyRegulationServiceImpl policyRegulationService;

    @Override
    public GridService<PolicyRegulation> getGridService() {
        return this.policyRegulationService;
    }

    /**
     * 获取政策法规内容
     * @param extendId
     * @return
     */
    @RequestMapping("/getPolicyRegulationContent")
    public ResultDataModel getTrainingContent(@RequestParam(TrainingConstant.PARAM_EXTEND_ID) String extendId) {
        return ResultDataModel.handleSuccessResult(this.policyRegulationService.getPolicyRegulationContent(extendId));
    }

    /**
     * 保存政策法规内容
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/savePolicyRegulationContent")
    public ResultDataModel saveTrainingContent(@RequestBody ParamsDataModel paramsDataModel) {
        String id = this.getIds(paramsDataModel);
        String content = paramsDataModel.getStringParameter(TrainingConstant.PARAM_CONTENT);
        this.policyRegulationService.savePolicyRegulationContent(id, content);
        return ResultDataModel.handleSuccessResult("保存成功");
    }
}
