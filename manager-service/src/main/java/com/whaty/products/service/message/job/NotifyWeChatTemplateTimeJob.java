package com.whaty.products.service.message.job;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.WeChatTemplateTimeRecord;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.products.service.message.MessageNoticeService;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.util.SQLHandleUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 微信模板定时通知工作类
 *
 * @author weipengsen
 */
@Lazy
@Component("notifyWeChatTemplateTimeJob")
public class NotifyWeChatTemplateTimeJob {

    @Resource(name = "messageNoticeServiceImpl")
    private MessageNoticeService messageNoticeService;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    /**
     * 执行查询
     *
     */
    public void invoke() throws Exception {
        List<Map<String, Object>> noticeConfig = this.getNoticeConfigData();
        if (CollectionUtils.isEmpty(noticeConfig)) {
            return;
        }
        for (Map<String, Object> elem : noticeConfig) {
            this.noticeSingleSite(elem);
        }
    }

    /**
     * 通知单一站点
     *
     * @param config
     */
    private void noticeSingleSite(Map<String, Object> config) throws Exception {
        MasterSlaveRoutingDataSource.setDbType((String) config.get("siteCode"));
        String earlyDays = (String) config.get("earlyDays");
        config.put("earlyDays", ((String) config.get("earlyDays")).split(","));
        Map<String, Object> data = this.openGeneralDao.getOneMapBySQL(SQLHandleUtils
                .replaceSignUseParams((String) config.get("dataSql"), config));
        if (MapUtils.isEmpty(data)) {
            return;
        }
        for (Map.Entry<String, Object> elem : data.entrySet()) {
            if (elem.getValue() != null) {
                elem.setValue(((String) elem.getValue()).split(","));
            }
        }
        data.put(MessageConstants.PARAM_TEMPLATE_CODE, config.get("templateCode"));
        data.put(MessageConstants.PARAM_MESSAGE_TYPE, "weChatTemplate");
        data.putAll(this.getSystemVariables());
        try {
            this.messageNoticeService.notice(data, (String) config.get("siteCode"));
            MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
            this.openGeneralDao.save(WeChatTemplateTimeRecord.successRecord((String) config.get("id"), earlyDays,
                    (String) config.get("name")));
        } catch (Exception e) {
            MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
            this.openGeneralDao.save(WeChatTemplateTimeRecord.failureRecord((String) config.get("id"), earlyDays,
                    (String) config.get("name"), e));
            throw e;
        }
    }

    /**
     * 获取系统属性
     *
     * @return
     */
    private Map<String, String> getSystemVariables() {
        List<Object[]> data = this.openGeneralDao
                .getBySQL("select name, value from system_variables where site_code = ?", SiteUtil.getSiteCode());
        return data.stream().collect(Collectors.toMap(e -> (String) e[0],e -> (String) e[1]));
    }

    /**
     * 获取需要通知的配置数据
     *
     * @return
     */
    private List<Map<String, Object>> getNoticeConfigData() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                                  ");
        sql.append(" 	wtt.id as id,                                                                ");
        sql.append(" 	wtt.name as name,                                                                ");
        sql.append(" 	pws.code as siteCode,                                                                ");
        sql.append(" 	wtt.data_sql as dataSql,                                                             ");
        sql.append(" 	wtt.early_days as earlyDays,                                                         ");
        sql.append(" 	wtmg.code as templateCode                                                            ");
        sql.append(" FROM                                                                                    ");
        sql.append(" 	wechat_template_time wtt                                                             ");
        sql.append(" INNER JOIN enum_const wtta on wtta.id = wtt.flag_active                                 ");
        sql.append(" INNER JOIN wechat_template_message_site wtms on wtms.id = wtt.fk_wechat_template_site_id");
        sql.append(" INNER JOIN wechat_template_message_group wtmg on wtmg.id = wtms.fk_template_group_id    ");
        sql.append(" INNER JOIN enum_const wtmga on wtmga.id = wtmg.flag_active                              ");
        sql.append(" INNER JOIN pe_web_site pws on pws.id = wtms.fk_web_site_id                              ");
        sql.append(" WHERE                                                                                   ");
        sql.append(" 	wtta.code = '1'                                                                      ");
        sql.append(" AND wtmga.code = '1'                                                                    ");
        return this.openGeneralDao.getMapBySQL(sql.toString());
    }

}
