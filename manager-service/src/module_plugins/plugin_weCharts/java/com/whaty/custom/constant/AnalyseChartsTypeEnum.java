package com.whaty.custom.constant;

/**
 * 自定义分析统计图类型
 * @author weipengsen
 */
public enum AnalyseChartsTypeEnum {
    /**
     * 折线图
     */
    lineValue("lineValue", "线型图"),
    /**
     * 饼状图
     */
    pieValue("pieValue", "饼状图"),
    /**
     * 柱状图
     */
    barValue("barValue", "柱状图");

    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;

    AnalyseChartsTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据code获得枚举
     * @param code
     * @return
     */
    public static AnalyseChartsTypeEnum getByCode(String code) {
        for (AnalyseChartsTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "AnalyseChartsTypeEnum{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
