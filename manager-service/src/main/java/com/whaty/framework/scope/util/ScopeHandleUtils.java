package com.whaty.framework.scope.util;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.scope.constants.ScopeEnum;
import com.whaty.util.SQLHandleUtils;
import com.whaty.utils.StaticBeanUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 横向权限工具类
 *
 * @author weipengsen
 */
public class ScopeHandleUtils {

    private final static String SCOPE_CACHE_KEY = "scope_cache_%s_%s";

    /**
     * 需要更新的单位权限redis key  根据站点存pe_manage的ssouserid 列表
     */
    public final static String PEUNIT_SCOPE_UPDATE_CACHE_KEY = "peunit_scope_update_cache_%s";

    private final static BiFunction<String, String, String> SCOPE_CACHE_GENERATOR =
            (userId, scopeType) -> String.format(SCOPE_CACHE_KEY, userId, scopeType);

    public final static Function<String, String> PEUNIT_SCOPE_UPDATE_CACHE_GENERATOR =
            (siteCode) -> String.format(PEUNIT_SCOPE_UPDATE_CACHE_KEY, siteCode);

    public final static int TIME_OUT = 24 * 60 * 60;

    /**
     * 清除单位权限缓存(用于处理修改单位权限的情况)
     *
     * @param userId
     * @param scopeEnum
     */
    private static void clearPeUnitCache(String userId, ScopeEnum scopeEnum) {
        Set<String> managers = StaticBeanUtils.getRedisCacheService().getFromCache(
                PEUNIT_SCOPE_UPDATE_CACHE_GENERATOR.apply(SiteUtil.getSiteCode()));
        if (managers != null && managers.remove(userId)) {
            removeScopeCache(userId, scopeEnum);
            StaticBeanUtils.getRedisCacheService().putToCache(
                    ScopeHandleUtils.PEUNIT_SCOPE_UPDATE_CACHE_GENERATOR.apply(SiteUtil.getSiteCode()),
                    managers, ScopeHandleUtils.TIME_OUT);
        }
    }

    /**
     * 根据用户id获得横向权限ids
     *
     * @param userId
     * @return
     */
    public static List<String> getScopeIdsByTableAlias(String alias, String userId) {
        ScopeEnum scopeEnum = ScopeEnum.getScopeByTableAlias(alias);
        if (ScopeEnum.peUnit.equals(scopeEnum)) {
            clearPeUnitCache(userId, scopeEnum);
        }
        List<String> scopeIds = getScopeIdsFromCache(userId, scopeEnum.getScopeType());
        if (scopeIds == null) {
            scopeIds = getScopeIdsFromDataBase(userId, scopeEnum);
            putScopeIdsToCache(userId, scopeEnum.getScopeType(), scopeIds);
        }
        return scopeIds;
    }

    /**
     * 通过beanAlias获取横向权限id
     *
     * @param alias
     * @param userId
     * @return
     */
    public static List<String> getScopeIdsByBeanAlias(String alias, String userId) {
        ScopeEnum scopeEnum = ScopeEnum.getScopeByBeanAlias(alias);
        if (ScopeEnum.peUnit.equals(scopeEnum)) {
            clearPeUnitCache(userId, scopeEnum);
        }
        List<String> scopeIds = getScopeIdsFromCache(userId, scopeEnum.getScopeType());
        if (scopeIds == null) {
            scopeIds = getScopeIdsFromDataBase(userId, scopeEnum);
            putScopeIdsToCache(userId, scopeEnum.getScopeType(), scopeIds);
        }
        return scopeIds;
    }

    /**
     * 从当前session中拿出指定的横向权限id
     *
     * @param scopeType
     * @return
     */
    private static List<String> getScopeIdsFromCache(String userId, String scopeType) {
        return StaticBeanUtils.getRedisCacheService().getFromCache(SCOPE_CACHE_GENERATOR.apply(userId, scopeType));
    }

    /**
     * 向当前session中插入指定的横向权限id
     *
     * @param userId
     * @param scopeType
     * @param scopeIds
     * @return
     */
    private static void putScopeIdsToCache(String userId, String scopeType, List<String> scopeIds) {
        StaticBeanUtils.getRedisCacheService()
                .putToCache(SCOPE_CACHE_GENERATOR.apply(userId, scopeType), scopeIds, TIME_OUT);
    }

    /**
     * 从数据库中拿出指定的user,横向权限类型的scopeIds
     *
     * @param userId
     * @param scopeEnum
     */
    private static List<String> getScopeIdsFromDataBase(String userId, ScopeEnum scopeEnum) {
        return scopeEnum.getScopeIdsFromDataBase().apply(userId);
    }

    /**
     * 处理sql中的横向权限占位符
     *
     * @return
     */
    public static String handleScopeSignOfSql(String sql, String userId) {
        Map<String, Object> scopeParams = new HashMap<>(4);
        if (!SiteConstant.SITE_CODE_CONTROL.equals(MasterSlaveRoutingDataSource.getDbType())) {
            Arrays.stream(ScopeEnum.values())
                    .forEach(e -> scopeParams.put(e.getTableAlias(),
                            getScopeIdsByTableAlias(e.getTableAlias(), userId)));
        }
        return SQLHandleUtils.replaceSignUseParams(sql, scopeParams);
    }

    /**
     * 更新横向权限
     * @param userId
     */
    public static void updateScopeCache(String userId) {
        Arrays.stream(ScopeEnum.values()).forEach(e -> updateScopeCache(userId, e));
    }

    /**
     * 更新横向权限
     * @param userId
     * @param scopeEnum
     */
    public static void updateScopeCache(String userId, ScopeEnum scopeEnum) {
        List<String> scopeIds = scopeEnum.getScopeIdsFromDataBase().apply(userId);
        putScopeIdsToCache(userId, scopeEnum.getScopeType(), scopeIds);
    }

    /**
     * 移除横向权限缓存
     *
     * @param userId
     * @param scopeEnum
     */
    public static void removeScopeCache(String userId, ScopeEnum scopeEnum) {
        StaticBeanUtils.getRedisCacheService().remove(SCOPE_CACHE_GENERATOR.apply(userId, scopeEnum.getScopeType()));
    }

}
