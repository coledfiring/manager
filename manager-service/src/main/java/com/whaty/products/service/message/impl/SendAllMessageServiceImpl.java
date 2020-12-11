package com.whaty.products.service.message.impl;

import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.domain.bean.message.SendMessageTimeConfig;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.message.MessageNoticeService;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.products.service.message.domain.SendMessageParameter;
import com.whaty.schedule.bean.PeScheduleJob;
import com.whaty.schedule.bean.PeScheduleTrigger;
import com.whaty.schedule.grid.constants.ScheduleConstants;
import com.whaty.util.CommonUtils;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.MalformedObjectNameException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.whaty.products.service.message.constant.MessageConstants.SCHEDULE_CONFIG_GROUP;

/**
 * 发送所有类型消息服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("sendAllMessageService")
public class SendAllMessageServiceImpl extends TycjGridServiceAdapter {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Resource(name = "messageNoticeServiceImpl")
    private MessageNoticeService messageNoticeService;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    /**
     * 获取消息配置
     *
     * @param code
     * @return
     */
    @LogAndNotice("获取消息配置")
    public Map<String, Object> getMessageConfig(String code) {
        if (StringUtils.isBlank(code)) {
            throw new ParameterIllegalException();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                 ");
        sql.append(" 	g. NAME AS title,                                                   ");
        sql.append(" 	ct. NAME AS type,                                                   ");
        sql.append(" 	ct. code AS typeCode,                                               ");
        sql.append(" 	ty.message_code AS messageCode                                      ");
        sql.append(" FROM                                                                   ");
        sql.append(" 	send_message_group g                                                ");
        sql.append(" INNER JOIN send_message_type ty ON ty.fk_send_message_group_id = g.id  ");
        sql.append(" INNER JOIN enum_const ct ON ct.id = ty.flag_message_type               ");
        sql.append(" INNER JOIN send_message_site s ON s.fk_send_message_type_id = ty.id    ");
        sql.append(" WHERE                                                                  ");
        sql.append(" 	s.fk_web_site_id = '" + SiteUtil.getSite().getId() + "'             ");
        sql.append(" AND g.`code` = '" + code + "'                                          ");
        List<Map<String, Object>> basicConfigList = this.generalDao.getMapBySQL(sql.toString());
        if (CollectionUtils.isEmpty(basicConfigList)) {
            throw new ServiceException("此消息组没有配置任何类型的消息，请联系管理员");
        }
        Map<String, Object> messageConfig = new HashMap<>(4);
        messageConfig.put("title", basicConfigList.get(0).get("title"));
        messageConfig.put("messageType", basicConfigList);
        return messageConfig;
    }

    /**
     * 发送消息
     *
     * @param sendMessageParameter
     */
    @LogAndNotice("发送消息")
    public void sendMessage(SendMessageParameter sendMessageParameter) throws Exception {
        if (sendMessageParameter.isSendNow()) {
            this.sendMessageNow(sendMessageParameter);
        } else {
            ParamsDataModel paramsDataModel = sendMessageParameter.getParams();
            paramsDataModel.getParams().put(CommonConstant.PARAM_IDS, this.getIds(paramsDataModel));
            paramsDataModel.getParams().put(CommonConstant.PARAM_SITE_CODE, SiteUtil.getSiteCode());
            paramsDataModel.getParams().put(CommonConstant.PARAM_USER_ID, this.userService.getCurrentUser().getId());
            this.scheduleSendMessage(sendMessageParameter);
        }
    }

    /**
     * 增加发送消息调度任务
     *
     * @param sendMessageParameter
     */
    private void scheduleSendMessage(SendMessageParameter sendMessageParameter)
            throws MalformedObjectNameException, SocketException, SchedulerException, UnknownHostException {
        // 时间是否在当前时间之前
        if (CommonUtils.compareDateDetail(sendMessageParameter.getTime(), new Date())) {
            throw new ServiceException("发送的时间必须在当前时间之后");
        }
        // 创建消息定时记录
        SendMessageTimeConfig config = null;
        PeScheduleJob job = null;
        Set<PeScheduleTrigger> triggers = null;
        String currentDataSource = MasterSlaveRoutingDataSource.getDbType();
        try {
            config = this.generateTimeConfig(sendMessageParameter);
            this.openGeneralDao.save(config);
            sendMessageParameter.setConfigId(config.getId());
            // 创建定时任务调度
            MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
            job = this.generateScheduleJob();
            this.openGeneralDao.save(job);
            triggers = this.generateScheduleTriggers(sendMessageParameter, job);
            this.openGeneralDao.saveAll(triggers);
            job.setTriggers(triggers);
            MasterSlaveRoutingDataSource.setDbType(currentDataSource);
            config.setScheduleJobId(job.getId());
            this.openGeneralDao.save(config);
            // 增加调度
            job.scheduleJob();
        } catch (RuntimeException e) {
            if (config != null) {
                MasterSlaveRoutingDataSource.setDbType(currentDataSource);
                this.openGeneralDao.delete(config);
            }
            if (triggers != null) {
                MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
                this.openGeneralDao.deleteAll(triggers);
            }
            if (job != null) {
                MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
                this.openGeneralDao.delete(job);
            }
            throw e;
        }
    }

    /**
     * 生成触发器
     *
     * @param sendMessageParameter
     * @param job
     * @return
     */
    private Set<PeScheduleTrigger> generateScheduleTriggers(SendMessageParameter sendMessageParameter,
                                                            PeScheduleJob job) {
        EnumConst valid = this.openGeneralDao
                .getEnumConstByNamespaceCode(ScheduleConstants.ENUM_CONST_NAMESPACE_JOB_VALID, "1");
        PeScheduleTrigger trigger = new PeScheduleTrigger();
        trigger.setEnumConstByFlagJobValid(valid);
        trigger.setPeScheduleJob(job);
        trigger.setTriggerTime(sendMessageParameter.getTime());
        JSONObject data = JSONObject.fromObject(sendMessageParameter);
        JSONObject param = (JSONObject) data.get("params");
        if (JSONNull.getInstance().equals(param.get("page"))) {
            param.remove("page");
        }
        trigger.setData(data.toString());
        trigger.setGroup(SCHEDULE_CONFIG_GROUP);
        trigger.setName(UUID.randomUUID().toString().replace("-", ""));
        trigger.setInputDate(new Date());
        return Collections.singleton(trigger);
    }

    /**
     * 生成调度任务
     *
     * @return
     */
    private PeScheduleJob generateScheduleJob() {
        EnumConst valid = this.openGeneralDao
                .getEnumConstByNamespaceCode(ScheduleConstants.ENUM_CONST_NAMESPACE_JOB_VALID, "1");
        PeScheduleJob job = new PeScheduleJob();
        job.setVersion(1L);
        job.setEnumConstByFlagJobValid(valid);
        job.setEnumConstByFlagIsShow(this.openGeneralDao
                .getEnumConstByNamespaceCode(ScheduleConstants.ENUM_CONST_NAMESPACE_IS_SHOW, "0"));
        job.setEnumConstByFlagIsSingleton(this.openGeneralDao
                .getEnumConstByNamespaceCode(ScheduleConstants.ENUM_CONST_NAMESPACE_IS_SINGLETON, "1"));
        job.setGroup(SCHEDULE_CONFIG_GROUP);
        job.setName(UUID.randomUUID().toString().replace("-", ""));
        job.setDescription("定时发送消息");
        job.setInputDate(new Date());
        job.setSpringId(MessageConstants.SEND_MESSAGE_JOB_SPRING_BEAN);
        job.setMethodName("invoke");
        return job;
    }

    /**
     * 生成时间配置
     *
     * @param sendMessageParameter
     * @return
     */
    private SendMessageTimeConfig generateTimeConfig(SendMessageParameter sendMessageParameter) {
        SsoUser user = this.openGeneralDao.getById(SsoUser.class, this.userService.getCurrentUser().getId());
        EnumConst noSend = this.openGeneralDao
                .getEnumConstByNamespaceCode(MessageConstants.ENUM_CONST_NAMESPACE_SEND_STATUS, "0");
        SendMessageTimeConfig config = new SendMessageTimeConfig();
        config.setEnumConstByFlagSendStatus(noSend);
        config.setSiteCode(SiteUtil.getSiteCode());
        config.setData(JSONObject.fromObject(sendMessageParameter).toString());
        String currentDataSource = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        String groupName = this.openGeneralDao
                .getOneBySQL("SELECT ty.message_code FROM send_message_group g" +
                                " INNER JOIN send_message_type ty ON ty.fk_send_message_group_id = g.id " +
                                "INNER JOIN enum_const t ON t.id = ty.flag_message_type WHERE g. CODE = ? AND t. CODE = ?",
                        sendMessageParameter.getTemplateCode(), sendMessageParameter.getTypeCode());
        config.setGroupName(groupName);
        EnumConst messageType = this.openGeneralDao
                .getEnumConstByNamespaceCode(MessageConstants.ENUM_CONST_NAMESPACE_MESSAGE_TYPE,
                        sendMessageParameter.getTypeCode());
        config.setMessageType(messageType.getName());
        config.setMessageTypeCode(messageType.getCode());
        config.setTime(sendMessageParameter.getTime());
        config.setReceiveNumber(this.getIds(sendMessageParameter.getParams())
                .split(CommonConstant.SPLIT_ID_SIGN).length);
        config.setCreateDate(new Date());
        MasterSlaveRoutingDataSource.setDbType(currentDataSource);
        config.setCreateBy(user);
        return config;
    }

    /**
     * 立即发送消息
     *
     * @param sendMessageParameter
     */
    private void sendMessageNow(SendMessageParameter sendMessageParameter) throws Exception {
        ParamsDataModel paramsDataModel = sendMessageParameter.getParams();
        paramsDataModel.getParams().put(CommonConstant.PARAM_IDS, this.getIds(paramsDataModel));
        this.messageNoticeService.notice(paramsDataModel.getParams());
        this.recordSend(sendMessageParameter);
    }

    /**
     * 发送记录
     *
     * @param sendMessageParameter
     */
    private void recordSend(SendMessageParameter sendMessageParameter) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        Map<String, Object> messageConfig = this.openGeneralDao
                .getOneMapBySQL("SELECT g.name as name, t.name as type FROM send_message_group g " +
                        "INNER JOIN send_message_type ty ON ty.fk_send_message_group_id = g.id " +
                        "INNER JOIN enum_const t ON t.id = ty.flag_message_type WHERE g. CODE = '" +
                        sendMessageParameter.getTemplateCode() + "' " +
                        "AND t. CODE = '" + sendMessageParameter.getTypeCode() + "'");
        MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
        List<String> idList = this.messageNoticeService.getFilterIds(sendMessageParameter.getParams().getParams());
        StringBuilder sql = new StringBuilder();
        this.openGeneralDao.batchExecuteSql(idList.stream().map(e -> {
            sql.delete(0, sql.length());
            sql.append(" INSERT INTO send_message_analyse (                  ");
            sql.append(" 	group_code,                                      ");
            sql.append(" 	group_name,                                      ");
            sql.append(" 	last_type_name,                                  ");
            sql.append(" 	last_send_time,                                  ");
            sql.append(" 	send_number,                                     ");
            sql.append(" 	receive_id,                                      ");
            sql.append("    site_code                                        ");
            sql.append(" ) values (                                          ");
            sql.append("    '" + sendMessageParameter.getTemplateCode() + "',");
            sql.append("    '" + messageConfig.get("name") + "',             ");
            sql.append("    '" + messageConfig.get("type") + "',             ");
            sql.append("    now(),                                           ");
            sql.append("    1,                                               ");
            sql.append("    '" + e + "',                                     ");
            sql.append("    '" + SiteUtil.getSiteCode() + "'                 ");
            sql.append(" ) ON DUPLICATE KEY UPDATE                           ");
            sql.append(" 	last_type_name = '" + messageConfig.get("type") + "', ");
            sql.append(" 	last_send_time = now(),                          ");
            sql.append(" 	send_number = send_number + 1                    ");
            return sql.toString();
        }).collect(Collectors.toList()));
        String config = JSONObject.fromObject(sendMessageParameter.getParams().getParams()
                .get(MessageConstants.PARAM_MESSAGE_CONFIG)).toString();
        this.openGeneralDao.batchExecuteSql(idList.stream().map(e -> {
            sql.delete(0, sql.length());
            sql.append(" insert into send_message_history ( ");
            sql.append(" 	receive_id,                     ");
            sql.append(" 	group_code,                     ");
            sql.append(" 	group_name,                     ");
            sql.append(" 	type_name,                      ");
            sql.append(" 	message_content,                ");
            sql.append(" 	send_time,                      ");
            sql.append(" 	send_user,                      ");
            sql.append("    site_code                       ");
            sql.append(" ) values (                         ");
            sql.append(" 	'" + e + "',                    ");
            sql.append(" 	'" + sendMessageParameter.getTemplateCode() + "', ");
            sql.append(" 	'" + messageConfig.get("name") + "',");
            sql.append(" 	'" + messageConfig.get("type") + "',");
            sql.append(" 	'" + config + "',               ");
            sql.append(" 	now(),                          ");
            sql.append(" 	'" + this.userService.getCurrentUser().getId() + "',");
            sql.append("    '" + SiteUtil.getSiteCode() + "'                 ");
            sql.append(" )                                  ");
            return sql.toString();
        }).collect(Collectors.toList()));
    }
}
