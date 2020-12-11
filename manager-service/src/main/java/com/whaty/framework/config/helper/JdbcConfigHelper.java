package com.whaty.framework.config.helper;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.BasicApplicationContext;
import com.whaty.framework.config.domain.JdbcConfig;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.jdbc.TycjMasterSlaveRoutingDataSource;
import com.whaty.framework.httpClient.helper.HttpClientHelper;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * jdbc配置辅助类
 *
 * @author weipengsen
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component("jdbcConfigHelper")
public class JdbcConfigHelper extends AbstractVersionConfigHelper<JdbcConfig>
        implements ApplicationContextAware, ErrorRetryManager {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = "myDataSource")
    private TycjMasterSlaveRoutingDataSource routingDataSource;

    private ConfigurableApplicationContext configurableApplicationContext;

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    private static final String CACHE_KEY = "basicCache::jdbc";

    private final static Logger logger = LoggerFactory.getLogger(JdbcConfig.class);

    @Override
    public void loadConfig() {
        BasicApplicationContext.JDBC_CONFIG_MAP = this.listConfigs();
        try {
            this.workWithRetry();
        } catch (Exception e) {
            String json = "{'msgtype':'text', 'text': {'content':'部署项目-上海交通大学培训[管理端]更新JDBC配置失败'}, " +
                    "'at': {'atMobiles': ['18158143856']}}";
            try {
                new HttpClientHelper().doPostJSON(CommonConstant.DING_DING_ROBOT_URL, json);
            } catch (IOException e1) {
                logger.error("send ding ding message error", e1);
            }
            throw e;
        }
    }

    @Override
    public GeneralDao getGeneralDao() {
        return this.generalDao;
    }

    @Override
    public String getConfigTypeCode() {
        return "1";
    }

    @Override
    public Class<JdbcConfig> getConfigClass() {
        return JdbcConfig.class;
    }

    /**
     * 使用配置
     */
    private void useConfig() {
        this.routingDataSource.getJdbcConfigMap().forEach((k, v) -> {
            try {
                v.destroyDataSource();
                ((DefaultSingletonBeanRegistry) this.configurableApplicationContext.getBeanFactory())
                        .destroySingleton(k);
            } catch (Exception e) {
                throw new UncheckException(e);
            }
        });
        if (MapUtils.isNotEmpty(BasicApplicationContext.JDBC_CONFIG_MAP)) {
            Map<Object, Object> dataSourceMap = BasicApplicationContext.JDBC_CONFIG_MAP.entrySet()
                    .stream().collect(Collectors.toMap(Map.Entry::getKey, e -> {
                        try {
                            DataSource dataSource = e.getValue().buildDataSource();
                            this.configurableApplicationContext.getBeanFactory().registerSingleton(e.getKey(), dataSource);
                            this.routingDataSource.getJdbcConfigMap().put(e.getKey(), e.getValue());
                            return dataSource;
                        } catch (Exception e1) {
                            logger.error(String.format("can't build dataSource '%s'", e.getKey()));
                            throw new UncheckException(e1);
                        }
                    }));
            this.routingDataSource.getDefaultDataSource().forEach(dataSourceMap::put);
            routingDataSource.setTargetDataSources(dataSourceMap);
            routingDataSource.afterPropertiesSet();
        }
    }

    @Override
    protected RedisCacheService getRedisCacheService() {
        return this.redisCacheService;
    }

    @Override
    protected String getCacheKey() {
        return CACHE_KEY;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void doWork() {
        this.useConfig();
    }
}
