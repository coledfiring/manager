package com.whaty.products.service.seal.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeReceived;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.message.MessageNoticeService;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.util.CollectionDivider;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Stream;

/**
 * 接待管理
 *
 * @author weipengsen
 */
@Lazy
@Service("peReceivedManageService")
public class PeReceivedManageServiceImpl extends TycjGridServiceAdapter<PeReceived> {

    private static final Logger logger = LoggerFactory.getLogger(PeReceivedManageServiceImpl.class);

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = "messageNoticeServiceImpl")
    private MessageNoticeService messageNoticeService;

    /**
     * 获取系统用户
     *
     * @return
     */
    public List<Map<String, Object>> listManager() {
        return generalDao.getMapBySQL("select id, name from pe_manager where name is not null AND name <> '' " +
                "AND site_code = ?", SiteUtil.getSiteCode() );
    }

    /**
     * 添加接待信息
     *
     * @param received
     */
    public void addReceived(PeReceived received) {
        received.setRegistTime(new Date());
        received.setSiteCode(SiteUtil.getSiteCode());
        received.setPeUnit(UserUtils.getCurrentUnit());
        received.setCreateBy(UserUtils.getCurrentUser());
        String receivedId = this.generalDao.save(received).getId();
        this.generalDao.flush();
        Stream.of(received.getReceivedUser().split(CommonConstant.SPLIT_ID_SIGN)).forEach(x ->
                this.generalDao.executeBySQL("INSERT INTO pri_received_manager (id, fk_received_id, fk_pe_manager_id) " +
                        "VALUES (?, ?, ?)", com.whaty.util.CommonUtils.generateUUIDNoSign(), receivedId, x));
        this.sendWeChatMessage(received.getReceivedUser(), received.getReceivedTime(), received.getRegistUser(),
                received.getReceivedBy(), received.getParticipateUser());
    }


    /**
     * 修改接待信息
     *
     * @param received
     */
    public void updateReceived(PeReceived received) {
        received.setSiteCode(SiteUtil.getSiteCode());
        received.setPeUnit(UserUtils.getCurrentUnit());
        received.setRegistTime(new Date());
        this.generalDao.save(received);
        updateManager(received.getId(), Arrays.asList( received.getReceivedUser().split(CommonConstant.SPLIT_ID_SIGN)));
    }
    /**
     * 更新管理员
     *
     * @param id
     * @param userList
     */
    private void updateManager (String id, List<String> userList) {
        TycjParameterAssert.isAllNotNull(id, userList);
        List<String> listManager = this.generalDao.getBySQL("select fk_pe_manager_id from pri_received_manager" +
                " where fk_received_id = ?", id);
        CollectionDivider<String> divide = new CollectionDivider<>(listManager, userList);
        divide.getAdd().forEach(x ->
                this.generalDao.executeBySQL("INSERT INTO pri_received_manager (id, fk_received_id, fk_pe_manager_id) " +
                        "VALUES (?, ?, ?)", com.whaty.util.CommonUtils.generateUUIDNoSign(), id, x));
        this.generalDao.executeBySQL("delete from pri_received_manager where "
                + " fk_received_id = ? AND"
                + CommonUtils.madeSqlIn(divide.getDelete(), "fk_pe_manager_id"), id);
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        this.generalDao.executeBySQL("delete from pri_received_manager where "
                + CommonUtils.madeSqlIn(idList, "fk_received_id"));
    }

    /**
     * 获取接待数据用于回显
     *
     * @param id
     */
    public PeReceived getReceivedData(String id) {
        PeReceived received = this.generalDao.getById(PeReceived.class, id);
        received.setReceivedUserList(this.generalDao.getBySQL("SELECT pm.id AS id" +
                " FROM pri_received_manager prm " +
                "INNER JOIN pe_received  pr ON pr.id = prm.fk_received_id " +
                "INNER JOIN pe_manager pm ON pm.id = prm.fk_pe_manager_id WHERE pr.id = ?", id));
        return received;
    }

    /**
     * 生成微信消息发送模板
     *
     * @param ids
     * @param title         消息标题
     * @param schoolName    学校
     * @param noticeUser    通知人
     * @param time          时间
     * @param noticeContent 通知内容
     * @param remark        消息备注
     * @param templateCode  模板code
     * @param messageType   消息类型
     * @return
     */
    private Map<String, Object> generalWeChatMessageTemplate(String ids, String title, String schoolName,
                                                             String noticeUser, String time,
                                                             String noticeContent, String remark,
                                                             String templateCode, String messageType) {
        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put(MessageConstants.PARAM_TEMPLATE_CODE, templateCode);
        templateParams.put(MessageConstants.WECHAT_MESSAGE_TYPE, messageType);
        templateParams.put("ids", ids);
        Map<String, Object> templateData = new HashMap<>();
        templateData.put(MessageConstants.WECHAT_MESSAGR_FIRST, title);
        templateData.put(MessageConstants.WECHAT_MESSAGR_KEYWORD1, schoolName);
        templateData.put(MessageConstants.WECHAT_MESSAGR_KEYWORD2, noticeUser);
        templateData.put(MessageConstants.WECHAT_MESSAGR_KEYWORD3, time);
        templateData.put(MessageConstants.WECHAT_MESSAGR_KEYWORD4, noticeContent);
        templateData.put(MessageConstants.WECHAT_MESSAGR_REMARK, remark);
        templateParams.put("templateData", templateData);
        return templateParams;
    }

    /**
     * 接待消息发送
     *
     * @param ids
     * @param receivedTime
     * @param noticeUser
     * @param receivedBy
     * @param participateUser
     */
    private void sendWeChatMessage(String ids, String receivedTime, String noticeUser, String receivedBy,
                                   String participateUser) {
        String noticeContent = "接待事由 :" + receivedBy + "\n" + "参加人员 ：" + participateUser;
        try {
            this.messageNoticeService.notice(this.generalWeChatMessageTemplate(
                    ids, "接待通知", SiteUtil.getSite().getName(), noticeUser,
                    receivedTime, noticeContent, "", MessageConstants.WECHAT_MESSAGE_CODE,
                    MessageConstants.WECHAT_TEMPLATE));
        } catch (Exception e) {
            logger.info("发送微信消息失败");
        }
    }
}