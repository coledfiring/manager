package com.whaty.products.service.flow.domain.config;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.flow.CheckFlow;
import com.whaty.domain.bean.flow.CheckFlowCopyPerson;
import com.whaty.domain.bean.flow.CheckFlowNode;
import com.whaty.domain.bean.flow.CheckFlowRecord;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.flow.constant.CheckFlowType;
import com.whaty.products.service.flow.domain.CustomCheckFlowParams;
import com.whaty.products.service.flow.helper.CheckFlowHelper;
import com.whaty.products.service.flow.strategy.check.AbstractCheckFlowStrategy;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.whaty.products.service.flow.builder.CheckFlowBuilder.newCheckFlow;

/**
 * 流程配置Vo
 *
 * @author weipengsen
 */
@Data
@NoArgsConstructor
public class CheckFlowConfig implements Serializable {

    private static final long serialVersionUID = 3373027771296988402L;

    @NotNull
    private String id;

    private String name;

    private String needCheck;

    private List<CheckFlowConfigNode> nodes;

    private List<CheckFlowConfigCopyPerson> copyPersons;

    public CheckFlowConfig(String id) {
        this.id = id;
    }

    /**
     * 构造整体对象
     * @return
     */
    public CheckFlowConfig build() {
        Map<String, Object> groupConfig = StaticBeanUtils.getGeneralDao()
                .getOneMapBySQL("select g.id as id, g.name as name, nc.code as needCheck from check_flow_group g " +
                        "inner join enum_const nc on nc.id = g.flag_need_check where g.id = ?", this.id);
        this.name = (String) groupConfig.get("name");
        this.needCheck = (String) groupConfig.get("needCheck");
        this.nodes = CheckFlowConfigNode.listNodes(this.id);
        this.copyPersons = CheckFlowConfigCopyPerson.listCopyPersons(this.id);
        return this;
    }

    /**
     * 增加第一个节点
     * @return
     */
    public CheckFlowNode addFirstNode() {
        return CheckFlowConfigNode.addFirstNode(this.id);
    }

    /**
     * 在节点后增加新节点
     * @param previousId
     * @return
     */
    public CheckFlowNode addNodeAfter(String previousId) {
        return CheckFlowConfigNode.addNodeAfter(this.id, previousId);
    }

    /**
     * 删除节点
     * @param nodeId
     */
    public void deleteNode(String nodeId) {
        CheckFlowConfigNode.deleteNode(this.id, nodeId);
    }

    /**
     * 是否激活
     * @return
     */
    public Boolean isActive() {
        return "1".equals(StaticBeanUtils.getGeneralDao().getOneBySQL("select ac.code from check_flow_group g " +
                "inner join enum_const ac on ac.id = g.flag_active where g.id = ?", this.id));
    }

    /**
     * 保存抄送人
     */
    public void saveCopyPersons() {
        String copyPersonId = StaticBeanUtils.getGeneralDao()
                .getOneBySQL("select fk_copy_person_detail_id from check_flow_group where id = ?", this.id);
        CheckFlowCopyPerson target = StringUtils.isBlank(copyPersonId) ? new CheckFlowCopyPerson()
                : StaticBeanUtils.getGeneralDao().getById(CheckFlowCopyPerson.class, copyPersonId);
        if (CollectionUtils.isNotEmpty(this.copyPersons)) {
            target.setCopyPerson(CommonUtils.join(this.copyPersons.stream()
                    .map(CheckFlowConfigCopyPerson::getId).collect(Collectors.toList()), ",", ""));
        } else {
            target.setCopyPerson("");
        }
        target.setUpdateBy(UserUtils.getCurrentUser());
        target.setUpdateDate(new Date());
        StaticBeanUtils.getGeneralDao().save(target);
        StaticBeanUtils.getGeneralDao().flush();
        StaticBeanUtils.getGeneralDao()
                .executeBySQL("update check_flow_group set fk_copy_person_detail_id = ? where id = ?",
                        target.getId(), this.id);
    }

    /**
     * 发起申请
     * @param itemId
     * @param type
     * @throws Exception
     */
    public void apply(String itemId, String type) throws Exception {
        TycjParameterAssert.isAllNotBlank(itemId, type);
        this.applyCheckFlowWithFunction(itemId, type, strategy -> newCheckFlow()
                .withFlowGroup(this.id).withFlowType(type).withItemId(itemId)
                .withName(strategy.getItemName(itemId))
                .withScopeId(strategy.getScopeId(itemId))
                .build());
    }

    /**
     * 基于自定义的审批发起
     *
     * @param params
     * @throws Exception
     */
    public void applyWithCustom(CustomCheckFlowParams params) throws Exception {
        TycjParameterAssert.validatePass(params);
        this.applyCheckFlowWithFunction(params.getItemId(), params.getType(), strategy -> newCheckFlow()
                .withFlowGroup(this.id).withCustomConfig(params)
                .withFlowType(params.getType()).withItemId(params.getItemId())
                .withName(strategy.getItemName(params.getItemId()))
                .withScopeId(strategy.getScopeId(params.getItemId()))
                .build());
    }

    /**
     * 基于生成器的审批发起
     * @param itemId
     * @param type
     * @param checkFlowGenerator
     */
    private void applyCheckFlowWithFunction(String itemId, String type,
                                            Function<AbstractCheckFlowStrategy, CheckFlow> checkFlowGenerator) throws Exception {
        AbstractCheckFlowStrategy strategy = CheckFlowType.getStrategy(type);
        // 判断是否已经审核
        if (!strategy.checkCanApply(itemId)) {
            throw new ServiceException("只有未申请和审批不通过的数据才可以发起申请");
        }
        // 将其他历史流程都设为无效
        EnumConst active = StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, "0");
        StaticBeanUtils.getGeneralDao().executeBySQL("update check_flow cf " +
                " inner join enum_const ty on ty.id = cf.flag_flow_type" +
                " set flag_active = ? where cf.fk_item_id = ? and ty.code = ?", active.getId(), itemId, type);
        CheckFlow checkFlow = checkFlowGenerator.apply(strategy);
        StaticBeanUtils.getGeneralDao().save(checkFlow.getCheckFlowDetail());
        StaticBeanUtils.getGeneralDao().save(checkFlow);
        // 增加发起记录
        EnumConst operateType = StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_OPERATE, "3");
        CheckFlowRecord record = new CheckFlowRecord(checkFlow.getEnumConstByFlagFlowType(),
                UserUtils.getCurrentManager(), operateType, strategy.getItemName(itemId), checkFlow);
        StaticBeanUtils.getGeneralDao().save(record);
        CheckFlowHelper helper = new CheckFlowHelper(checkFlow);
        if ("1".equals(checkFlow.getEnumConstByFlagNeedCheck().getCode())) {
            // 设置审核状态
            strategy.applyCheck(itemId);
            helper.recordCanCheckAuditor();
            helper.noticeAuditor();
        } else {
            checkFlow.setCompleteDate(new Date());
            checkFlow.setEnumConstByFlagCheckStatus(StaticBeanUtils.getGeneralDao()
                    .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_STATUS, "2"));
            strategy.passCheck(checkFlow.getItemId());
            StaticBeanUtils.getGeneralDao().save(checkFlow);
            StaticBeanUtils.getGeneralDao().flush();
            helper.recordCopyPerson();
            helper.noticeCopyPersonPass();
            helper.noticeApplyUser(true);
        }
    }
}
