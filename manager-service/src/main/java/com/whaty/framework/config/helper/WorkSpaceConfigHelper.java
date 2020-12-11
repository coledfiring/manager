package com.whaty.framework.config.helper;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.BasicApplicationContext;
import com.whaty.framework.config.domain.WorkSpaceConfig;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 工作室配置辅助类
 *
 * @author weipengsen
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component("workSpaceConfigHelper")
public class WorkSpaceConfigHelper extends AbstractVersionConfigHelper<WorkSpaceConfig> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = "redisCacheService")
    private RedisCacheService redisCacheService;

    private static final String CACHE_KEY = "basicCache::workSpace";

    @Override
    public void loadConfig() {
        BasicApplicationContext.WORKSPACE_CONFIG_MAP = this.listConfigs();
    }

    @Override
    public GeneralDao getGeneralDao() {
        return this.generalDao;
    }

    @Override
    public String getConfigTypeCode() {
        return "6";
    }

    @Override
    public Class<WorkSpaceConfig> getConfigClass() {
        return WorkSpaceConfig.class;
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
