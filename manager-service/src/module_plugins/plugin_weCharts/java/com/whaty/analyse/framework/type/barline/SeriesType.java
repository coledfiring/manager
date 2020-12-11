package com.whaty.analyse.framework.type.barline;

import java.util.Arrays;

/**
 * y轴显示类型
 *
 * @author weipengsen
 */
public enum SeriesType {

    /**
     * 折线
     */
    LINE("line", "折线"),

    /**
     * 柱状
     */
    BAR("bar", "柱状"),
    ;

    private String type;

    private String name;

    SeriesType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * 是否合法
     * @param type
     * @return
     */
    static boolean isValid(String type) {
        return Arrays.stream(values()).anyMatch(e -> e.type.equals(type));
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}