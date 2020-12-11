package com.whaty.products.service.message.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.message.SendMessageType;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 发送消息类型管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("sendMessageTypeManageService")
public class SendMessageTypeManageServiceImpl extends TycjGridServiceAdapter<SendMessageType> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(SendMessageType bean, Map<String, Object> params) throws EntityException {
        bean.setEnumConstByFlagMessageType(this.myGeneralDao.getById(EnumConst.class,
                bean.getEnumConstByFlagMessageType().getId()));
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(SendMessageType bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    /**
     * 添加或修改前检查
     *
     * @param bean
     */
    private void checkBeforeAddOrUpdate(SendMessageType bean) throws EntityException {
        bean.setEnumConstByFlagMessageType(this.myGeneralDao
                .getById(EnumConst.class, bean.getEnumConstByFlagMessageType().getId()));
        if (!MessageType.checkCodeIsExists(bean.getEnumConstByFlagMessageType().getCode(),
                bean.getMessageCode())) {
            throw new EntityException("选中的类型的消息组中没有此编号");
        }
    }

    /**
     * 添加消息到站点
     *
     * @param ids
     * @param siteId
     */
    public void addToSite(String ids, String siteId) {
        TycjParameterAssert.isAllNotBlank(ids, siteId);
        // 发送消息组增加
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO send_message_site (           ");
        sql.append(" 	fk_send_message_type_id,               ");
        sql.append(" 	fk_web_site_id                         ");
        sql.append(" ) SELECT                                  ");
        sql.append(" 	ty.id,                                 ");
        sql.append(" 	s.id                                   ");
        sql.append(" FROM                                      ");
        sql.append(" 	send_message_type ty,                  ");
        sql.append(" 	pe_web_site s                          ");
        sql.append(" WHERE                                     ");
        sql.append(CommonUtils.madeSqlIn(ids, "ty.id"));
        sql.append(" AND " + CommonUtils.madeSqlIn(siteId, "s.id"));
        sql.append(" AND NOT EXISTS (                          ");
        sql.append(" 	SELECT                                 ");
        sql.append(" 		1                                  ");
        sql.append(" 	FROM                                   ");
        sql.append(" 		send_message_site si               ");
        sql.append(" 	WHERE                                  ");
        sql.append(" 		si.fk_send_message_type_id = ty.id ");
        sql.append(" 	AND si.fk_web_site_id = s.id           ");
        sql.append(" )                                         ");
        this.myGeneralDao.executeBySQL(sql.toString());
        // 三个消息增加
        List<Map<String, Object>> messageCode = this.myGeneralDao
                .getMapBySQL("select smt.message_code as messageCode, ty.code as typeCode from send_message_type smt " +
                        "inner join enum_const ty on ty.id = smt.flag_message_type where " +
                        CommonUtils.madeSqlIn(ids, "smt.id"));
        messageCode.forEach(e -> MessageType.addMessageSite((String) e.get("typeCode"),
                (String) e.get("messageCode"), siteId));
    }

    /**
     * 消息类型与处理方法枚举
     *
     * @author weipengsen
     */
    private enum MessageType {

        /**
         * 微信模板
         */
        WE_CHAT_TEMPLATE_MESSAGE("1", e -> StaticBeanUtils.getGeneralDao().checkNotEmpty("select 1 from " +
                "wechat_template_message_group g inner join enum_const ac on ac.id = g.flag_active" +
                " where g.code = ? and ac.code = '1'", e), (code, siteId) -> {
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO wechat_template_message_site (  ");
            sql.append(" 	fk_template_group_id,                    ");
            sql.append(" 	fk_web_site_id,                          ");
            sql.append(" 	template_id                              ");
            sql.append(" ) SELECT                                    ");
            sql.append(" 	g.id,                                    ");
            sql.append(" 	s.id,                                    ");
            sql.append(" 	si.template_id                           ");
            sql.append(" FROM                                        ");
            sql.append(" 	wechat_template_message_group g,         ");
            sql.append(" 	pe_web_site s,                           ");
            sql.append(" 	wechat_template_message_site si,         ");
            sql.append(" 	pe_web_site ba                           ");
            sql.append(" WHERE                                       ");
            sql.append(" 	si.fk_template_group_id = g.id           ");
            sql.append(" AND ba.id = si.fk_web_site_id               ");
            sql.append(" AND ba.code = ?                             ");
            sql.append(" AND g.`code` = ?                            ");
            sql.append(" AND " + CommonUtils.madeSqlIn(siteId, "s.id"));
            sql.append(" AND NOT EXISTS (                            ");
            sql.append(" 	SELECT                                   ");
            sql.append(" 		1                                    ");
            sql.append(" 	FROM                                     ");
            sql.append(" 		wechat_template_message_site si2     ");
            sql.append(" 	WHERE                                    ");
            sql.append(" 		si2.fk_template_group_id = g.id      ");
            sql.append(" 	AND si2.fk_web_site_id = s.id            ");
            sql.append(" )                                           ");
            StaticBeanUtils.getGeneralDao().executeBySQL(sql.toString(), SiteConstant.SERVICE_SITE_CODE_BASE, code);
        }),
        /**
         * 站内信
         */
        STATION_MESSAGE("2", e -> StaticBeanUtils.getGeneralDao()
                .checkNotEmpty("select 1 from station_message_group g" +
                        " inner join enum_const ac on ac.id = g.flag_active where g.code = ? AND ac.code = '1'", e),
                (code, siteId) -> {
                    StringBuilder sql = new StringBuilder();
                    sql.append(" INSERT INTO station_message_group_site ( ");
                    sql.append(" 	fk_message_group_id,                  ");
                    sql.append(" 	fk_web_site_id,                       ");
                    sql.append(" 	flag_active                           ");
                    sql.append(" ) SELECT                                 ");
                    sql.append(" 	g.id,                                 ");
                    sql.append(" 	s.id,                                 ");
                    sql.append(" 	ac.id                                 ");
                    sql.append(" FROM                                     ");
                    sql.append(" 	station_message_group g,              ");
                    sql.append(" 	pe_web_site s,                        ");
                    sql.append(" 	enum_const ac                         ");
                    sql.append(" WHERE                                    ");
                    sql.append(" 	ac.namespace = ?          ");
                    sql.append(" AND ac.code = '1'                        ");
                    sql.append(" AND g.`code` = ?                        ");
                    sql.append(" AND " + CommonUtils.madeSqlIn(siteId, "s.id"));
                    sql.append(" AND NOT EXISTS (                         ");
                    sql.append(" 	SELECT                                ");
                    sql.append(" 		1                                 ");
                    sql.append(" 	FROM                                  ");
                    sql.append(" 		station_message_group_site si     ");
                    sql.append(" 	WHERE                                 ");
                    sql.append(" 		si.fk_message_group_id = g.id     ");
                    sql.append(" 	AND si.fk_web_site_id = s.id          ");
                    sql.append(" )                                        ");
                    StaticBeanUtils.getGeneralDao().executeBySQL(sql.toString(),
                            EnumConstConstants.ENUM_CONST_NAMESPACE_ACTIVE, code);
                }),
        /**
         * 邮件
         */
        EMAIL_MESSAGE("3", e -> StaticBeanUtils.getGeneralDao().checkNotEmpty("select 1 from email_message_group g " +
                "inner join enum_const ac on ac.id = g.flag_active where g.code = ? and ac.code = '1'", e),
                (code, siteId) -> {
                    StringBuilder sql = new StringBuilder();
                    sql.append(" INSERT INTO email_message_site (            ");
                    sql.append(" 	fk_email_message_group_id,               ");
                    sql.append(" 	fk_web_site_id                           ");
                    sql.append(" ) SELECT                                    ");
                    sql.append(" 	g.id,                                    ");
                    sql.append(" 	s.id                                     ");
                    sql.append(" FROM                                        ");
                    sql.append(" 	email_message_group g,                   ");
                    sql.append(" 	pe_web_site s                            ");
                    sql.append(" WHERE                                       ");
                    sql.append("  g.`code` = ?                              ");
                    sql.append(" AND " + CommonUtils.madeSqlIn(siteId, "s.id"));
                    sql.append(" AND NOT EXISTS (                            ");
                    sql.append(" 	SELECT                                   ");
                    sql.append(" 		1                                    ");
                    sql.append(" 	FROM                                     ");
                    sql.append(" 		email_message_site si                ");
                    sql.append(" 	WHERE                                    ");
                    sql.append(" 		si.fk_email_message_group_id = g.id  ");
                    sql.append(" 	AND si.fk_web_site_id = s.id             ");
                    sql.append(" )                                           ");
                    StaticBeanUtils.getGeneralDao().executeBySQL(sql.toString(), code);
                }),
        /**
         * 短信
         */
        SMS_MESSAGE("4", e -> StaticBeanUtils.getGeneralDao().checkNotEmpty("select 1 from sms_message_group g" +
                " inner join enum_const ac on ac.id = g.flag_active where g.code = ? and ac.code = '1'", e),
                (code, siteId) -> {
                    StringBuilder sql = new StringBuilder();
                    sql.append(" INSERT INTO sms_message_site (           ");
                    sql.append(" 	fk_sms_message_group_id,              ");
                    sql.append(" 	fk_web_site_id                        ");
                    sql.append(" ) SELECT                                 ");
                    sql.append(" 	g.id,                                 ");
                    sql.append(" 	s.id                                  ");
                    sql.append(" FROM                                     ");
                    sql.append(" 	sms_message_group g,                  ");
                    sql.append(" 	pe_web_site s                         ");
                    sql.append(" WHERE                                    ");
                    sql.append("  g.`code` = ?                            ");
                    sql.append(" AND " + CommonUtils.madeSqlIn(siteId, "s.id"));
                    sql.append(" AND NOT EXISTS (                         ");
                    sql.append(" 	SELECT                                ");
                    sql.append(" 		1                                 ");
                    sql.append(" 	FROM                                  ");
                    sql.append(" 		sms_message_site si               ");
                    sql.append(" 	WHERE                                 ");
                    sql.append(" 		si.fk_sms_message_group_id = g.id ");
                    sql.append(" 	AND si.fk_web_site_id = s.id          ");
                    sql.append(" )                                        ");
                    StaticBeanUtils.getGeneralDao().executeBySQL(sql.toString(), code);
                }),
        ;

        private String typeCode;

        private Function<String, Boolean> checkExists;

        private BiConsumer<String, String> addMessageSite;

        MessageType(String typeCode, Function<String, Boolean> checkExists, BiConsumer<String, String> addMessageSite) {
            this.typeCode = typeCode;
            this.checkExists = checkExists;
            this.addMessageSite = addMessageSite;
        }

        /**
         * 检查编号是否存在
         *
         * @param type
         * @param code
         * @return
         */
        private static Boolean checkCodeIsExists(String type, String code) {
            return findMessageType(type).getCheckExists().apply(code);
        }

        /**
         * 给站点增加对应消息
         *
         * @param type
         * @param code
         * @param siteId
         */
        private static void addMessageSite(String type, String code, String siteId) {
            findMessageType(type).getAddMessageSite().accept(code, siteId);
        }

        private static MessageType findMessageType(String type) {
            MessageType messageType = Arrays.stream(values()).filter(e -> type.equals(e.getTypeCode()))
                    .findFirst().orElseGet(null);
            if (messageType == null) {
                throw new IllegalArgumentException();
            }
            return messageType;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public Function<String, Boolean> getCheckExists() {
            return checkExists;
        }

        public BiConsumer<String, String> getAddMessageSite() {
            return addMessageSite;
        }
    }

}
