package com.whaty.products.service.enroll.domain;

import java.util.Arrays;

/**
 * 项类型
 *
 * @author weipengsen
 */
public enum ColumnType {

    /**
     * 文本
     */
    TEXT("1", "text"),

    /**
     * 下拉
     */
    SELECT("2", "select"),

    /**
     * 图片
     */
    PICTURE("4", "picture"),

    /**
     * 日期（年月日）
     */
    DATE("5", "date"),
    ;

    private String code;

    private String type;


    ColumnType(String code, String type) {
        this.code = code;
        this.type = type;
    }

    public static ColumnType getType(String code) {
        return Arrays.stream(values()).filter(e -> e.getCode().equals(code))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

}
