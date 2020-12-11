package com.whaty.products.service.superadmin.impl;

/**
 * 用户同步数据查询sql枚举
 *
 * @author suoqiangqiang
 */
public enum UserSyncDataSearchSqlEnum {

    MANAGER_SEARCH_SQL("manager", "%s md5(sso.login_id) as password FROM pe_manager user %s"),
    TEACHER_SEARCH_SQL("teacher", "%s md5(concat(sso.login_id,'@',sso.site_code)) as password FROM pe_teacher user %s"),
    STUDENT_SEARCH_SQL("student", "%s md5(user.mobile) as password FROM pe_student user %s");

    /**
     * sql前缀
     */
    private static final String SQL_PREFIX = " SELECT user.id as id,sso.login_id as loginId," +
            "user.true_name as trueName,sso.id as userId,sso.site_code as siteCode,";
    /**
     * sql后缀
     */
    private static final String SQL_SUFFIX = " INNER JOIN sso_user sso ON sso.id = user.fk_sso_user_id where";

    /**
     * 角色编码
     */
    private String code;
    /**
     * 查询sql
     */
    private String sql;

    UserSyncDataSearchSqlEnum(String code, String sql) {
        this.code = code;
        this.sql = sql;
    }

    /**
     * 根据code获得枚举
     *
     * @param code
     * @return
     */
    public static UserSyncDataSearchSqlEnum getByCode(String code) {
        for (UserSyncDataSearchSqlEnum sqlEnum : values()) {
            if (sqlEnum.getCode().equals(code)) {
                sqlEnum.setSql(String.format(sqlEnum.getSql(), SQL_PREFIX, SQL_SUFFIX));
                return sqlEnum;
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

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "UserSyncDataSearchSqlEnum{" +
                "code='" + code + '\'' +
                ", sql='" + sql + '\'' +
                '}';
    }
}
