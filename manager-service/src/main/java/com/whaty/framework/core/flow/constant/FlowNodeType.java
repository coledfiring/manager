package com.whaty.framework.core.flow.constant;

import java.util.Arrays;

/**
 * 流程节点类型枚举
 *
 * @author weipengsen
 */
public enum FlowNodeType {

    /**
     * 菜单节点
     */
    CATEGORY("category"),
    /**
     * 按钮节点
     */
    MENU("menu"),
    /**
     * 自定义节点
     */
    CUSTOM("custom"),
    /**
     * 开始或结束节点
     */
    START_OR_END("startOrEnd"),
    ;

    private String type;

    FlowNodeType(String type) {
        this.type = type;
    }

    public static FlowNodeType getNodeType(String type) {
        return Arrays.stream(values()).filter(e -> type.equals(e.getType())).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getType() {
        return type;
    }
}
