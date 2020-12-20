package com.whaty.products.service.message.strategy.email;

import com.whaty.framework.config.domain.PlatformConfig;
import com.whaty.framework.config.util.PlatformConfigUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.common.mail.WhatyMailUtils;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.products.service.message.strategy.AbstractNoticeStrategy;
import com.whaty.util.SQLHandleUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发送邮件策略
 *
 * @author weipengsen
 */
@Lazy
@Service("emailNoticeStrategy")
public class EmailNoticeStrategy implements AbstractNoticeStrategy {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Override
    public void notice(Map<String, Object> params) throws Exception {
        if (!params.containsKey(CommonConstant.PARAM_IDS)
                || !params.containsKey(MessageConstants.PARAM_TEMPLATE_CODE)) {
            throw new ParameterIllegalException();
        }
        String siteCode = params.get(MessageConstants.PARAM_SITE_CODE) == null ? SiteUtil.getSiteCode() :
                (String) params.get(MessageConstants.PARAM_SITE_CODE);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        Map<String, Object> messageConfig = this.getMessageConfig((String) params
                .get(MessageConstants.PARAM_TEMPLATE_CODE), siteCode);
        params.put(CommonConstant.PARAM_IDS, ((String) params.get(CommonConstant.PARAM_IDS))
                .split(CommonConstant.SPLIT_ID_SIGN));
        String sql = SQLHandleUtils.replaceSignUseParams((String) messageConfig.get("dataSql"), params);
        List<String> receive = this.openGeneralDao.getBySQL(sql);
        if (CollectionUtils.isEmpty(receive)) {
            throw new ServiceException("选择的用户都未填写邮箱");
        }
        PlatformConfig platformConfig = PlatformConfigUtil.getPlatformConfig(SiteUtil.getSiteCode());
       /* WhatyMailUtils.setSenderMail(platformConfig.getMailUser());
        WhatyMailUtils.setSenderMailPassword(platformConfig.getMailPassword());
        WhatyMailUtils.setSmtpServer(platformConfig.getMailSmtp());
        WhatyMailUtils.sendMail("培训平台邮件：" + params.get(MessageConstants.EMAIL_ARG_SUBJECT),
                (String) params.get(MessageConstants.EMAIL_ARG_CONTENT), receive, new ArrayList<>());*/
    }

    @Override
    public String getTableName() {
        return "email_message_group";
    }

    /**
     * 获取消息配置
     * @param templateCode
     * @param siteCode
     * @return
     */
    private Map<String, Object> getMessageConfig(String templateCode, String siteCode) {
        String currentDataSource = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                 ");
        sql.append(" 	g.data_sql AS dataSql                                               ");
        sql.append(" FROM                                                                   ");
        sql.append(" 	email_message_site s                                                ");
        sql.append(" INNER JOIN email_message_group g ON g.id = s.fk_email_message_group_id ");
        sql.append(" INNER JOIN enum_const ac ON ac.id = g.flag_active                      ");
        sql.append(" WHERE                                                                  ");
        sql.append(" 	g. CODE = '" + templateCode + "'                                    ");
        sql.append(" AND s.fk_web_site_id = '" + SiteUtil.getSite(siteCode).getId() + "'    ");
        Map<String, Object> config = this.openGeneralDao.getOneMapBySQL(sql.toString());
        MasterSlaveRoutingDataSource.setDbType(currentDataSource);
        return config;
    }

}
