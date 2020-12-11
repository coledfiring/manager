package com.whaty.framework.config.helper;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.BasicApplicationContext;
import com.whaty.framework.config.domain.ExamSystemConfig;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 考试系统配置辅助类
 *
 * @author weipengsen
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component("examSystemConfigHelper")
public class ExamSystemConfigHelper extends AbstractVersionConfigHelper<ExamSystemConfig> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = "redisCacheService")
    private RedisCacheService redisCacheService;

    private static final String CACHE_KEY = "basicCache::examSystem";

    @Override
    public void loadConfig() {
        BasicApplicationContext.EXAM_SYSTEM_CONFIG_MAP = this.listConfigs();
    }

    @Override
    public GeneralDao getGeneralDao() {
        return this.generalDao;
    }

    @Override
    public String getConfigTypeCode() {
        return "5";
    }

    @Override
    public Class<ExamSystemConfig> getConfigClass() {
        return ExamSystemConfig.class;
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
