package com.whaty.products.service.flow.domain.flow;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.flow.CheckFlow;
import com.whaty.domain.bean.flow.CheckFlowRecord;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.flow.constant.CheckFlowType;
import com.whaty.products.service.flow.domain.CustomCheckFlowParams;
import com.whaty.products.service.flow.helper.CheckFlowHelper;
import com.whaty.products.service.flow.strategy.check.AbstractCheckFlowStrategy;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 审核流程数据库存储结构
 *
 * @author weipengsen
 */
@Data
@NoArgsConstructor
public class CheckFlowDTO implements Serializable {

    private static final long serialVersionUID = -985617662940216002L;

    private String id;

    /**
     * 审核节点集合
     */
    private LinkedHashMap<String, CheckFlowAuditorNodeDTO> checkFlowAuditors;
    /**
     * 抄送人集合
     */
    private List<String> copyPersons;

    public CheckFlowDTO(String id) {
        this.id = id;
    }

    public CheckFlowDTO(LinkedHashMap<String, CheckFlowAuditorNodeDTO> checkFlowAuditors, List<String> copyPersons) {
        this.checkFlowAuditors = checkFlowAuditors;
        this.copyPersons = copyPersons;
    }

    /**
     * 获取下一个审核节点
     * @param currentNode
     * @return
     */
    public String nextNode(String currentNode) {
        Iterator<Map.Entry<String, CheckFlowAuditorNodeDTO>> nodeIterator = checkFlowAuditors.entrySet().iterator();
        while(nodeIterator.hasNext()) {
            if (currentNode.equals(nodeIterator.next().getKey()) && nodeIterator.hasNext()) {
                return nodeIterator.next().getKey();
            }
        }
        return null;
    }

    /**
     * 通过当前节点
     * @param currentNode
     * @param note
     * @throws Exception
     */
    public void passCurrentNode(String currentNode, String note) throws Exception {
        this.changeCurrentNodeStatus(currentNode, note, true);
    }

    /**
     * 驳回当前节点
     * @param currentNode
     * @param note
     * @throws Exception
     */
    public void rejectCurrentNode(String currentNode, String note) throws Exception {
        this.changeCurrentNodeStatus(currentNode, note, false);
    }

    /**
     * 更改当前节点状态
     * @param currentNode
     * @param note
     * @param isPass
     */
    private void changeCurrentNodeStatus(String currentNode, String note, boolean isPass) throws Exception {
        String checkOperate = isPass ? "1" : "2";
        // 拿出配置
        CheckFlow checkFlow = StaticBeanUtils.getGeneralDao().getById(CheckFlow.class, this.id);
        CheckFlowDTO checkFlowDO = JSON.parseObject(checkFlow.getCheckFlowDetail().getCheckDetail(), CheckFlowDTO.class);
        // 找到当前审核节点
        CheckFlowDTO.CheckFlowAuditorNodeDTO auditorNodeDO = checkFlowDO.getCheckFlowAuditors()
                .get(checkFlow.getCurrentNode());
        // 判断是否可以审核
        CheckFlowDTO.CheckFlowAuditorDTO checkFlowAuditorDO = auditorNodeDO
                .getCurrentAuditor(UserUtils.getCurrentManager().getId());
        if (checkFlowAuditorDO == null) {
            throw new ServiceException("您不在当前节点的审批人中");
        }
        if (checkFlowAuditorDO.getCheckOperate() != null) {
            throw new ServiceException("您已审批过此节点");
        }
        // 设置对应审核人的状态
        checkFlowAuditorDO.setCheckedDate(com.whaty.util.CommonUtils.changeDateToString(new Date(),
                CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR));
        checkFlowAuditorDO.setCheckOperate(checkOperate);
        checkFlowAuditorDO.setNote(note);

        // 判断所有节点是否通过了
        CheckFlowHelper helper = new CheckFlowHelper(checkFlow);
        AbstractCheckFlowStrategy strategy = CheckFlowType
                .getStrategy(checkFlow.getEnumConstByFlagFlowType().getCode());
        if (isPass && auditorNodeDO.isCheckPass()) {
            auditorNodeDO.setApplyStatus("2");
            auditorNodeDO.setCheckedDate(com.whaty.util.CommonUtils.changeDateToString(new Date(),
                    CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR));
            auditorNodeDO.setIsChecked(true);
            auditorNodeDO.setCheckedDate(com.whaty.util.CommonUtils.changeDateToString(new Date(),
                    CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR));

            String nextNode = checkFlowDO.nextNode(currentNode);
            if (nextNode == null) {
                // 最后的节点就更改项目状态为审核，抄送所有抄送人
                checkFlow.setCurrentNode(null);
                checkFlow.setCompleteDate(new Date());
                checkFlow.setEnumConstByFlagCheckStatus(StaticBeanUtils.getGeneralDao()
                        .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_STATUS, "2"));
                strategy.passCheck(checkFlow.getItemId());
                StaticBeanUtils.getGeneralDao().save(checkFlow);
                StaticBeanUtils.getGeneralDao().flush();
                helper.recordCopyPerson();
                helper.noticeCopyPersonPass();
                helper.noticeApplyUser(true);
            } else {
                // 不是最后节点就移动当前节点指针
                checkFlow.setCurrentNode(nextNode);
                helper.noticeAuditor();
            }
        } else if (!isPass) {
            auditorNodeDO.setApplyStatus("3");
            auditorNodeDO.setCheckedDate(com.whaty.util.CommonUtils.changeDateToString(new Date(),
                    CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR));
            auditorNodeDO.setIsChecked(true);
            auditorNodeDO.setNote(note);
            auditorNodeDO.setCheckedDate(com.whaty.util.CommonUtils.changeDateToString(new Date(),
                    CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR));

            checkFlow.setCurrentNode(null);
            checkFlow.setCompleteDate(new Date());
            checkFlow.setEnumConstByFlagCheckStatus(StaticBeanUtils.getGeneralDao()
                    .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_STATUS, "3"));
            strategy.noPassCheck(checkFlow.getItemId());
            StaticBeanUtils.getGeneralDao().save(checkFlow);
            StaticBeanUtils.getGeneralDao().flush();
            helper.noticeApplyUser(false);
        }
        checkFlow.getCheckFlowDetail().setCheckDetail(JSON.toJSONString(checkFlowDO));
        helper.recordCanCheckAuditor();
        // 增加发起记录
        EnumConst operateType = StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_OPERATE, checkOperate);
        CheckFlowRecord record = new CheckFlowRecord(checkFlow.getEnumConstByFlagFlowType(),
                UserUtils.getCurrentManager(), operateType, strategy.getItemName(checkFlow.getItemId()), checkFlow);
        StaticBeanUtils.getGeneralDao().save(record);
    }

    /**
     * 撤回申请
     */
    public void cancelCheck() {
        CheckFlow checkFlow = StaticBeanUtils.getGeneralDao().getById(CheckFlow.class, this.id);
        if (!checkFlow.getCreateBy().getId().equals(UserUtils.getCurrentUserId())) {
            throw new ServiceException("没有撤回权限");
        }
        AbstractCheckFlowStrategy strategy = CheckFlowType
                .getStrategy(checkFlow.getEnumConstByFlagFlowType().getCode());
        EnumConst isCancel = StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_STATUS, "4");
        checkFlow.setEnumConstByFlagCheckStatus(isCancel);
        checkFlow.setCurrentNode(null);
        checkFlow.setCompleteDate(new Date());
        strategy.cancelCheck(checkFlow.getItemId());
        StaticBeanUtils.getGeneralDao().save(checkFlow);
        StaticBeanUtils.getGeneralDao().flush();

        EnumConst operateType = StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_OPERATE, "4");
        CheckFlowRecord record = new CheckFlowRecord(checkFlow.getEnumConstByFlagFlowType(),
                UserUtils.getCurrentManager(), operateType, strategy.getItemName(checkFlow.getItemId()), checkFlow);
        StaticBeanUtils.getGeneralDao().save(record);
    }

    /**
     * 写入自定义节点
     * @param origin
     */
    public void putCustomNode(CustomCheckFlowParams.CustomCheckFlowNode origin) {
        Optional.ofNullable(this.checkFlowAuditors.get(origin.getId()))
                .ifPresent(e -> e.updateByCustomConfig(origin));
    }

    /**
     * 过滤空的审批人
     */
    public void filterEmptyAuditor() {
        this.checkFlowAuditors = this.checkFlowAuditors.entrySet().stream()
                .filter(e -> Objects.nonNull(e.getValue()))
                .filter(e -> e.getValue().isNotEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (u, v) -> {throw new IllegalStateException(String.format("Duplicate key %s", u));},
                        LinkedHashMap::new));
    }

    @Data
    public static class CheckFlowAuditorNodeDTO implements Serializable {

        private static final long serialVersionUID = -3262681852649080290L;
        /**
         * 节点id
         */
        private String id;
        /**
         * 审核人集合
         */
        private List<CheckFlowAuditorDTO> auditors;
        /**
         * 审核类型，或签、会签
         */
        private String checkType;
        /**
         * 审核人类型，用户、角色
         */
        private String auditorType;
        /**
         * 此节点是否已被审核
         */
        private Boolean isChecked;
        /**
         * 此节点审核的状态
         */
        private String applyStatus;
        /**
         * 此节点被审核的时间
         */
        private String checkedDate;
        /**
         * 节点类型
         */
        private String nodeType;
        /**
         * 审核意见
         */
        private String note;

        public CheckFlowAuditorNodeDTO() {
        }

        public CheckFlowAuditorNodeDTO(String id, List<String> auditors, String checkType,
                                       String auditorType, String nodeType) {
            this.id = id;
            this.auditors = Optional.ofNullable(auditors)
                    .map(e -> e.stream().map(CheckFlowAuditorDTO::new).collect(Collectors.toList()))
                    .orElse(null);
            this.checkType = checkType;
            this.auditorType = auditorType;
            this.nodeType = nodeType;
            this.applyStatus = StaticBeanUtils.getGeneralDao()
                    .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_STATUS, "1").getCode();
            this.isChecked = false;
        }

        /**
         * 使用自定义配置更新
         * @param origin
         */
        public void updateByCustomConfig(CustomCheckFlowParams.CustomCheckFlowNode origin) {
            if ("1".equals(this.nodeType)) {
                throw new ParameterIllegalException();
            }
            this.auditors = origin.getAuditors().stream().map(CheckFlowAuditorDTO::new).collect(Collectors.toList());
            this.checkType = origin.getCheckType();
            this.auditorType = origin.getAuditorType();
        }

        /**
         * 检查是否通过
         *
         * @return
         */
        public boolean isCheckPass() {
            return "2".equals(this.checkType)
                    || this.auditors.stream().filter(e -> e.getCheckOperate() == null).count() <= 0;
        }

        /**
         * 检查是否可以审核
         * @param managerId
         * @return
         */
        public boolean canCheck(String managerId) {
            CheckFlowAuditorDTO checkFlowAuditorDO = this.getCurrentAuditor(managerId);
            return checkFlowAuditorDO != null && checkFlowAuditorDO.getCheckOperate() == null;
        }

        /**
         * 获取当前用户对应的审核人
         * @param managerId
         * @return
         */
        public CheckFlowAuditorDTO getCurrentAuditor(String managerId) {
            if ("2".equals(this.getAuditorType())) {
                return this.auditors.stream().filter(e -> e.getId().equals(managerId)).findFirst().orElse(null);
            } else {
                List<String> managerList = StaticBeanUtils.getGeneralDao()
                        .getBySQL("SELECT m.id AS id FROM pe_manager m " +
                                "INNER JOIN sso_user ss ON ss.id = m.fk_sso_user_id " +
                                "INNER JOIN enum_const ac ON ac.id = m.flag_active " +
                                        "WHERE ss.FK_ROLE_ID = ? AND ac.code = '1'",
                                this.auditors.get(0).getId());
                if (managerList.contains(managerId)) {
                    return this.auditors.get(0);
                }
            }
            return null;
        }

        public boolean isNotEmpty() {
            return CollectionUtils.isNotEmpty(this.auditors);
        }
    }

    @Data
    @NoArgsConstructor
    public static class CheckFlowAuditorDTO implements Serializable {

        private static final long serialVersionUID = 5015712242952952167L;
        /**
         * 审核人id
         */
        private String id;
        /**
         * 审核时间
         */
        private String checkedDate;
        /**
         * 审核操作，通过、驳回
         */
        private String checkOperate;
        /**
         * 意见
         */
        private String note;

        public CheckFlowAuditorDTO(String id) {
            this.id = id;
        }

    }

}
