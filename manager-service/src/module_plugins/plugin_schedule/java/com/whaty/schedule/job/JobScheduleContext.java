package com.whaty.schedule.job;

import com.whaty.schedule.bean.PeScheduleJob;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文对象
 *
 * @author weipengsen
 */
public class JobScheduleContext {

    private final static Map<String, PeScheduleJob> EXECUTING_JOB = new ConcurrentHashMap<>();

    /**
     * 添加工作
     * @param jobId
     * @param job
     */
    public static void addJob(String jobId, PeScheduleJob job) {
        EXECUTING_JOB.put(jobId, job);
    }

    /**
     * 获取工作id
     * @return
     */
    public static Set<String> getJobId() {
        return EXECUTING_JOB.keySet();
    }

    /**
     * 获取执行中任务集合
     * @return
     */
    public static Map<String, PeScheduleJob> getExecutingJob() {
        return EXECUTING_JOB;
    }

    /**
     * 移除
     * @param id
     */
    public static void remove(String id) {
        EXECUTING_JOB.remove(id);
    }
}
