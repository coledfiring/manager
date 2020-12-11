package com.whaty.products.service.oltrain.enroll.domain;

import java.util.Arrays;

/**
 * 项类型
 *
 * @author suoqiangqiang
 */
public enum OlColumnType {

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

    ;

    private String code;

    private String type;


    OlColumnType(String code, String type) {
        this.code = code;
        this.type = type;
    }

    public static OlColumnType getType(String code) {
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
