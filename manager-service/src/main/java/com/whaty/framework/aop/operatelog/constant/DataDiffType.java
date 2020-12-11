package com.whaty.framework.aop.operatelog.constant;

import java.util.Arrays;

/**
 * 数据差异类型
 *
 * @author weipengsen
 */
public enum DataDiffType {

    /**
     * 插入
     */
    INSERT("insert"),

    /**
     * 删除
     */
    DELETE("delete"),

    /**
     * 修改
     */
    CHANGE("change"),
    ;

    private String type;

    public static DataDiffType getTypeByType(String type) {
        DataDiffType diffType = Arrays.stream(values()).filter(e -> e.getType().equals(type))
                .findFirst().orElse(null);
        if (diffType == null) {
            throw new IllegalArgumentException();
        }
        return diffType;
    }

    DataDiffType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
