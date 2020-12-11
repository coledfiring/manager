package com.whaty.products.service.message.impl;

import com.whaty.domain.bean.message.SendMessageTimeConfig;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.job.JobStatusBus;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 发送消息配置服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("sendMessageTimeConfigManageService")
public class SendMessageTimeConfigManageServiceImpl extends TycjGridServiceAdapter<SendMessageTimeConfig> {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    private final JobStatusBus jobStatusBus = new JobStatusBus();

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.generalDao.checkNotEmpty("select 1 from send_message_time_config conf " +
                "inner join enum_const ss on ss.id = conf.flag_send_status where ss.code <> '0' AND "
                + CommonUtils.madeSqlIn(idList, "conf.id"))) {
            throw new EntityException("不能删除发送了的数据");
        }
    }

    @Override
    public void afterDelete(List idList) throws EntityException {
        List<String> list = this.generalDao.getBySQL("select schedule_job_id from send_message_time_config where "
                + CommonUtils.madeSqlIn(idList, "id"));
        String currentDataSource = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        this.generalDao.executeBySQL("delete from pe_schedule_trigger where " +
                CommonUtils.madeSqlIn(list, "fk_job_id"));
        this.generalDao.executeBySQL("delete from pe_schedule_job where " + CommonUtils.madeSqlIn(list, "id"));
        MasterSlaveRoutingDataSource.setDbType(currentDataSource);
        jobStatusBus.setUpdateStatus();
    }
}
