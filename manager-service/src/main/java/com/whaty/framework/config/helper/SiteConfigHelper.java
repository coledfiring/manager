package com.whaty.framework.config.helper;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.BasicApplicationContext;
import com.whaty.function.Functions;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * 站点配置辅助类
 *
 * @author weipengsen
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component("siteConfigHelper")
public class SiteConfigHelper extends AbstractVersionConfigSuperHelper {

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    private static final String CACHE_KEY = "basicCache::siteConfig";

    @Override
    protected RedisCacheService getRedisCacheService() {
        return this.redisCacheService;
    }

    @Override
    protected void updateNow() {
        loadConfig();
    }

    /**
     * 加载配置
     */
    @Override
    public void loadConfig() {
        DetachedCriteria dc = DetachedCriteria.forClass(PeWebSite.class)
                .add(Restrictions.eq("activeStatus", 1))
                .createCriteria("peWebSiteDetail", "peWebSiteDetail", DetachedCriteria.LEFT_JOIN);
        List<PeWebSite> siteList = this.generalDao.getList(dc);
        BasicApplicationContext.CODE_KEY_SITE_MAP = Optional.ofNullable(siteList)
                .map(e -> e.stream().collect(Functions.map(PeWebSite::getCode))).orElse(new HashMap<>(2));
        BasicApplicationContext.DOMAIN_KEY_SITE_MAP = Optional.ofNullable(siteList)
                .map(e -> e.stream().collect(Functions.map(PeWebSite::getDomain))).orElse(new HashMap<>(2));
    }

    @Override
    protected String getCacheKey() {
        return CACHE_KEY;
    }
}
