package com.whaty.schedule.job;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.schedule.job.service.ScheduleJobService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.listeners.JobListenerSupport;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时调度插件
 *
 * @author weipengsen
 */
public class CustomJobHistoryPlugin extends JobListenerSupport implements SchedulerPlugin {

    private String name;

    private static final Logger logger = LoggerFactory.getLogger(CustomJobHistoryPlugin.class);

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void initialize(String name, Scheduler scheduler, ClassLoadHelper classLoadHelper) throws SchedulerException {
        this.name = name;
        scheduler.getListenerManager().addJobListener(this, EverythingMatcher.allJobs());
        if (logger.isInfoEnabled()) {
            logger.info("scheduler initialized");
        }
    }

    @Override
    public void start() {
        // todo 用于指定拿取任务的数据库
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        // 执行任务负载机分配与任务调度
        ((ScheduleJobService) SpringUtil.getBean("scheduleJobService")).doScheduleJob();
    }

    @Override
    public void shutdown() {
    }
}
