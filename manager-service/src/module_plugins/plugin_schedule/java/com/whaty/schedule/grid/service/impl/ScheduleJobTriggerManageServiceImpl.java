package com.whaty.schedule.grid.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.schedule.bean.PeScheduleJob;
import com.whaty.schedule.bean.PeScheduleTrigger;
import com.whaty.schedule.job.JobStatusBus;
import com.whaty.schedule.util.CommonUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务调度触发器管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("scheduleJobTriggerManageService")
public class ScheduleJobTriggerManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<PeScheduleTrigger> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    private final JobStatusBus jobStatusBus = new JobStatusBus();

    @Override
    public void checkBeforeAdd(PeScheduleTrigger bean, Map<String, Object> params) throws EntityException {
        bean.setPeScheduleJob(this.myGeneralDao.getById(PeScheduleJob.class,
                (String) params.get(CommonConstant.PARAM_PARENT_ID)));
        bean.setEnumConstByFlagJobValid(this.myGeneralDao.getById(EnumConst.class,
                bean.getEnumConstByFlagJobValid().getId()));
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    protected void afterAdd(PeScheduleTrigger bean) throws EntityException {
        // 检查job和触发器是否有效
        if ("1".equals(bean.getEnumConstByFlagJobValid().getCode())
                && "1".equals(bean.getPeScheduleJob().getEnumConstByFlagJobValid().getCode())) {
            try {
                PeScheduleJob job = this.myGeneralDao.getById(PeScheduleJob.class, bean.getPeScheduleJob().getId());
                // 更新任务版本
                job.addJobVersion();
                jobStatusBus.setUpdateStatus();
            } catch (Exception e) {
                throw new UncheckException(e);
            }
        }
    }

    @Override
    public void checkBeforeUpdate(PeScheduleTrigger bean) throws EntityException {
        bean.setEnumConstByFlagJobValid(this.myGeneralDao.getById(EnumConst.class,
                bean.getEnumConstByFlagJobValid().getId()));
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    protected void afterUpdate(PeScheduleTrigger bean) throws EntityException {
        // 检查job是否有效
        if ("1".equals(bean.getPeScheduleJob().getEnumConstByFlagJobValid().getCode())) {
            try {
                // 更新任务版本
                bean.getPeScheduleJob().addJobVersion();
                jobStatusBus.setUpdateStatus();
            } catch (Exception e) {
                throw new UncheckException(e);
            }
        }
    }

    /**
     * 添加或更新前检查
     * @param bean
     */
    private void checkBeforeAddOrUpdate(PeScheduleTrigger bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " AND id <> '" + bean.getId() + "'";
        // 检查key是否唯一
        List<Object> list = this.myGeneralDao
                .getBySQL("select 1 from pe_schedule_trigger where trigger_group = ? AND trigger_name = ? "
                        + additionalSql, bean.getGroup(), bean.getName());
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("已存在相同的组和名称");
        }
        // 表达式是否正确
        if (StringUtils.isBlank(bean.getCronExpression()) && bean.getTriggerTime() == null) {
            throw new EntityException("cron表达式和触发时间必须填写一个");
        }
        if (StringUtils.isNotBlank(bean.getCronExpression())) {
            if (!CronExpression.isValidExpression(bean.getCronExpression())) {
                throw new EntityException("cronExpression表达式不合法");
            }
            // 同一个job中表达式是否唯一
            list = this.myGeneralDao
                    .getBySQL("select 1 from pe_schedule_trigger where cron_expression = ? AND fk_job_id = ? "
                            + additionalSql, bean.getCronExpression(), bean.getPeScheduleJob().getId());
            if (CollectionUtils.isNotEmpty(list)) {
                throw new EntityException("在此任务中同样的cronExpression已存在");
            }
        } else if (bean.getTriggerTime() != null) {
            if (CommonUtils.compareDateDetail(bean.getTriggerTime(), new Date())) {
                throw new EntityException("触发时间必须在当前时间之后");
            }
            list = this.myGeneralDao
                    .getBySQL("select 1 from pe_schedule_trigger where date_format(trigger_time, '%Y%m%d %h%m%s') = ?" +
                            " AND fk_job_id = ? " + additionalSql,
                            CommonUtils.changeDateToString(bean.getTriggerTime(),
                                    CommonConstant.NO_SIGN_DATETIME_TO_SECOND_FORMAT), bean.getPeScheduleJob().getId());
            if (CollectionUtils.isNotEmpty(list)) {
                throw new EntityException("在此任务中同样的触发时间已存在");
            }
        }
        if (StringUtils.isNotBlank(bean.getData())) {
            try {
                JSONObject.fromObject(bean.getData());
            } catch (Exception e) {
                throw new EntityException("传递数据格式必须为json");
            }
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        // 检查是否有效
        List<Object> list = this.myGeneralDao
                .getBySQL("select 1 from pe_schedule_trigger tr inner join enum_const ac on ac.id = tr.flag_job_valid" +
                        " where ac.code = '1' AND " + CommonUtils.madeSqlIn(idList, "tr.id"));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("存在有效的数据");
        }
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "peScheduleJob.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "peScheduleJob.id";
    }
}
