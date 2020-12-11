package com.whaty.framework.config.helper;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.BasicApplicationContext;
import com.whaty.framework.config.domain.PlatformConfig;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 平台配置辅助类
 *
 * @author weipengsen
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component("platformConfigHelper")
public class PlatformConfigHelper extends AbstractVersionConfigHelper<PlatformConfig> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    private static final String CACHE_KEY = "basicCache::platform";

    @Override
    public void loadConfig() {
        BasicApplicationContext.PLATFORM_CONFIG_MAP = this.listConfigs();
    }

    @Override
    public GeneralDao getGeneralDao() {
        return this.generalDao;
    }

    @Override
    public String getConfigTypeCode() {
        return "3";
    }

    @Override
    public Class<PlatformConfig> getConfigClass() {
        return PlatformConfig.class;
    }

    @Override
    protected RedisCacheService getRedisCacheService() {
        return this.redisCacheService;
    }

    @Override
    protected String getCacheKey() {
        return CACHE_KEY;
    }
}
