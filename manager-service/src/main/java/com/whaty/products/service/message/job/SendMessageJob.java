package com.whaty.products.service.message.job;

import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.products.service.message.MessageNoticeService;
import com.whaty.products.service.message.constant.MessageConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 信息发送任务类
 *
 * @author weipengsen
 */
@Lazy
@Component("sendMessageJob")
public class SendMessageJob {

    @Resource(name = "messageNoticeServiceImpl")
    private MessageNoticeService messageNoticeService;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    /**
     * 执行发送
     * @param data
     */
    public void invoke(Map<String, Object> data) throws Exception {
        MasterSlaveRoutingDataSource.setDbType((String) data.get("siteCode"));
        if (!this.generalDao.checkNotEmpty("select 1 from send_message_time_config where id = ?",
                (String) data.get("configId"))) {
            return;
        }
        try {
            this.messageNoticeService.notice((Map<String, Object>) ((Map<String, Object>) data.get("params"))
                            .get("params"), (String) data.get("siteCode"));
            EnumConst success = this.generalDao
                    .getEnumConstByNamespaceCode(MessageConstants.ENUM_CONST_NAMESPACE_SEND_STATUS, "1");
            this.generalDao.executeBySQL("update send_message_time_config set flag_send_status = ? where id = ?",
                    success.getId(), data.get("configId"));
        } catch (Exception e) {
            EnumConst failure = this.generalDao
                    .getEnumConstByNamespaceCode(MessageConstants.ENUM_CONST_NAMESPACE_SEND_STATUS, "2");
            String message = e instanceof AbstractBasicException ? ((AbstractBasicException) e).getInfo()
                    : CommonConstant.ERROR_STR;
            this.generalDao.executeBySQL("update send_message_time_config set flag_send_status = ?, " +
                            "failure_message = ? where id = ?", failure.getId(), message, data.get("configId"));
            if (!(e instanceof AbstractBasicException)) {
                throw e;
            }
        } finally {
            String scheduleId = this.generalDao
                    .getOneBySQL("select schedule_job_id from send_message_time_config where id = ?",
                            data.get("configId"));
            MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
            this.generalDao.executeBySQL("delete from pe_schedule_trigger where fk_job_id = ?", scheduleId);
            this.generalDao.executeBySQL("delete from pe_schedule_job where id = ?", scheduleId);
        }
    }

}
