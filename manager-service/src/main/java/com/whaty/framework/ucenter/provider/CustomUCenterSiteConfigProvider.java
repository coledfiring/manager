package com.whaty.framework.ucenter.provider;

import com.whaty.framework.config.util.SiteUtil;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.ucenter.oauth2.client.config.UCenterSiteConfig;
import com.whaty.ucenter.oauth2.client.context.DomainHolder;
import com.whaty.ucenter.oauth2.client.context.UCenterSiteConfigProvider;

/**
 * 用户中心配置提供者
 *
 * @author hanshichao
 */
public class CustomUCenterSiteConfigProvider implements UCenterSiteConfigProvider {

    @Override
    public UCenterSiteConfig getUCenterSiteConfig(String uCenterSiteCode) {
        PeWebSite site = SiteUtil.getSiteByDomain(DomainHolder.getDomain());
        if (site == null) {
            return null;
        }
        UCenterSiteConfig uCenterSiteConfig = new UCenterSiteConfig();
        uCenterSiteConfig.setuCenterSiteCode(site.getSsoAppId());
        uCenterSiteConfig.setoAuthClientId(site.getSsoAppId());
        uCenterSiteConfig.setoAuthClientSecret(site.getSsoAppSecret());
        uCenterSiteConfig.setuCenterSiteServerPrefix(site.getSsoBasePath());
        return uCenterSiteConfig;
    }

}
