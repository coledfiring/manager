package com.whaty.products.service.flow.helper;

import com.alibaba.fastjson.JSON;
import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.domain.bean.flow.CheckFlow;
import com.whaty.framework.config.util.PlatformConfigUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.notice.constant.NoticeServerPollConstant;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.flow.constant.CheckFlowType;
import com.whaty.products.service.flow.domain.flow.CheckFlowDTO;
import com.whaty.products.service.flow.strategy.check.AbstractCheckFlowStrategy;
import com.whaty.products.service.message.facade.SendMessageFacade;
import com.whaty.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 审批流程的辅助类
 *
 * @author weipengsen
 */
public class CheckFlowHelper {

    private CheckFlow checkFlow;

    private final static String NEED_CHECK_NOTICE_INFO = "您有需要审批的%s流程，名称为%s";

    private final static String CHECK_PASS_NOTICE_INFO = "%s流程已审批通过";

    private final static Logger logger = LoggerFactory.getLogger(CheckFlowHelper.class);

    public CheckFlowHelper(CheckFlow checkFlow) {
        this.checkFlow = checkFlow;
    }

    /**
     * 给当前节点的审批者发送通知
     * @throws Exception
     */
    public void noticeAuditor() throws Exception {
        String currentNodeId = checkFlow.getCurrentNode();
        AbstractCheckFlowStrategy strategy = CheckFlowType.getStrategy(checkFlow.getEnumConstByFlagFlowType().getCode());
        String itemName = strategy.getItemName(checkFlow.getItemId());

        CheckFlowDTO checkFlowDO = JSON.parseObject(this.checkFlow.getCheckFlowDetail().getCheckDetail(),
                CheckFlowDTO.class);
        List<String> auditor = checkFlowDO.getCheckFlowAuditors().get(currentNodeId).getAuditors().stream()
                .map(CheckFlowDTO.CheckFlowAuditorDTO::getId).collect(Collectors.toList());
        String noticeInfo = String
                .format(NEED_CHECK_NOTICE_INFO, this.checkFlow.getEnumConstByFlagFlowType().getName(), itemName);
        List<String> userIds;
        if ("2".equals(checkFlowDO.getCheckFlowAuditors().get(currentNodeId).getAuditorType())) {
            userIds = StaticBeanUtils.getGeneralDao()
                    .getBySQL("select fk_sso_user_id from pe_manager where " + CommonUtils.madeSqlIn(auditor, "id"));
        } else {
            userIds = StaticBeanUtils.getGeneralDao()
                    .getBySQL("select ss.id from pe_manager m inner join sso_user ss on ss.id = m.fk_sso_user_id" +
                            " LEFT JOIN pr_pri_manager_unit ppmu on ppmu.fk_sso_user_id=ss.id" +
                            " where " + CommonUtils.madeSqlIn(auditor, "ss.FK_ROLE_ID") +
                            (StringUtils.isBlank(checkFlow.getScopeId()) ?
                                    "" : " and (ppmu.id is null OR ppmu.fk_item_id='" + checkFlow.getScopeId() + "')") +
                            " GROUP BY ss.id ");
        }
        if (CollectionUtils.isNotEmpty(userIds)) {
            NoticeServerPollUtils.noticeAll(noticeInfo,
                    NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_CHECK_FLOW, userIds);
            if (PlatformConfigUtil.isWeChatSiteOpen(SiteUtil.getSiteCode())) {
                try {
                    new SendMessageFacade().noticeWeChatTemplate("noticeAuditor",
                            CommonUtils.join(userIds, CommonConstant.SPLIT_ID_SIGN, null), null,
                            Collections.singletonMap("checkFlowId", checkFlow.getId()));
                } catch (ServiceException e) {
                    if (logger.isInfoEnabled()) {
                        logger.info(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 通知所有抄送人，审批通过
     * @throws Exception
     */
    public void noticeCopyPersonPass() throws Exception {
        CheckFlowDTO checkFlowDO = JSON.parseObject(this.checkFlow.getCheckFlowDetail().getCheckDetail(),
                CheckFlowDTO.class);
        List<String> copyPerson = checkFlowDO.getCopyPersons();
        if (CollectionUtils.isNotEmpty(copyPerson)) {
            List<String> userIds = StaticBeanUtils.getGeneralDao()
                    .getBySQL("select fk_sso_user_id from pe_manager where " + CommonUtils.madeSqlIn(copyPerson, "id"));
            String noticeInfo = String.format(CHECK_PASS_NOTICE_INFO, this.checkFlow
                    .getEnumConstByFlagFlowType().getName());
            NoticeServerPollUtils.noticeAll(noticeInfo,
                    NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_CHECK_FLOW, userIds);
            if (PlatformConfigUtil.isWeChatSiteOpen(SiteUtil.getSiteCode())) {
                try {
                    Map<String, Object> extraParams = new HashMap<>(2);
                    extraParams.put("checkFlowId", checkFlow.getId());
                    extraParams.put("userId", UserUtils.getCurrentUserId());
                    new SendMessageFacade().noticeWeChatTemplate("noticeFlowPass",
                            CommonUtils.join(userIds, CommonConstant.SPLIT_ID_SIGN, null), null, extraParams);
                } catch (ServiceException e) {
                    if (logger.isInfoEnabled()) {
                        logger.info(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 通知申请人
     */
    public void noticeApplyUser(boolean isPass) throws Exception {
        String userId = this.checkFlow.getCreateBy().getId();
        NoticeServerPollUtils.noticeAll(String.format("您申请的%s已审批", this.checkFlow.getName()) +
                        (isPass ? "通过" : "不通过"),
                NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_CHECK_FLOW, Collections.singletonList(userId));
        try {
            Map<String, Object> extraParams = new HashMap<>(2);
            extraParams.put("checkFlowId", checkFlow.getId());
            extraParams.put("userId", UserUtils.getCurrentUserId());
            if (PlatformConfigUtil.isWeChatSiteOpen(SiteUtil.getSiteCode())) {
                new SendMessageFacade().noticeWeChatTemplate("noticeFlowPass", userId, null, extraParams);
            }
        } catch (ServiceException e) {
            if (logger.isInfoEnabled()) {
                logger.info(e.getMessage());
            }
        }
    }

    /**
     * 将当前节点可审批的审批人记录到数据库中
     */
    public void recordCanCheckAuditor() {
        String currentNodeId = checkFlow.getCurrentNode();
        if (StringUtils.isBlank(currentNodeId)) {
            this.checkFlow.setAuditors(null);
        } else {
            CheckFlowDTO checkFlowDO = JSON.parseObject(this.checkFlow.getCheckFlowDetail().getCheckDetail(),
                    CheckFlowDTO.class);
            List<String> auditor = checkFlowDO.getCheckFlowAuditors().get(currentNodeId).getAuditors().stream()
                    .filter(e -> e.getCheckOperate() == null)
                    .map(CheckFlowDTO.CheckFlowAuditorDTO::getId).collect(Collectors.toList());
            this.checkFlow.setAuditors(CommonUtils.join(auditor, ",", null));
        }
        StaticBeanUtils.getGeneralDao().save(this.checkFlow);
    }

    public CheckFlow getCheckFlow() {
        return checkFlow;
    }

    /**
     * 记录抄送人
     */
    public void recordCopyPerson() {
        CheckFlowDTO checkFlowDO = JSON.parseObject(this.checkFlow.getCheckFlowDetail().getCheckDetail(),
                CheckFlowDTO.class);
        List<String> copyPerson = checkFlowDO.getCopyPersons();
        if (CollectionUtils.isNotEmpty(copyPerson)) {
            this.checkFlow.setCopyPersons(CommonUtils.join(copyPerson, ",", null));
        }
        StaticBeanUtils.getGeneralDao().save(this.checkFlow);
    }
}
