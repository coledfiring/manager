package com.whaty.products.service.message.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.WeChatTemplateTime;
import com.whaty.domain.bean.message.WeChatTemplateMessageSite;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 微信模板定时推送
 *
 * @author weipengsen
 */
@Lazy
@Service("weChatTemplateTimeManageService")
public class WeChatTemplateTimeManageServiceImpl extends TycjGridServiceAdapter<WeChatTemplateTime> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(WeChatTemplateTime bean) throws EntityException {
        bean.setWeChatTemplateMessageSite(this.getMessageSite(bean.getWeChatTemplateMessageSite()
                .getWeChatTemplateMessageGroup().getId(), bean.getWeChatTemplateMessageSite().getPeWebSite().getId()));
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(WeChatTemplateTime bean) throws EntityException {
        bean.setWeChatTemplateMessageSite(this.getMessageSite(bean.getWeChatTemplateMessageSite()
                .getWeChatTemplateMessageGroup().getId(), bean.getWeChatTemplateMessageSite().getPeWebSite().getId()));
        this.checkBeforeAddOrUpdate(bean);
    }

    private void checkBeforeAddOrUpdate(WeChatTemplateTime bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " and id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from wechat_template_time where name = ?" + additionalSql,
                bean.getName())) {
            throw new EntityException("此名称已存在");
        }
    }

    /**
     * 获取对应的微信模板站点配置
     * @param groupId
     * @param siteId
     * @return
     */
    private WeChatTemplateMessageSite getMessageSite(String groupId, String siteId) {
        return (WeChatTemplateMessageSite) this.myGeneralDao
                .getOneByHQL("from WeChatTemplateMessageSite where weChatTemplateMessageGroup.id = ? " +
                        "and peWebSite.id = ?", groupId, siteId);
    }

    /**
     * 列举时间配置
     *
     * @return
     */
    public List<Map<String, Object>> listTimeConfig() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                          ");
        sql.append(" 	wtt.id as id,                ");
        sql.append(" 	wtt.name as name,            ");
        sql.append(" 	wtt.early_days as earlyDays, ");
        sql.append(" 	wtta.code as isActive        ");
        sql.append(" FROM                            ");
        sql.append(" 	wechat_template_time wtt     ");
        sql.append(" INNER JOIN wechat_template_message_site wtms ON wtms.id = wtt.fk_wechat_template_site_id");
        sql.append(" INNER JOIN enum_const wtta on wtta.id = wtt.flag_active                                 ");
        sql.append(" INNER JOIN pe_web_site pws ON pws.id = wtms.fk_web_site_id                              ");
        sql.append(" INNER JOIN wechat_template_message_group wtmg on wtmg.id = wtms.fk_template_group_id    ");
        sql.append(" INNER JOIN enum_const wtmga on wtmga.id = wtmg.flag_active                              ");
        sql.append(" WHERE                           ");
        sql.append(" 	wtmga.code = '1'             ");
        sql.append(" AND pws.code = ?                ");
        return this.myGeneralDao.getMapBySQL(sql.toString(), SiteUtil.getSiteCode());
    }

    private final static Pattern EARLY_DAY_FORMAT = Pattern.compile("^([0-9]+,)*[0-9]+$");

    /**
     * 保存时间配置
     * @param timeConfig
     */
    public void saveTimeConfig(List<Map<String, Object>> timeConfig) {
        if (timeConfig.stream().anyMatch(e -> !EARLY_DAY_FORMAT.matcher((String) e.get("earlyDays")).find())) {
            throw new ServiceException("天数格式不正确,多个天数请使用','分割");
        }
        List<String> sqlList = timeConfig.stream().map(e ->
                "update wechat_template_time wtt " +
                        "inner join enum_const ac on ac.namespace = 'flagActive' and ac.code = '" + e.get("isActive")
                        + "' set wtt.flag_active = ac.id, wtt.early_days = '" + e.get("earlyDays") +
                        "' where wtt.id = '" + e.get("id") + "'").collect(Collectors.toList());
        this.myGeneralDao.batchExecuteSql(sqlList);
    }
}
