package com.whaty.products.service.message.impl;

import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.constant.SiteConstant;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.message.StationMessageNoticeService;
import com.whaty.products.service.message.constant.MessageConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站内信推送信息获取服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("stationMessageNoticeService")
public class StationMessageNoticeServiceImpl implements StationMessageNoticeService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public Map<String, Object> getMessageGroupInfoById(String groupId) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        Map<String, Object> groupInfo = new HashMap<>(2);
        if (StringUtils.isBlank(groupId)) {
            throw new ParameterIllegalException();
        }
        // 获取sign信息
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                     ");
        sql.append(" 	sign.id AS id,                                                          ");
        sql.append(" 	sign. NAME AS name,                                                     ");
        sql.append(" 	sign.sign AS sign,                                                      ");
        sql.append(" 	sign.example AS example                                                 ");
        sql.append(" FROM                                                                       ");
        sql.append(" 	station_message_group_sign sign                                         ");
        sql.append(" INNER JOIN station_message_group gro ON gro.id = sign.fk_message_group_id  ");
        sql.append(" WHERE                                                                      ");
        sql.append(" 	gro.id = '" + groupId + "'                                              ");
        List<Map<String, Object>> signs = this.generalDao.getMapBySQL(sql.toString());
        groupInfo.put("signs", signs);
        // 获取模板信息
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                                            ");
        sql.append(" 	tem.id AS id,                                                                  ");
        sql.append(" 	tem. NAME AS name,                                                             ");
        sql.append(" 	tem.title AS title,                                                            ");
        sql.append(" 	tem.content AS content,                                                        ");
        sql.append(" 	tem.foot AS foot                                                               ");
        sql.append(" FROM                                                                              ");
        sql.append(" 	station_message_template tem                                                   ");
        sql.append(" INNER JOIN station_message_group_site si ON si.id = tem.fk_message_group_site_id  ");
        sql.append(" WHERE                                                                             ");
        sql.append(" 	si.fk_message_group_id = '" + groupId + "'                                     ");
        sql.append(" AND si.fk_web_site_id = '" + SiteUtil.getSiteId() + "'                            ");
        List<Map<String, Object>> templates = this.generalDao.getMapBySQL(sql.toString());
        groupInfo.put("templates", templates);
        return groupInfo;
    }

    @Override
    public List<Map<String, Object>> listMessageGroup() {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                       ");
        sql.append(" 	gro.id AS id,                                                             ");
        sql.append(" 	gro. NAME AS name                                                         ");
        sql.append(" FROM                                                                         ");
        sql.append(" 	station_message_group gro                                                 ");
        sql.append(" INNER JOIN station_message_group_site si ON gro.id = si.fk_message_group_id  ");
        sql.append(" INNER JOIN enum_const ac ON ac.id = gro.flag_active                          ");
        sql.append(" WHERE                                                                        ");
        sql.append(" 	ac. CODE = '1'                                                            ");
        sql.append(" AND si.fk_web_site_id = '" + SiteUtil.getSiteId() + "'                       ");
        return this.generalDao.getMapBySQL(sql.toString());
    }

    @Override
    @LogAndNotice("保存信息模板")
    public void saveMessageTemplate(Map<String, Object> params) {
        if (!params.containsKey(MessageConstants.PARAM_GROUP_ID)
                || !params.containsKey(MessageConstants.PARAM_MESSAGE_CONFIG)) {
            throw new ParameterIllegalException();
        }
        Map<String, Object> messageConfig = (Map<String, Object>) params.get(MessageConstants.PARAM_MESSAGE_CONFIG);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT 1                                               ");
        sql.append(" FROM                                                   ");
        sql.append(" 	station_message_template  smt                       ");
        sql.append(" WHERE                                                  ");
        sql.append(" 	smt.name = '" + messageConfig.get("name") + "'    ");
        List list = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isNotEmpty(list)) {
            throw new ServiceException("操作失败，模板名称已存在！");
        }
        if (params.get(MessageConstants.PARAM_TEMPLATE_CODE) == null) {
            EnumConst active = this.generalDao.getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, "1");
            sql.delete(0,sql.length());
            sql.append(" INSERT INTO station_message_template (                                 ");
            sql.append(" 	NAME,                                                               ");
            sql.append(" 	fk_message_group_site_id,                                           ");
            sql.append(" 	title,                                                              ");
            sql.append(" 	content,                                                            ");
            sql.append(" 	foot,                                                               ");
            sql.append(" 	flag_active,                                                        ");
            sql.append(" 	use_number                                                          ");
            sql.append(" ) SELECT                                                               ");
            sql.append(" 	'" + messageConfig.get("name") + "',                                ");
            sql.append(" 	si.id,                                                              ");
            sql.append(" 	'" + messageConfig.get("title") + "',                               ");
            sql.append(" 	'" + messageConfig.get("content") + "',                             ");
            sql.append(" 	'" + messageConfig.get("foot") + "',                                ");
            sql.append(" 	'" + active.getId() + "',                                           ");
            sql.append(" 	0                                                                   ");
            sql.append(" FROM                                                                   ");
            sql.append(" 	station_message_group_site si                                       ");
            sql.append(" INNER JOIN station_message_group gro on gro.id = si.fk_message_group_id");
            sql.append(" WHERE                                                                  ");
            sql.append(" 	gro.id = '" +
                    params.get(MessageConstants.PARAM_GROUP_ID) + "'                            ");
            sql.append(" AND si.fk_web_site_id = '" +
                    SiteUtil.getSiteId() + "'                                                   ");
        } else {
            sql.append("update station_message_template set name = '" + messageConfig.get("name") + "', title = '"
                    + messageConfig.get("title") + "', content = '" + messageConfig.get("content") + "', foot = '"
                    + messageConfig.get("foot") + "' where id = '" +
                    params.get(MessageConstants.PARAM_TEMPLATE_CODE) + "'");
        }
        this.generalDao.executeBySQL(sql.toString());
    }

    @Override
    @LogAndNotice("删除站内信模板")
    public void deleteMessageTemplate(String templateId) {
        if (StringUtils.isBlank(templateId)) {
            throw new ParameterIllegalException();
        }
        this.generalDao.executeBySQL("delete from station_message_template where id = '" + templateId + "'");
    }

    @Override
    public Map<String, Object> getMessageGroupInfoByCode(String groupCode) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        Map<String, Object> groupInfo = new HashMap<>(2);
        if (StringUtils.isBlank(groupCode)) {
            throw new ParameterIllegalException();
        }
        // 获取sign信息
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                     ");
        sql.append(" 	sign.id AS id,                                                          ");
        sql.append(" 	sign. NAME AS name,                                                     ");
        sql.append(" 	sign.sign AS sign,                                                      ");
        sql.append(" 	sign.example AS example                                                 ");
        sql.append(" FROM                                                                       ");
        sql.append(" 	station_message_group_sign sign                                         ");
        sql.append(" INNER JOIN station_message_group gro ON gro.id = sign.fk_message_group_id  ");
        sql.append(" WHERE                                                                      ");
        sql.append(" 	gro.code = '" + groupCode + "'                                              ");
        List<Map<String, Object>> signs = this.generalDao.getMapBySQL(sql.toString());
        groupInfo.put("signs", signs);
        // 获取模板信息
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                                            ");
        sql.append(" 	tem.id AS id,                                                                  ");
        sql.append(" 	tem. NAME AS name,                                                             ");
        sql.append(" 	tem.title AS title,                                                            ");
        sql.append(" 	tem.content AS content,                                                        ");
        sql.append(" 	tem.foot AS foot                                                               ");
        sql.append(" FROM                                                                              ");
        sql.append(" 	station_message_template tem                                                   ");
        sql.append(" INNER JOIN station_message_group_site si ON si.id = tem.fk_message_group_site_id  ");
        sql.append(" INNER JOIN station_message_group gro on gro.id = si.fk_message_group_id           ");
        sql.append(" WHERE                                                                             ");
        sql.append(" 	gro.code = '" + groupCode + "'                                                 ");
        sql.append(" AND si.fk_web_site_id = '" + SiteUtil.getSiteId() + "'                            ");
        List<Map<String, Object>> templates = this.generalDao.getMapBySQL(sql.toString());
        groupInfo.put("templates", templates);
        return groupInfo;
    }

}
