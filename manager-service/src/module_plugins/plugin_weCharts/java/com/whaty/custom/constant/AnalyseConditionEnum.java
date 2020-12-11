package com.whaty.custom.constant;

/**
 * 自定义统计条件
 * @author weipengsen
 */
public enum AnalyseConditionEnum {
    /**
     * 单选
     */
    multiSelect("multiSelect", "多选"),
    /**
     * 多选
     */
    singleSelect("singleSelect", "单选"),
    /**
     * 文本框
     */
    input("input", "文本框");
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;

    AnalyseConditionEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获得枚举
     * @param code
     * @return
     */
    public static AnalyseConditionEnum getByCode(String code) {
        for (AnalyseConditionEnum condition : values()) {
            if (condition.getCode().equals(code)) {
                return condition;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AnalyseConditionEnum{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
