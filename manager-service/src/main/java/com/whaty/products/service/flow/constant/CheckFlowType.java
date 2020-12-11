package com.whaty.products.service.flow.constant;

import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.products.service.flow.strategy.check.AbstractCheckFlowStrategy;

import java.util.Arrays;

/**
 * 审批流程类型枚举
 *
 * @author suoqiangqiang
 */
public enum CheckFlowType {
    /**
     * 社会项目
     */
    SOCIETY_TRAINING_ITEM("1", "社会项目", "societyTrainingItemCheckFlowStrategy"),

    /**
     * 合作项目
     */
    COOPERATION_TRAINING_ITEM("2", "合作项目", "cooperationTrainingItemCheckFlowStrategy"),

    /**
     * 项目结业
     */
    ITEM_COMPLETION("3", "项目结业", "itemCompletionCheckFlowStrategy"),

    /**
     * 费用结算
     */
    SETTLE_ACCOUNT("4", "费用结算", "settleAccountCheckFlowStrategy"),

    /**
     * 易耗品审批
     */
    CONSUMABLE_ITEM("5", "易耗品审批", "consumableItemCheckFlowStrategy"),

    /**
     * 用印管理审批
     */
    USE_SEAL_ITEM("6", "用印管理审批", "useSealItemCheckFlowStrategy"),;

    private String type;

    private String name;

    private String strategyBean;

    CheckFlowType(String type, String name, String strategyBean) {
        this.type = type;
        this.name = name;
        this.strategyBean = strategyBean;
    }

    /**
     * 获取类型名称
     *
     * @param type
     * @return
     */
    public static String getTypeName(String type) {
        return findCheckFlowType(type).getName();
    }

    /**
     * 寻找审核流程类型
     *
     * @param type
     * @return
     */
    private static CheckFlowType findCheckFlowType(String type) {
        CheckFlowType checkFlowType = Arrays.stream(values()).filter(e -> e.getType().equals(type))
                .findFirst().orElse(null);
        if (checkFlowType == null) {
            throw new IllegalArgumentException(String.format("type '%s' not found", type));
        }
        return checkFlowType;
    }

    /**
     * 获取策略对象
     *
     * @param type
     * @return
     */
    public static AbstractCheckFlowStrategy getStrategy(String type) {
        return (AbstractCheckFlowStrategy) SpringUtil.getBean(findCheckFlowType(type).getStrategyBean());
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getStrategyBean() {
        return strategyBean;
    }
}
