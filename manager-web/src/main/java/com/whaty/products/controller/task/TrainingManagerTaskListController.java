package com.whaty.products.controller.task;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.task.TrainingManagerTaskListService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 任务列表
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/task/trainingManagerTaskList")
public class TrainingManagerTaskListController {

    @Resource(name = "trainingManagerTaskListService")
    private TrainingManagerTaskListService trainingManagerTaskListService;

    /**
     * 获取审核流程
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/getTrainingTask")
    public ResultDataModel getTrainingTask(@RequestBody ParamsDataModel paramsDataModel) {
        String priorityLevel = paramsDataModel.getStringParameter("priorityLevel");
        String taskType = paramsDataModel.getStringParameter("taskType");
        String taskStatus = paramsDataModel.getStringParameter("taskStatus");
        String search = paramsDataModel.getStringParameter("search");
        String showType = paramsDataModel.getStringParameter("showType");
        Map<String, Object> page = (Map<String, Object>) paramsDataModel.getParameter("page");
        return ResultDataModel.handleSuccessResult(this.trainingManagerTaskListService
                .getTrainingTask(priorityLevel, taskType, taskStatus, search, showType, page));
    }

    /**
     * 统计待我审核的项目数量
     *
     * @return
     */
    @GetMapping("/countWaitHandle")
    public ResultDataModel countWaitHandle() {
        return ResultDataModel.handleSuccessResult(this.trainingManagerTaskListService.countWaitHandle());
    }

    /**
     * 任务标记为已完成
     *
     * @return
     */
    @PostMapping("/signAsCompleted")
    public ResultDataModel signAsCompleted(@RequestParam(CommonConstant.PARAM_IDS) String id) {
        this.trainingManagerTaskListService.doSignAsCompleted(id);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 任务标记为未完成
     *
     * @return
     */
    @PostMapping("/signAsNotCompleted")
    public ResultDataModel signAsNotCompleted(@RequestParam(CommonConstant.PARAM_IDS) String id) {
        this.trainingManagerTaskListService.doSignAsNotCompleted(id);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 获得自定义任务状态列表
     *
     * @return
     */
    @RequestMapping("/flagTrainingTaskStatus")
    public ResultDataModel listFlagTrainingTaskStatus() {
        return ResultDataModel.handleSuccessResult(this.trainingManagerTaskListService.listFlagTrainingTaskStatus());
    }
}
