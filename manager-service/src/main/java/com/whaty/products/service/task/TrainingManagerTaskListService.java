package com.whaty.products.service.task;

import java.util.List;
import java.util.Map;

/**
 * 待办催办任务列表服务类
 *
 * @author suoqiangqiang
 */
public interface TrainingManagerTaskListService {

    /**
     * 获取任务列表
     *
     * @param priorityLevel
     * @param taskType
     * @param search
     * @param taskStatus
     * @param page
     * @return
     */
    Map<String, Object> getTrainingTask(String priorityLevel, String taskType, String taskStatus, String search,
                                        String showType, Map<String, Object> page);

    /**
     * 统计待我处理的任务数量
     *
     * @return
     */
    int countWaitHandle();

    /**
     * 任务标记为已完成
     *
     * @param id
     */
    void doSignAsCompleted(String id);

    /**
     * 任务标记为未完成
     *
     * @param id
     */
    void doSignAsNotCompleted(String id);

    /**
     * 获取自定义任务状态
     *
     * @return
     */
    List<Object[]> listFlagTrainingTaskStatus();
}
