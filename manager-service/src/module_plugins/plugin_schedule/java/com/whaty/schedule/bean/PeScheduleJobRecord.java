package com.whaty.schedule.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.exception.DataOperateException;
import com.whaty.schedule.constants.QuartzConstant;
import com.whaty.schedule.util.QuartzUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.management.MalformedObjectNameException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.whaty.constant.CommonConstant.NO_SIGN_DATETIME_TO_SECOND_STR;
import static com.whaty.schedule.constants.QuartzConstant.JOB_STATUS_DOING;

/**
 * 工作调度记录
 *
 * @author weipengsen
 */
@Entity(
        name = "PeScheduleJobRecord"
)
@Table(
        name = "pe_schedule_job_record"
)
public class PeScheduleJobRecord extends AbstractBean {

    private static final long serialVersionUID = -4962118191606664430L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    @Column(
            name = "job_name"
    )
    private String jobName;
    @Column(
            name = "job_group"
    )
    private String jobGroup;
    @Column(
            name = "trigger_name"
    )
    private String triggerName;
    @Column(
            name = "trigger_group"
    )
    private String triggerGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_job_status"
    )
    private EnumConst enumConstByFlagJobStatus;
    @Column(
            name = "plan_time"
    )
    private Date planTime;
    @Column(
            name = "start_time"
    )
    private Date startTime;
    @Column(
            name = "end_time"
    )
    private Date endTime;
    @Column(
            name = "next_time"
    )
    private Date nextTime;
    @Column(
            name = "init_data"
    )
    private String initData;
    @Column(
            name = "exception_message"
    )
    private String exceptionMessage;
    /**
     * 执行机
     */
    @Column(
            name = "execute_machine"
    )
    private String executeMachine;

    /**
     * 尝试抢占记录
     * @param job
     * @param trigger
     * @param fireTime
     * @throws SocketException
     * @throws UnknownHostException
     * @throws MalformedObjectNameException
     * @return
     */
    public static PeScheduleJobRecord tryRecord(PeScheduleJob job, PeScheduleTrigger trigger, Date fireTime)
            throws SocketException, UnknownHostException, MalformedObjectNameException {
        StringBuilder sql = new StringBuilder();
        String id = UUID.randomUUID().toString().replace("-", "");
        sql.append(" INSERT INTO `pe_schedule_job_record` ( ");
        sql.append(" `id`, `job_name`, `job_group`, `trigger_name`, `trigger_group`, ");
        sql.append(" `flag_job_status`, `plan_time`, `start_time`, `init_data`, ");
        sql.append(" `execute_machine`) VALUES(");
        sql.append(" '" + id + "', '" + job.getName() + "', ");
        sql.append(" '" + job.getGroup() + "', '" + trigger.getName() + "', ");
        sql.append(" '" + trigger.getGroup() + "', '" + com.whaty.utils.StaticBeanUtils.getOpenGeneralDao()
                .getEnumConstByNamespaceCode(QuartzConstant.ENUM_CONST_NAMESPACE_FLAG_JOB_STATUS,
                        JOB_STATUS_DOING).getId() + "', STR_TO_DATE('" + com.whaty.util.CommonUtils
                .changeDateToString(fireTime, NO_SIGN_DATETIME_TO_SECOND_STR) + "', '%Y%m%d%H%i%s'),");
        sql.append(" now(), '" + trigger.getData() + "', '" + QuartzUtils.getLocalKey() + "')");

        DataSource dataSource = (DataSource) SpringUtil.getBean(MasterSlaveRoutingDataSource.getDbType());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement p = connection.prepareStatement(sql.toString())) {
            connection.setAutoCommit(true);
            if (p.executeUpdate() > 0) {
                return QuartzUtils.getOpenGeneralDao().getById(PeScheduleJobRecord.class, id);
            }
        } catch (SQLException e) {
            if (!e.getMessage().contains("Duplicate entry")) {
                throw new DataOperateException(e);
            }
        }
        return null;
    }

    /**
     * 完成任务
     * @param code
     * @param t
     * @param nextFireTime
     */
    public void finalJob(String code, Throwable t, Date nextFireTime) {
        this.setEnumConstByFlagJobStatus(QuartzUtils.getOpenGeneralDao()
                .getEnumConstByNamespaceCode(QuartzConstant.ENUM_CONST_NAMESPACE_FLAG_JOB_STATUS, code));
        this.setEndTime(new Date());
        this.setNextTime(nextFireTime);
        if (t != null) {
            this.setExceptionMessage(t.toString());
        }
        QuartzUtils.getOpenGeneralDao().save(this);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getInitData() {
        return initData;
    }

    public void setInitData(String initData) {
        this.initData = initData;
    }

    public EnumConst getEnumConstByFlagJobStatus() {
        return enumConstByFlagJobStatus;
    }

    public void setEnumConstByFlagJobStatus(EnumConst enumConstByFlagJobStatus) {
        this.enumConstByFlagJobStatus = enumConstByFlagJobStatus;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExecuteMachine() {
        return executeMachine;
    }

    public void setExecuteMachine(String executeMachine) {
        this.executeMachine = executeMachine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeScheduleJobRecord jobRecord = (PeScheduleJobRecord) o;
        return Objects.equals(id, jobRecord.id) &&
                Objects.equals(jobName, jobRecord.jobName) &&
                Objects.equals(jobGroup, jobRecord.jobGroup) &&
                Objects.equals(triggerName, jobRecord.triggerName) &&
                Objects.equals(triggerGroup, jobRecord.triggerGroup) &&
                Objects.equals(enumConstByFlagJobStatus, jobRecord.enumConstByFlagJobStatus) &&
                Objects.equals(planTime, jobRecord.planTime) &&
                Objects.equals(startTime, jobRecord.startTime) &&
                Objects.equals(endTime, jobRecord.endTime) &&
                Objects.equals(nextTime, jobRecord.nextTime) &&
                Objects.equals(initData, jobRecord.initData) &&
                Objects.equals(exceptionMessage, jobRecord.exceptionMessage) &&
                Objects.equals(executeMachine, jobRecord.executeMachine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobName, jobGroup, triggerName, triggerGroup, enumConstByFlagJobStatus, planTime,
                startTime, endTime, nextTime, initData, exceptionMessage, executeMachine);
    }

    @Override
    public String toString() {
        return "PeScheduleJobRecord{" +
                "id='" + id + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                ", enumConstByFlagJobStatus=" + enumConstByFlagJobStatus +
                ", planTime=" + planTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", nextTime=" + nextTime +
                ", initData='" + initData + '\'' +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                ", executeMachine='" + executeMachine + '\'' +
                '}';
    }
}
