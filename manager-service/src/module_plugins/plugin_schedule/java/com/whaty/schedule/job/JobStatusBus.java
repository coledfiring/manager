package com.whaty.schedule.job;

import com.whaty.schedule.util.QuartzUtils;
import org.apache.commons.collections.CollectionUtils;

import static com.whaty.schedule.util.QuartzUtils.getRedisService;

/**
 * 任务状态总线类
 *
 * @author weipengsen
 */
public class JobStatusBus {

    private static long updateVersion;

    private static final String UPDATE_JOB_VERSION_KEY = "schedule_updateJobKey";

    /**
     * 检查工作是否被删除
     * @param jobId
     * @return
     */
    public boolean checkJobDeleted(String jobId) {
        return CollectionUtils.isEmpty(QuartzUtils.getOpenGeneralDao()
                .getBySQL("SELECT 1 from pe_schedule_job WHERE id = ?", jobId));
    }

    /**
     * 检查更新
     * @return
     */
    public boolean checkHasUpdate() {
        Long updateJobVersion = getRedisService().getFromCache(UPDATE_JOB_VERSION_KEY);
        boolean diff = updateJobVersion != null && updateJobVersion != updateVersion;
        if (diff) {
            updateVersion = updateJobVersion;
        }
        return diff;
    }
    
    /**
     * 设置更新任务提醒标记
     */
    public void setUpdateStatus() {
        getRedisService().putToCache(UPDATE_JOB_VERSION_KEY, System.currentTimeMillis());
    }

    /**
     * 检查任务版本是否正确
     * @param id
     * @param version
     * @return
     */
    public boolean checkJobVersionValid(String id, Long version) {
        return CollectionUtils.isNotEmpty(QuartzUtils.getOpenGeneralDao()
                .getBySQL("select 1 from pe_schedule_job where id = ? AND version = ?", id, version));
    }
}
