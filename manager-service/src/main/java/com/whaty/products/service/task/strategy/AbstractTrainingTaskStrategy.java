package com.whaty.products.service.task.strategy;

import com.whaty.common.string.StringUtils;

import java.util.Map;

/**
 * 抽象的待办任务策略
 *
 * @author suoqiangqiang
 */
public abstract class AbstractTrainingTaskStrategy {
    /**
     * 列举任务
     *
     * @param priorityLevel
     * @param taskType
     * @param search
     * @param showType
     * @param page
     * @return
     */
    public abstract Map<String, Object> listTrainingTask(String priorityLevel, String taskType, String taskStatus, String search, String showType,
                                                         Map<String, Object> page);

    /**
     * 计算分页
     *
     * @param page
     * @param count
     * @return
     */
    protected Map<String, Object> countPage(Map<String, Object> page, Integer count) {
        Integer pageSize = (Integer) page.get("pageSize");
        page.put("totalNumber", count);
        Integer totalPage = count % pageSize == 0 ? count / pageSize : (count / pageSize + 1);
        page.put("totalPage", totalPage);
        Integer currentPage = (Integer) page.get("currentPage");
        currentPage = currentPage > totalPage ? totalPage : currentPage;
        currentPage = currentPage < 1 ? 1 : currentPage;
        page.put("currentPage", currentPage);
        return page;
    }

    /**
     * 生成分页limit语句
     *
     * @param page
     * @return
     */
    protected String generatePageLimit(Map<String, Object> page) {
        Integer currentPage = (Integer) page.get("currentPage");
        currentPage = currentPage <= 0 ? 1 : currentPage;
        return " LIMIT " + ((currentPage - 1) * (Integer) page.get("pageSize")) + ", " + page.get("pageSize");
    }

    /**
     * 生成任务状态查询sql
     *
     * @param trainingManagerTaskTableAlias               training_manager_task表别名
     * @param enumConstByFlagTrainingTaskStatusTableAlias enum_const中namespace为flag_training_task_status表别名
     * @param taskStatus
     * @return 以AND开头的串或空串
     */
    protected String generateTaskStatusConditionalSql(String trainingManagerTaskTableAlias,
                                                      String enumConstByFlagTrainingTaskStatusTableAlias,
                                                      String taskStatus) {
        if (StringUtils.isBlank(taskStatus) || StringUtils.isBlank(trainingManagerTaskTableAlias) ||
                StringUtils.isBlank(enumConstByFlagTrainingTaskStatusTableAlias)) {
            return "";
        }
        String result;
        switch (taskStatus) {
            case "notStarted":
                result = " AND " + trainingManagerTaskTableAlias + ".start_time>now() ";
                break;
            case "notCompleted":
                result = " AND " + trainingManagerTaskTableAlias + ".start_time<=now() " +
                        " AND " + trainingManagerTaskTableAlias + ".end_time>=now() " +
                        " AND ifnull(" + enumConstByFlagTrainingTaskStatusTableAlias + ".code,'')<>'1' ";
                break;
            case "completed":
                result = " AND " + trainingManagerTaskTableAlias + ".start_time<=now() " +
                        " AND " + trainingManagerTaskTableAlias + ".end_time>=now() " +
                        " AND ifnull(" + enumConstByFlagTrainingTaskStatusTableAlias + ".code,'')='1' ";
                break;
            case "delayed":
                result = " AND " + trainingManagerTaskTableAlias + ".end_time<now() " +
                        " AND ifnull(" + enumConstByFlagTrainingTaskStatusTableAlias + ".code,'')<>'1' ";
                break;
            case "delayedCompleted":
                result = " AND " + trainingManagerTaskTableAlias + ".end_time<now() " +
                        " AND ifnull(" + enumConstByFlagTrainingTaskStatusTableAlias + ".code,'')='1' ";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }
}
