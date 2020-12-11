package com.whaty.schedule.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.schedule.constants.QuartzConstant;
import com.whaty.schedule.util.CommonUtils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

import static com.whaty.schedule.constants.QuartzConstant.VERSION_STRING_FORMAT;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 任务调度触发器
 * @author weipengsen
 */
@Entity(
        name = "PeScheduleTrigger"
)
@Table(
        name = "pe_schedule_trigger"
)
public class PeScheduleTrigger extends AbstractBean {

    private static final long serialVersionUID = 4613631360348435929L;
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
            name = "trigger_name"
    )
    private String name;
    @Column(
            name = "trigger_group"
    )
    private String group;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "fk_job_id"
    )
    private PeScheduleJob peScheduleJob;
    /**
     * cron表达式
     */
    @Column(
            name = "cron_expression"
    )
    private String cronExpression;
    /**
     * 触发时间，只触发一次
     */
    @Column(
            name = "trigger_time"
    )
    private Date triggerTime;
    /**
     * 任务执行所需数据
     */
    @Column(
            name = "data"
    )
    private String data;
    @Column(
            name = "update_date"
    )
    private Date updateDate;
    @Column(
            name = "input_date"
    )
    private Date inputDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_job_valid"
    )
    private EnumConst enumConstByFlagJobValid;

    /**
     * 获取json序列化配置
     * 排除环形引用
     * @return
     */
    public static JsonConfig getJsonConfig() {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"peScheduleJob"});
        return jsonConfig;
    }

    /**
     * 生成触发器
     * @return
     */
    public Trigger generateTrigger() {
        TriggerBuilder builder = newTrigger()
                .withIdentity(String.format(VERSION_STRING_FORMAT, this.getName(),
                        this.getPeScheduleJob().getVersion()), this.getGroup());
        if (StringUtils.isNotBlank(this.getCronExpression())) {
            builder.withSchedule(cronSchedule(this.getCronExpression()));
        } else {
            builder.startAt(this.getTriggerTime());
        }
        return builder.usingJobData(QuartzConstant.DATA_TRIGGER,
                        JSONObject.fromObject(this, PeScheduleTrigger.getJsonConfig()).toString()).build();
    }

    /**
     * 检查是否可以参与调度
     * @return
     */
    public boolean checkCanSchedule() {
        boolean isCron = StringUtils.isNotBlank(this.getCronExpression());
        boolean triggerDateIsAfter = this.getTriggerTime() != null
                && CommonUtils.compareDateDetail(new Date(), this.getTriggerTime());
        return "1".equals(this.getEnumConstByFlagJobValid().getCode()) && (isCron || triggerDateIsAfter);
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public PeScheduleJob getPeScheduleJob() {
        return peScheduleJob;
    }

    public void setPeScheduleJob(PeScheduleJob peScheduleJob) {
        this.peScheduleJob = peScheduleJob;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public EnumConst getEnumConstByFlagJobValid() {
        return enumConstByFlagJobValid;
    }

    public void setEnumConstByFlagJobValid(EnumConst enumConstByFlagJobValid) {
        this.enumConstByFlagJobValid = enumConstByFlagJobValid;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeScheduleTrigger trigger = (PeScheduleTrigger) o;
        return Objects.equals(id, trigger.id) &&
                Objects.equals(name, trigger.name) &&
                Objects.equals(group, trigger.group) &&
                Objects.equals(peScheduleJob, trigger.peScheduleJob) &&
                Objects.equals(cronExpression, trigger.cronExpression) &&
                Objects.equals(triggerTime, trigger.triggerTime) &&
                Objects.equals(data, trigger.data) &&
                Objects.equals(updateDate, trigger.updateDate) &&
                Objects.equals(inputDate, trigger.inputDate) &&
                Objects.equals(enumConstByFlagJobValid, trigger.enumConstByFlagJobValid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, group, peScheduleJob, cronExpression, triggerTime, data, updateDate,
                inputDate, enumConstByFlagJobValid);
    }

    @Override
    public String toString() {
        return "PeScheduleTrigger{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", peScheduleJob=" + peScheduleJob +
                ", cronExpression='" + cronExpression + '\'' +
                ", triggerTime=" + triggerTime +
                ", data='" + data + '\'' +
                ", updateDate=" + updateDate +
                ", inputDate=" + inputDate +
                ", enumConstByFlagJobValid=" + enumConstByFlagJobValid +
                '}';
    }
}
