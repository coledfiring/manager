package com.whaty.schedule.job.service;

import com.whaty.schedule.bean.PeScheduleJob;
import com.whaty.schedule.dao.QuartzDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工作调度处理器
 *
 * @author weipengsen
 */
@Lazy
@Component("scheduleJobService")
public class ScheduleJobService {

    @Resource(name = "quartzDao")
    private QuartzDao quartzDao;

    /**
     * 调度任务，声明式事务入口
     */
    public void doScheduleJob() {
        // 查询所有持久化的调度数据
        List<PeScheduleJob> jobs = this.quartzDao.listAllJobs();
        if (CollectionUtils.isNotEmpty(jobs)) {
            // 注册工作任务
            PeScheduleJob.scheduleJobs(jobs);
        }
    }

}
