package com.whaty.framework.scope.constants;

import com.whaty.framework.asserts.TycjAssert;
import com.whaty.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.function.Function;

/**
 * 横向权限枚举
 *
 * @author weipengsen
 */
public enum ScopeEnum {

    /**
     * 层级权限
     */
    peUnit("peUnit", "peUnit", "PeUnit",
            e -> String.format("select fk_item_id from pr_pri_manager_unit where fk_sso_user_id = '%s'", e),
            (e) -> {
                List<String> result = StaticBeanUtils.getOpenGeneralDao()
                        .getBySQL("SELECT DISTINCT fk_item_id FROM pr_pri_manager_unit " +
                                " WHERE FK_SSO_USER_ID = ? AND fk_item_id IS NOT NULL ", e);
                if (CollectionUtils.isEmpty(result)) {
                    return Collections.emptyList();
                }
                List<String> ids = new ArrayList<>(result.size() * 2);
                ids.addAll(result);
                List<String> childList = result;
                //获取权限子节点
                while (true) {
                    childList = StaticBeanUtils.getOpenGeneralDao().getBySQL(
                            "select id from pe_unit where " + CommonUtils.madeSqlIn(childList, "fk_parent_id"));
                    if (CollectionUtils.isEmpty(childList)) {
                        break;
                    }
                    ids.addAll(childList);
                }
                return ids;
            }
    ),
    ;

    private String tableAlias;

    private String scopeType;

    private String beanAlias;

    private Function<String, String> idSqlFunction;

    /***
     * 从数据库中获取ids列表
     */
    private Function<String, List<String>> scopeIdsFromDataBase;

    ScopeEnum(String tableAlias, String scopeType, String beanAlias, Function<String, String> idSqlFunction,
              Function<String, List<String>> scopeIdsFromDataBase) {
        this.tableAlias = tableAlias;
        this.scopeType = scopeType;
        this.beanAlias = beanAlias;
        this.idSqlFunction = idSqlFunction;
        this.scopeIdsFromDataBase = scopeIdsFromDataBase;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public String getScopeType() {
        return scopeType;
    }

    public String getBeanAlias() {
        return beanAlias;
    }

    public Function<String, String> getIdSqlFunction() {
        return idSqlFunction;
    }

    public Function<String, List<String>> getScopeIdsFromDataBase() {
        return scopeIdsFromDataBase;
    }

    public static boolean hasAlias(String alias) {
        return Arrays.stream(values())
                .filter(e -> e.getTableAlias().equalsIgnoreCase(alias)).anyMatch(Objects::nonNull);
    }

    public static ScopeEnum getScopeByTableAlias(String alias) {
        ScopeEnum scopeEnum = Arrays.stream(values())
                .filter(e -> e.getTableAlias().equalsIgnoreCase(alias)).findFirst().orElse(null);
        TycjAssert.isAllNotNull(scopeEnum);
        return scopeEnum;
    }

    public static boolean hasBean(String beanAlias) {
        return Arrays.stream(values())
                .filter(e -> e.getBeanAlias().equalsIgnoreCase(beanAlias)).anyMatch(Objects::nonNull);
    }

    public static ScopeEnum getScopeByBeanAlias(String alias) {
        ScopeEnum scopeEnum = Arrays.stream(values())
                .filter(e -> e.getBeanAlias().equalsIgnoreCase(alias)).findFirst().orElse(null);
        TycjAssert.isAllNotNull(scopeEnum);
        return scopeEnum;
    }
}
