package com.whaty.products.controller.training;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.impl.CooperationTrainingItemCheckServiceImpl;
import com.whaty.products.service.training.impl.SocietyTrainingItemCheckServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 合作培训项目审核
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/training/cooperationTrainingItemCheck")
public class CooperationTrainingItemCheckController extends TycjGridBaseControllerAdapter<TrainingItem> {

    @Resource(name = "cooperationTrainingItemCheckService")
    private CooperationTrainingItemCheckServiceImpl cooperationTrainingItemCheckService;

    @Resource(name = "societyTrainingItemCheckService")
    private SocietyTrainingItemCheckServiceImpl societyTrainingItemCheckService;

    /**
     * 驳回审核
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/rejectAudit")
    public ResultDataModel rejectAudit(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        this.societyTrainingItemCheckService.doRejectAudit(ids);
        return ResultDataModel.handleSuccessResult("驳回审核成功");
    }

    @Override
    public GridService<TrainingItem> getGridService() {
        return cooperationTrainingItemCheckService;
    }
}
