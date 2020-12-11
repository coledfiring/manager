package com.whaty.products.controller.workspace;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.JobArrange;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.constant.TrainingConstant;
import com.whaty.products.service.workspace.JobArrangeServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 工作安排管理
 *
 * @author shangyu
 */
@Lazy
@RestController
@RequestMapping("/entity/workspace/jobArrange")
public class JobArrangeController extends TycjGridBaseControllerAdapter<JobArrange> {

    @Resource(name = "jobArrangeService")
    private JobArrangeServiceImpl jobArrangeService;

    @Override
    public GridService<JobArrange> getGridService() {
        return this.jobArrangeService;
    }

    /**
     * 获得工作安排内容
     * @param extendId 工作安排内容ID
     * @return
     */
    @RequestMapping("/getJobArrangeContent")
    public ResultDataModel getJobArrangeContent(@RequestParam(TrainingConstant.PARAM_EXTEND_ID) String extendId) {
        return ResultDataModel.handleSuccessResult(this.jobArrangeService.getJobArrangeContent(extendId));
    }

    /**
     * 保存工作安排内容
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/saveJobArrangeContent")
    public ResultDataModel saveJobArrangeContent(@RequestBody ParamsDataModel paramsDataModel) {
        String id = this.getIds(paramsDataModel);
        String content = paramsDataModel.getStringParameter(TrainingConstant.PARAM_CONTENT);
        this.jobArrangeService.saveJobArrangeContent(id, content);
        return ResultDataModel.handleSuccessResult("保存成功");
    }
}
