package com.whaty.analyse.framework;

import com.whaty.common.string.StringUtils;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.framework.sqlflow.SqlFlowHandler;
import com.whaty.util.SQLHandleUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 统计工具类
 *
 * @author weipengsen
 */
public class AnalyseUtils {

    /**
     * 处理执行sql
     * 替换占位符、增加横向权限
     *
     * @param sql
     * @param params
     * @return
     */
    public static String handleSql(String sql, Map<String, Object> params) {
        Map<String, Object> filterParams = Optional.ofNullable(params)
                .map(e -> e.entrySet().stream().filter(AnalyseUtils::isNotEmpty)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .orElse(null);
        sql = ScopeHandleUtils.handleScopeSignOfSql(sql.replace("${siteCode}", SiteUtil.getSiteCode())
                        .replace("${currentUserId}", UserUtils.getCurrentUserId()),
                UserUtils.getCurrentUserId());
        Optional.ofNullable(filterParams).ifPresent(p -> p.keySet().stream().filter(e -> e.endsWith("_dateLimit"))
                .filter(e -> filterParams.get(e) instanceof List)
                .forEach(e -> filterParams.computeIfPresent(e, (k, o) ->
                        ((List) o).get(0) + " 00:00:00," + ((List) o).get(1) + " 23:59:59")));
        sql = SQLHandleUtils.replaceSignUseParams(sql, filterParams, true);
        SqlFlowHandler handler = new SqlFlowHandler(sql, filterParams);
        try {
            handler.handle();
        } catch (Exception e) {
            throw new UncheckException(e);
        }
        return handler.getSql();
    }

    /**
     * 判断值不为空
     * @param entry
     * @return
     */
    private static boolean isNotEmpty(Map.Entry<String, Object> entry) {
        if (Objects.isNull(entry.getValue())) {
            return false;
        } else if (entry.getValue() instanceof Collection) {
            return CollectionUtils.isNotEmpty((Collection) entry.getValue());
        } else if (entry.getValue().getClass().isArray()) {
            return ArrayUtils.isNotEmpty((Object[]) entry.getValue());
        } else if (entry.getValue() instanceof String) {
            return StringUtils.isNotBlank((String) entry.getValue());
        } else {
            return Objects.nonNull(entry.getValue());
        }
    }

    /**
     * 符合切换站点
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> Supplier<T> composeSwitchSite(Supplier<T> supplier) {
        return () -> {
            String currentSite = MasterSlaveRoutingDataSource.getDbType();
            try {
                MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
                return supplier.get();
            } finally {
                MasterSlaveRoutingDataSource.setDbType(currentSite);
            }
        };
    }
}
