package com.whaty.products.service.message.job;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.products.service.message.MessageNoticeService;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.schedule.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 周期报告消息发送任务
 * @author weipengsen
 */
@Lazy
@Component("periodReportMessageJob")
public class PeriodReportMessageJob {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = "messageNoticeServiceImpl")
    private MessageNoticeService messageNoticeService;

    private final static Logger logger = LoggerFactory.getLogger(PeriodReportMessageJob.class);

    public void invoke(Map<String, Object> data) {
        data.put("date", CommonUtils.changeDateToString(new Date()));
        data.put("unit", Period.getPeriodByCode((String) data.get("period")).getUnit());
        this.listCanNoticeSite().forEach(siteCode -> this.noticeSingleSite(siteCode, data));
    }

    enum Period {

        WEEK_PERIOD("week", "周"),

        MONTH_PERIOD("month", "月"),

        YEAR_PERIOD("year", "年"),
        ;

        private String period;

        private String unit;

        Period(String period, String unit) {
            this.period = period;
            this.unit = unit;
        }

        static Period getPeriodByCode(String code) {
            Period period = Arrays.stream(values()).filter(e -> e.getPeriod().equals(code)).findFirst().orElse(null);
            if (period == null) {
                throw new IllegalArgumentException();
            }
            return period;
        }

        public String getPeriod() {
            return period;
        }

        public String getUnit() {
            return unit;
        }

        @Override
        public String toString() {
            return "Period{" +
                    "period='" + period + '\'' +
                    ", unit='" + unit + '\'' +
                    '}';
        }
    }

    /**
     * 通知单个站点
     * @param siteCode
     * @param data
     */
    private void noticeSingleSite(String siteCode, Map<String, Object> data) {
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        data.put(MessageConstants.PARAM_TEMPLATE_CODE, "notifyPeriodReport");
        data.put(MessageConstants.PARAM_MESSAGE_TYPE, "weChatTemplate");
        data.put("basePath", SiteUtil.getSite(siteCode).getDomain());
        data.put("schoolName", SiteUtil.getSite(siteCode).getName());
        List<String> ids = this.generalDao.getBySQL("select m.id from pe_manager m " +
                "inner join enum_const ac on ac.id = m.flag_active " +
                "inner join wechat_user wu on wu.fk_sso_user_id = m.fk_sso_user_id where m.site_code = ?", siteCode);
        data.put(CommonConstant.PARAM_IDS, ids);
        try {
            this.messageNoticeService.notice(data, siteCode);
        } catch (Exception e) {
            logger.warn("report failure " + e.getMessage());
        }
    }

    /**
     * 列举所有可以通知的站点
     * @return
     */
    private List<String> listCanNoticeSite() {
        return this.generalDao.getBySQL("select pws.code from wechat_template_message_group wtmg " +
                "inner join wechat_template_message_site wtms on wtms.fk_template_group_id = wtmg.id " +
                "inner join pe_web_site pws on pws.id = wtms.fk_web_site_id where wtmg.`code` = 'notifyPeriodReport'");
    }

}
