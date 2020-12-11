package com.whaty.products.service.task.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.products.service.task.TrainingManagerTaskListService;
import com.whaty.products.service.task.strategy.AbstractTrainingTaskStrategy;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 待办催办任务列表服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("trainingManagerTaskListService")
public class TrainingManagerTaskListServiceImpl implements TrainingManagerTaskListService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public Map<String, Object> getTrainingTask(String priorityLevel, String taskType, String taskStatus, String search,
                                               String showType, Map<String, Object> page) {
        TycjParameterAssert.isAllNotBlank(showType);
        TycjParameterAssert.isAllNotNull(page);
        return TrainingTaskSearch.getStrategy(showType).listTrainingTask(priorityLevel, taskType, taskStatus, search,
                showType, page);
    }

    @Override
    public int countWaitHandle() {
        return this.generalDao.getBySQL("select 1 from training_manager_task tmt" +
                " left JOIN enum_const ttt on ttt.id=tmt.flag_training_task_status" +
                " WHERE" +
                "   ifnull(ttt.code,'')<>'1'" +
                " and tmt.start_time <= now()" +
                " and tmt.charge_persons like '%" + UserUtils.getCurrentManager().getId() + ",%'").size();
    }

    @Override
    public void doSignAsCompleted(String id) {
        TycjParameterAssert.isAllNotBlank(id);
        this.generalDao.executeBySQL("update training_manager_task tmt " +
                " INNER JOIN enum_const tt on tt.namespace = 'FlagTrainingTaskStatus'" +
                " and tt.code = '1'" +
                " set tmt.flag_training_task_status = tt.id" +
                " ,update_manager = ?" +
                " ,update_time = now()" +
                " where " +
                "   tmt.id = ?", UserUtils.getCurrentManager().getId(), id);
    }

    @Override
    public void doSignAsNotCompleted(String id) {
        TycjParameterAssert.isAllNotBlank(id);
        this.generalDao.executeBySQL("update training_manager_task tmt " +
                " INNER JOIN enum_const tt on tt.namespace = 'FlagTrainingTaskStatus'" +
                " and tt.code = '0'" +
                " set tmt.flag_training_task_status = tt.id" +
                " ,update_manager = ?" +
                " ,update_time = now()" +
                " where " +
                "   tmt.id = ?", UserUtils.getCurrentManager().getId(), id);
    }

    @Override
    public List<Object[]> listFlagTrainingTaskStatus() {
        Object[] notStarted = new Object[]{"notStarted", "未开始"};
        Object[] notCompleted = new Object[]{"notCompleted", "未完成"};
        Object[] completed = new Object[]{"completed", "已完成"};
        Object[] delayed = new Object[]{"delayed", "已延期"};
        Object[] delayedCompleted = new Object[]{"delayedCompleted", "延期完成"};

        List<Object[]> taskStatus = new ArrayList<>(5);
        taskStatus.add(notStarted);
        taskStatus.add(notCompleted);
        taskStatus.add(completed);
        taskStatus.add(delayed);
        taskStatus.add(delayedCompleted);
        return taskStatus;
    }

    private enum TrainingTaskSearch {

        /**
         * 我的任务
         */
        MY_TRAINING_TASK_SEARCH("myTask", "chargeByMeTrainingTaskStrategy"),
        /**
         * 已完成
         */
        COMPLETED_TRAINING_TASK_SEARCH("completed", "completedTrainingTaskStrategy"),
        /**
         * 我发起的
         */
        CREATE_BY_ME_TRAINING_TASK_SEARCH("createByMe", "createByMeTrainingTaskStrategy"),
        /**
         * 抄送我的
         */
        COPY_TO_ME_TRAINING_TASK_SEARCH("copyToMe", "copyToMeTrainingTaskStrategy"),
        ;

        private String showType;

        private String springBean;

        TrainingTaskSearch(String showType, String springBean) {
            this.showType = showType;
            this.springBean = springBean;
        }

        static AbstractTrainingTaskStrategy getStrategy(String showType) {
            return Arrays.stream(values())
                    .filter(e -> e.getShowType().equals(showType))
                    .findFirst()
                    .map(e -> (AbstractTrainingTaskStrategy) SpringUtil.getBean(e.getSpringBean()))
                    .orElseThrow(IllegalArgumentException::new);
        }

        public String getShowType() {
            return showType;
        }

        public String getSpringBean() {
            return springBean;
        }
    }
}
