package com.whaty.schedule;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.schedule.designer.deal.AbstractDaemonThreadDealSupport;
import com.whaty.schedule.bean.PeScheduleJob;
import com.whaty.schedule.constants.QuartzConstant;
import com.whaty.schedule.job.JobStatusBus;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 调度运行应用类
 *
 * @author weipengsen
 */
@Component("quartzRunApplication")
@DependsOn("springUtil")
public class QuartzRunApplication extends AbstractDaemonThreadDealSupport {

    private final JobStatusBus jobStatusBus = new JobStatusBus();

    private final static Logger logger = LoggerFactory.getLogger(QuartzRunApplication.class);

    @PostConstruct
    public void run() throws SchedulerException {
        this.getScheduler().start();
        this.start();
    }

    /**
     * 创建scheduler实例并返回
     * @return
     * @throws SchedulerException
     */
    @Bean("scheduler")
    public Scheduler getScheduler() throws SchedulerException {
        return new StdSchedulerFactory().getScheduler();
    }

    @Override
    public String getThreadName() {
        return QuartzConstant.CHECK_NEW_JOB_THREAD_NAME;
    }

    @Override
    public void deal() {
        try {
            if (jobStatusBus.checkHasUpdate()) {
                PeScheduleJob.updateScheduleJob();
            }
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            logger.error("check new job error", e);
        }
    }

    @Override
    public void doBeforeLoop() {
        // todo 用于守护线程热更新任务时指定执行的数据库
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
    }
}
