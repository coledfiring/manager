package com.whaty.products.controller.task;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.TrainingManagerTask;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.task.impl.TrainingManagerTaskManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.TRAINING_MANAGER_TASK;

/**
 * 待办催办任务管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/task/trainingManagerTaskManage")
@BasicOperateRecord("待办催办任务")
@SqlRecord(namespace = "trainingManagerTask", sql = TRAINING_MANAGER_TASK)
public class TrainingManagerTaskManageController extends TycjGridBaseControllerAdapter<TrainingManagerTask> {

    @Resource(name = "trainingManagerTaskManageService")
    private TrainingManagerTaskManageServiceImpl trainingManagerTaskManageService;

    @Override
    public GridService<TrainingManagerTask> getGridService() {
        return trainingManagerTaskManageService;
    }
}
