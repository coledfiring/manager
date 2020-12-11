package com.whaty.products.service.common.constant;

/**
 * 班级级联项枚举
 * @author weipengsen
 */
public enum ClassCascadeItemEnum {

    site("site", "c.fk_site_id = '%s'"),

    grade("grade", "c.fk_grade_id = '%s'"),

    major("major", "c.fk_major_id = '%s'"),

    eduType("eduType", "c.fk_edutype_id = '%s'"),

    majorType("majorType", "c.flag_major_type = '%s'"),
    ;

    /**
     * 唯一key
     */
    private String key;
    /**
     * where语句
     */
    private String whereSql;

    ClassCascadeItemEnum(String key, String whereSql) {
        this.key = key;
        this.whereSql = whereSql;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWhereSql() {
        return whereSql;
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }
}
