package com.whaty.products.service.task.strategy;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.util.TycjCollectionUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Map;

/**
 * 我的任务策略
 *
 * @author suoqiangqiang
 */
@Lazy
@Component("chargeByMeTrainingTaskStrategy")
public class ChargeByMeTrainingTaskStrategy extends AbstractTrainingTaskStrategy {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public Map<String, Object> listTrainingTask(String priorityLevel, String taskType, String taskStatus, String search,
                                                String showType, Map<String, Object> page) {
        String managerId = UserUtils.getCurrentManager().getId();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                              ");
        sql.append("    count(1)                                                         ");
        sql.append(" FROM (                                                              ");
        sql.append("    SELECT                                                           ");
        sql.append("    	1                                                            ");
        sql.append("    FROM                                                             ");
        sql.append("    	training_manager_task tmt                                    ");
        sql.append("    INNER JOIN enum_const tt on tt.id=tmt.flag_task_type             ");
        sql.append("    INNER JOIN enum_const pt on pt.id=tmt.flag_priority_level        ");
        sql.append("    left JOIN enum_const ttt on ttt.id=tmt.flag_training_task_status ");
        sql.append("    INNER JOIN enum_const nw on nw.id=tmt.flag_notify_way            ");
        sql.append("    INNER JOIN pe_manager cre on cre.id=tmt.create_manager           ");
        sql.append("    WHERE                                                            ");
        sql.append("      tmt.charge_persons like '%" + managerId + "%'                  ");
        if (StringUtils.isNotBlank(priorityLevel)) {
            sql.append(" AND pt.id = '" + priorityLevel + "' ");
        }
        if (StringUtils.isNotBlank(taskType)) {
            sql.append(" AND tt.id = '" + taskType + "' ");
        }
        if (StringUtils.isNotBlank(taskStatus)) {
            sql.append(this.generateTaskStatusConditionalSql("tmt", "ttt", taskStatus));
        }
        if (StringUtils.isNotBlank(search)) {
            sql.append(" AND tmt.name like '%" + search + "%' ");
        }
        sql.append(" ) r");
        page = this.countPage(page, this.generalDao.<BigInteger>getOneBySQL(sql.toString()).intValue());
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                           ");
        sql.append("   tmt.id as id,                                                  ");
        sql.append("   tmt.name as name,                                              ");
        sql.append("   tt.name as taskType,                                           ");
        sql.append("   cre.name as createManager,                                     ");
        sql.append("   date_format(tmt.start_time,'%Y-%m-%d %H:%i:%s') as startTime,  ");
        sql.append("   date_format(tmt.end_time,'%Y-%m-%d %H:%i:%s') as endTime,      ");
        sql.append("   nw.name as notifyWay,                                          ");
        sql.append("   pt.name as priorityLevel,                                      ");
        sql.append("   pt.code as priorityLevelCode,                                  ");
        sql.append("   tmt.note as note,                                              ");
        sql.append("   CASE WHEN tmt.start_time>now() THEN '未开始'                                                  ");
        sql.append("   WHEN tmt.start_time<=now() and tmt.end_time>=now() AND ifnull(ttt.code,'')<>'1' THEN '未完成' ");
        sql.append("   WHEN tmt.start_time<=now() and tmt.end_time>=now() AND ifnull(ttt.code,'')='1' THEN '已完成'  ");
        sql.append("   WHEN tmt.end_time<now() AND ifnull(ttt.code,'')<>'1' THEN '已延期'                            ");
        sql.append("   WHEN tmt.end_time<now() AND ifnull(ttt.code,'')='1' THEN '延期完成'                           ");
        sql.append("   END as taskStatus,                                                                           ");
        sql.append("   CASE WHEN tmt.start_time>now() THEN 'notStarted'                                             ");
        sql.append("   WHEN tmt.start_time<=now() and tmt.end_time>=now()                                           ");
        sql.append("   AND ifnull(ttt.code,'')<>'1' THEN 'notCompleted'                                             ");
        sql.append("   WHEN tmt.start_time<=now() and tmt.end_time>=now()                                           ");
        sql.append("   AND ifnull(ttt.code,'')='1' THEN 'completed'                                                 ");
        sql.append("   WHEN tmt.end_time<now() AND ifnull(ttt.code,'')<>'1' THEN 'delayed'                          ");
        sql.append("   WHEN tmt.end_time<now() AND ifnull(ttt.code,'')='1' THEN 'delayedCompleted'                  ");
        sql.append("   END as taskStatusCode                                          ");
        sql.append(" FROM                                                             ");
        sql.append("   training_manager_task tmt                                      ");
        sql.append(" INNER JOIN enum_const tt on tt.id=tmt.flag_task_type             ");
        sql.append(" INNER JOIN enum_const pt on pt.id=tmt.flag_priority_level        ");
        sql.append(" left JOIN enum_const ttt on ttt.id=tmt.flag_training_task_status ");
        sql.append(" INNER JOIN enum_const nw on nw.id=tmt.flag_notify_way            ");
        sql.append(" INNER JOIN pe_manager cre on cre.id=tmt.create_manager           ");
        sql.append(" WHERE                                                            ");
        sql.append("   tmt.charge_persons like '%" + managerId + "%'                  ");
        if (StringUtils.isNotBlank(priorityLevel)) {
            sql.append(" AND pt.id = '" + priorityLevel + "' ");
        }
        if (StringUtils.isNotBlank(taskType)) {
            sql.append(" AND tt.id = '" + taskType + "' ");
        }
        if (StringUtils.isNotBlank(taskStatus)) {
            sql.append(this.generateTaskStatusConditionalSql("tmt", "ttt", taskStatus));
        }
        if (StringUtils.isNotBlank(search)) {
            sql.append(" AND tmt.name like '%" + search + "%' ");
        }
        sql.append(" ORDER BY                     ");
        sql.append("   tmt.start_time DESC,       ");
        sql.append("   tmt.end_time DESC          ");
        sql.append(this.generatePageLimit(page));
        return TycjCollectionUtils.map("page", page, "trainingTask", this.generalDao.getMapBySQL(sql.toString()));
    }
}
