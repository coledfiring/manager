package com.whaty.schedule.dao;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.schedule.bean.PeScheduleJob;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 调度器dao对象
 *
 * @author weipengsen
 */
@Component("quartzDao")
public class QuartzDao {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    /**
     * 列举所有的任务配置
     * @return
     */
    public List<PeScheduleJob> listAllJobs() {
        return this.generalDao.getByHQL("from PeScheduleJob where enumConstByFlagJobValid.code = '1'");
    }

    /**
     * 查询任务是否更新
     * @param jobId
     * @param version
     * @return
     */
    public boolean checkJobUpdated(String jobId, Long version) {
        return CollectionUtils.isEmpty(this.generalDao
                .getBySQL("SELECT 1 FROM pe_schedule_job where id = ? AND version = ?", jobId, version));
    }

    public GeneralDao getGeneralDao() {
        return generalDao;
    }

    public GeneralDao getOpenGeneralDao() {
        return this.openGeneralDao;
    }

}
