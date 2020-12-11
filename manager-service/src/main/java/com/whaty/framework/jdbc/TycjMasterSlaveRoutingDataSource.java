package com.whaty.framework.jdbc;

import com.whaty.framework.config.domain.JdbcConfig;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 成教多数据源路由
 *
 * @author nifuxing
 */
public class TycjMasterSlaveRoutingDataSource extends MasterSlaveRoutingDataSource {

    private static Map<String, JdbcConfig> jdbcConfigMap = new HashMap<>(16);

    private static Map<String, DataSource> defaultDataSource = new HashMap<>();

    /**
     * 查看站点是否被支持
     * @param siteCode
     * @return
     */
    public static boolean support(String siteCode) {
        return jdbcConfigMap.containsKey(siteCode);
    }

    public Map<String, DataSource> getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(Map<String, DataSource> defaultDataSource) {
        TycjMasterSlaveRoutingDataSource.defaultDataSource = defaultDataSource;
    }

    public Map<String, JdbcConfig> getJdbcConfigMap() {
        return jdbcConfigMap;
    }

    public void setJdbcConfigMap(Map<String, JdbcConfig> jdbcConfigMap) {
        TycjMasterSlaveRoutingDataSource.jdbcConfigMap = jdbcConfigMap;
    }
}
