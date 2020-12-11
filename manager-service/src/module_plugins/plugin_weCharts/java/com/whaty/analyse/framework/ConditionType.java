package com.whaty.analyse.framework;

import java.util.Arrays;

/**
 * 条件类型
 *
 * @author weipengsen
 */
public enum ConditionType {

    /**
     * 日期范围
     */
    DATE_LIMIT("1", "dateLimit"),

    /**
     * 下拉框
     */
    SELECT("2", "select"),

    /**
     * 文本框
     */
    TEXT("3", "text"),

    /**
     * 多选下拉框
     */
    MULTI_SELECT("4", "multiSelect"),

    /**
     * 树下拉
     */
    TREE_SELECT("5", "treeSelect"),
    ;

    private String code;

    private String type;

    ConditionType(String code, String type) {
        this.code = code;
        this.type = type;
    }

    /**
     * 根据code获取类型
     * @return
     */
    public static ConditionType getTypeByCode(String type) {
        return Arrays.stream(values()).filter(e -> e.code.equals(type)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
