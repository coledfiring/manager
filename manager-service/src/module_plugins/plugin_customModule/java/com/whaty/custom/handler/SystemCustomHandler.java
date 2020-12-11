package com.whaty.custom.handler;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.SiteConstant;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.custom.constant.CustomConfigConstant;
import com.whaty.custom.context.CustomContext;
import com.whaty.utils.StaticBeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定制处理类
 *
 * @author weipengsen
 */
@Lazy
@Component("systemCustomHandler")
public class SystemCustomHandler {

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    /**
     * 移除上下文中map
     */
    public void removeMap() {
        CustomContext.removeMap();
    }

    /**
     * 将定制配置放入线程上下文
     *
     * @param url
     */
    public void putCustomConfigToContext(String url) {
        Map<String, String> customConfig = this.getCustomConfigFromCache(SiteUtil.getSiteCode(), url);
        if (customConfig == null) {
            customConfig = this.getCustomConfigFromDatabase(SiteUtil.getSiteCode(), url);
            this.putCustomConfigToCache(SiteUtil.getSiteCode(), url, customConfig);
        }
        CustomContext.putAll(customConfig);
    }

    /**
     * 将定制配置放入缓存
     *
     * @param siteCode
     * @param url
     * @param customConfig
     */
    private void putCustomConfigToCache(String siteCode, String url, Map<String, String> customConfig) {
        this.redisCacheService.putToCache(String.format(CustomConfigConstant.CUSTOM_CACHE_KEY, siteCode, url),
                customConfig);
    }

    /**
     * 删除缓存中的配置
     * @param siteCode
     * @param url
     */
    public void removeCustomConfigCache(String siteCode, String url) {
        this.redisCacheService.remove(String.format(CustomConfigConstant.CUSTOM_CACHE_KEY, siteCode, url));
    }

    /**
     * 从缓存中获取定制配置
     *
     * @param siteCode
     * @param url
     * @return
     */
    private Map<String, String> getCustomConfigFromCache(String siteCode, String url) {
        String key = String.format(CustomConfigConstant.CUSTOM_CACHE_KEY, siteCode, url);
        return this.redisCacheService.getFromCache(key);
    }

    /**
     * 从数据库中获取定制配置
     *
     * @param siteCode
     * @param url
     * @return
     */
    private Map<String, String> getCustomConfigFromDatabase(String siteCode, String url) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                       ");
        sql.append(" 	cu. CODE AS `key`,                                                        ");
        sql.append(" 	IFNULL(ss. VALUE, cu.default_value) AS `value`                            ");
        sql.append(" FROM                                                                         ");
        sql.append(" 	system_custom_config cu                                                   ");
        sql.append(" INNER JOIN system_custom_config_url url on url.fk_system_custom_id = cu.id   ");
        sql.append(" LEFT JOIN (                                                                  ");
        sql.append(" 	SELECT                                                                    ");
        sql.append(" 		ss.`value` AS `value`,                                                ");
        sql.append(" 		ss.fk_system_custom_id                                                ");
        sql.append(" 	FROM                                                                      ");
        sql.append(" 		system_site_custom_config ss                                          ");
        sql.append(" 	INNER JOIN pe_web_site site ON site.id = ss.fk_web_site_id                ");
        sql.append(" 	WHERE                                                                     ");
        sql.append(" 		site.`code` = '" + siteCode + "'                                      ");
        sql.append(" ) ss ON ss.fk_system_custom_id = cu.id                                       ");
        sql.append(" WHERE                                                                        ");
        sql.append(" 	url.url = '" + url + "'                                                   ");
        String currentCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<Object[]> customConfigList = StaticBeanUtils.getOpenGeneralDao().getBySQL(sql.toString());
        MasterSlaveRoutingDataSource.setDbType(currentCode);
        Map<String, String> customConfig = new HashMap<>(16);
        customConfigList.forEach(e -> customConfig.put((String) e[0], (String) e[1]));
        return customConfig;
    }
}
