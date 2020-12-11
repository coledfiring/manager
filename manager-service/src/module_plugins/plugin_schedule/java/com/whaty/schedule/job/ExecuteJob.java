package com.whaty.schedule.job;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.schedule.bean.PeScheduleJob;
import com.whaty.schedule.bean.PeScheduleTrigger;
import com.whaty.schedule.util.QuartzUtils;
import net.sf.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工作任务调度类
 *
 * @author weipengsen
 */
public class ExecuteJob implements Job {

    private PeScheduleJob job;

    private PeScheduleTrigger trigger;

    private final JobStatusBus jobStatusBus = new JobStatusBus();

    private final static Logger logger = LoggerFactory.getLogger(ExecuteJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        try {
            if (jobStatusBus.checkJobDeleted(job.getId())
                    || !jobStatusBus.checkJobVersionValid(job.getId(), job.getVersion())) {
                QuartzUtils.getScheduler().deleteJob(jobExecutionContext.getJobDetail().getKey());
                JobScheduleContext.remove(job.getId());
                return;
            }
            ExecuteProxy.execute(job, trigger, jobExecutionContext);
        } catch (Exception e) {
            logger.error(String.format("execute job[%s] failure", jobExecutionContext.getJobDetail().getKey()), e);
            throw new JobExecutionException(e);
        }
    }

    public void setJob(String job) {
        this.job = (PeScheduleJob) JSONObject.toBean(JSONObject.fromObject(job), PeScheduleJob.class);
    }

    public void setTrigger(String trigger) {
        this.trigger = (PeScheduleTrigger) JSONObject.toBean(JSONObject.fromObject(trigger), PeScheduleTrigger.class);
    }
}
