package com.whaty.redisson.config;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Properties;

/**
 * redisson的配置类
 *
 * @author weipengsen
 */
@Configuration("redissonConfiguration")
public class RedissonConfiguration implements Serializable, InitializingBean {

    public final static Config REDISSON_CONFIG = new Config();

    private final static String REDIS_PROPERTIES_PATH = "redis.properties";

    private final static String REDIS_SENTINELS_KEY = "redis.sentinels";

    private final static String REDIS_MASTER_NAME = "master01";

    private final static String REDIS_SERVERS_DB = "redis.db";

    private final static String REDIS_SERVER_FORMAT = "redis://%s";

    private final static Logger logger = LoggerFactory.getLogger(RedissonConfiguration.class);

    private static final long serialVersionUID = -6557598395743122849L;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("starting redisson config");
        }
        Properties prop = new Properties();
        prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(REDIS_PROPERTIES_PATH));
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("redisson use model sentinels, load sentinels %s",
                    prop.getProperty(REDIS_SENTINELS_KEY)));
        }
        String[] sentinels = prop.getProperty(REDIS_SENTINELS_KEY).split(",");
        if (ArrayUtils.isEmpty(sentinels)) {
            throw new IllegalArgumentException("redisson init failure: not found redis sentinels config");
        }
        String dbConfig = prop.getProperty(REDIS_SERVERS_DB);
        if (StringUtils.isBlank(dbConfig)) {
            throw new IllegalArgumentException("redisson init failure: not found redis db config");
        }
        if (sentinels.length == 1) {
            this.loadSingleConfig(sentinels[0], Integer.parseInt(dbConfig));
        } else {
            this.loadSentinelsConfig(sentinels, Integer.parseInt(dbConfig));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("redisson load config success, [%s]");
        }
    }

    /**
     * 加载sentinels服务器配置
     * @param sentinels
     * @param db
     */
    private void loadSentinelsConfig(String[] sentinels, int db) {
        SentinelServersConfig config = REDISSON_CONFIG.useSentinelServers();
        config.setMasterName(REDIS_MASTER_NAME);
        Arrays.stream(sentinels).map(e -> String.format(REDIS_SERVER_FORMAT, e)).forEach(config::addSentinelAddress);
        config.setDatabase(db);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("redisson load the sentinels config [%s]", config));
        }
    }

    /**
     * 加载单服务器配置
     *
     * @param server
     * @param db
     */
    private void loadSingleConfig(String server, int db) {
        SingleServerConfig config = REDISSON_CONFIG.useSingleServer();
        config.setAddress(String.format(REDIS_SERVER_FORMAT, server));
        config.setDatabase(db);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("redisson load the single config [%s]", config));
        }
    }
}
