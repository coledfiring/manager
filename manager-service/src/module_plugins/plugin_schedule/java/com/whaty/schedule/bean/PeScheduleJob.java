package com.whaty.schedule.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.schedule.constants.QuartzConstant;
import com.whaty.schedule.job.ExecuteJob;
import com.whaty.schedule.job.JobScheduleContext;
import com.whaty.schedule.util.QuartzUtils;
import com.whaty.schedule.util.CommonUtils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.GenericGenerator;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MalformedObjectNameException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.whaty.schedule.constants.QuartzConstant.VERSION_STRING_FORMAT;
import static org.quartz.JobBuilder.newJob;

/**
 * 调度任务
 *
 * @author weipengsen
 */
@Entity(
        name = "PeScheduleJob"
)
@Table(
        name = "pe_schedule_job"
)
public class PeScheduleJob extends AbstractBean implements Cloneable {

    private static final long serialVersionUID = 5112999828978204950L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    /**
     * 任务名称
     */
    @Column(
            name = "job_name"
    )
    private String name;
    /**
     * 任务分组
     */
    @Column(
            name = "job_group"
    )
    private String group;
    /**
     * 任务是否有效
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_job_valid"
    )
    private EnumConst enumConstByFlagJobValid;

    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    @Column(
            name = "bean_class"
    )
    private String beanClass;
    /**
     * 任务调用的方法名
     */
    @Column(
            name = "method_name"
    )
    private String methodName;
    /**
     * spring bean
     */
    @Column(
            name = "spring_id"
    )
    private String springId;
    /**
     * 描述
     */
    @Column(
            name = "description"
    )
    private String description;
    @Column(
            name = "input_date"
    )
    private Date inputDate;
    @Column(
            name = "update_date"
    )
    private Date updateDate;
    @Column(
            name = "version"
    )
    private Long version;

    @Transient
    private String isScheduling;

    @Transient
    private Date nextFireTime;
    /**
     * 调度时间配置
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "fk_job_id"
    )
    private Set<PeScheduleTrigger> triggers;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_is_show"
    )
    private EnumConst enumConstByFlagIsShow;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_is_singleton"
    )
    private EnumConst enumConstByFlagIsSingleton;

    private final static Logger logger = LoggerFactory.getLogger(PeScheduleJob.class);

    /**
     * 获取json配置
     * 目的：过滤环形引用
     *
     * @return
     */
    public static JsonConfig getJsonConfig() {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"peScheduleJob"});
        return jsonConfig;
    }

    /**
     * 增加任务版本
     * 版本作用：通知所有任务更新
     *
     * @throws SchedulerException
     * @throws SocketException
     * @throws UnknownHostException
     */
    public long addJobVersion()
            throws SchedulerException, SocketException, UnknownHostException, MalformedObjectNameException {
        QuartzUtils.getGeneralDao().executeBySQL("UPDATE pe_schedule_job SET version = version + 1 WHERE id = ?",
                this.getId());
        Integer i = QuartzUtils.getGeneralDao()
                .getOneBySQL("SELECT version FROM pe_schedule_job WHERE id = '"+this.getId()+"'");
        this.setVersion(Long.parseLong(String.valueOf(i)));
        return this.getVersion();
    }

    /**
     * 将任务加入调度
     *
     * @throws SocketException
     * @throws UnknownHostException
     * @throws SchedulerException
     */
    public void scheduleJob()
            throws SocketException, UnknownHostException, SchedulerException, MalformedObjectNameException {
        if ("1".equals(this.getEnumConstByFlagJobValid().getCode())) {
            if (logger.isInfoEnabled()) {
                logger.info("job schedule start");
            }
            // 生成工作对象
            JobDetail detail = newJob(ExecuteJob.class)
                    .withIdentity(String.format(VERSION_STRING_FORMAT, this.getName(),
                            this.getVersion()), this.getGroup())
                    .usingJobData(QuartzConstant.DATA_JOB,
                            JSONObject.fromObject(this, PeScheduleJob.getJsonConfig()).toString()).build();
            // 生成调度器计划
            Set<Trigger> triggers = this.getTriggers().stream()
                    .filter(PeScheduleTrigger::checkCanSchedule)
                    .map(PeScheduleTrigger::generateTrigger)
                    .collect(Collectors.toSet());
            // 计划调度
            if (CollectionUtils.isNotEmpty(triggers)) {
                QuartzUtils.getScheduler().scheduleJob(detail, triggers, true);
                JobScheduleContext.addJob(this.getId(), this);
            }
            if (logger.isInfoEnabled()) {
                logger.info(String.format("job[%s] has been scheduled", detail.getKey()));
            }
        }
    }

    /**
     * 调度所有
     * @param jobs
     */
    public static void scheduleJobs(List<PeScheduleJob> jobs) {
        jobs.forEach(e -> {
            try {
                e.scheduleJob();
            } catch (Exception e1) {
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format("schedule job[%s] failure", e), e1);
                }
            }
        });
    }

    /**
     * 更新任务
     */
    public static void updateScheduleJob() {
        // 加入新的调度
        List<PeScheduleJob> newJobs = QuartzUtils.getOpenGeneralDao()
                .getByHQL("from PeScheduleJob where enumConstByFlagJobValid.code = '1' AND "
                        + CommonUtils.madeSqlNotIn(JobScheduleContext.getJobId(), "id"));
        scheduleJobs(newJobs);
        // 更改旧的调度
        JobScheduleContext.getExecutingJob().forEach((k, v) -> {
            if (QuartzUtils.getQuartzDao().checkJobUpdated(k, v.getVersion())) {
                PeScheduleJob job = QuartzUtils.getOpenGeneralDao().getById(PeScheduleJob.class, k);
                try {
                    QuartzUtils.getScheduler().deleteJob(new JobKey(String.format(VERSION_STRING_FORMAT,
                            v.getName(), v.getVersion()), v.getGroup()));
                    boolean jobActive = job != null
                            && "1".equals(job.getEnumConstByFlagJobValid().getCode())
                            && CollectionUtils.isNotEmpty(job.getTriggers())
                            && job.getTriggers().stream()
                                .map(e -> "1".equals(e.getEnumConstByFlagJobValid().getCode()))
                                .reduce(false, (e1, e2) -> e1 || e2);
                    if (jobActive) {
                        job.scheduleJob();
                    } else {
                        JobScheduleContext.remove(k);
                    }
                } catch (Exception e) {
                    logger.error("delete job failure", e);
                }
            }
        });
    }

    /**
     * 通过id查询任务
     *
     * @param id
     * @return
     */
    public static PeScheduleJob getById(String id) {
        return QuartzUtils.getGeneralDao().getById(PeScheduleJob.class, id);
    }

    /**
     * 是否是单例的
     * @return
     */
    public boolean isSingleton() {
        return "1".equals(this.getEnumConstByFlagIsSingleton().getCode());
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getSpringId() {
        return springId;
    }

    public void setSpringId(String springId) {
        this.springId = springId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Set<PeScheduleTrigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(Set<PeScheduleTrigger> triggers) {
        this.triggers = triggers;
    }

    public String getIsScheduling() {
        return isScheduling;
    }

    public void setIsScheduling(String isScheduling) {
        this.isScheduling = isScheduling;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public EnumConst getEnumConstByFlagIsShow() {
        return enumConstByFlagIsShow;
    }

    public void setEnumConstByFlagIsShow(EnumConst enumConstByFlagIsShow) {
        this.enumConstByFlagIsShow = enumConstByFlagIsShow;
    }

    public EnumConst getEnumConstByFlagIsSingleton() {
        return enumConstByFlagIsSingleton;
    }

    public void setEnumConstByFlagIsSingleton(EnumConst enumConstByFlagIsSingleton) {
        this.enumConstByFlagIsSingleton = enumConstByFlagIsSingleton;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeScheduleJob that = (PeScheduleJob) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(group, that.group) &&
                Objects.equals(enumConstByFlagJobValid, that.enumConstByFlagJobValid) &&
                Objects.equals(beanClass, that.beanClass) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(springId, that.springId) &&
                Objects.equals(description, that.description) &&
                Objects.equals(inputDate, that.inputDate) &&
                Objects.equals(updateDate, that.updateDate) &&
                Objects.equals(version, that.version) &&
                Objects.equals(isScheduling, that.isScheduling) &&
                Objects.equals(nextFireTime, that.nextFireTime) &&
                Objects.equals(enumConstByFlagIsShow, that.enumConstByFlagIsShow) &&
                Objects.equals(enumConstByFlagIsSingleton, that.enumConstByFlagIsSingleton);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, group, enumConstByFlagJobValid, beanClass, methodName, springId,
                description, inputDate, updateDate, version, isScheduling, nextFireTime,
                enumConstByFlagIsShow, enumConstByFlagIsSingleton);
    }

    @Override
    public String toString() {
        return "PeScheduleJob{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", enumConstByFlagJobValid=" + enumConstByFlagJobValid +
                ", beanClass='" + beanClass + '\'' +
                ", methodName='" + methodName + '\'' +
                ", springId='" + springId + '\'' +
                ", description='" + description + '\'' +
                ", inputDate=" + inputDate +
                ", updateDate=" + updateDate +
                ", version=" + version +
                ", isScheduling='" + isScheduling + '\'' +
                ", nextFireTime=" + nextFireTime +
                ", enumConstByFlagIsShow=" + enumConstByFlagIsShow +
                ", enumConstByFlagIsSingleton=" + enumConstByFlagIsSingleton +
                '}';
    }
}
