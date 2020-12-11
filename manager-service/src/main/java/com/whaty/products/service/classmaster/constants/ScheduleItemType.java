package com.whaty.products.service.classmaster.constants;

import java.util.Arrays;

/**
 * 日程项目类型
 *
 * @author weipengsen
 */
public enum ScheduleItemType {

    /**
     * 课程
     */
    COURSE("course"),

    /**
     * 活动
     */
    ACTIVITY("activity"),
    ;

    private String type;

    ScheduleItemType(String type) {
        this.type = type;
    }

    /**
     * 检查是否合法
     * @param type
     * @return
     */
    public static boolean checkValid(String type) {
        return Arrays.stream(values()).anyMatch(e -> e.type.equals(type));
    }

    public String getType() {
        return type;
    }
}
