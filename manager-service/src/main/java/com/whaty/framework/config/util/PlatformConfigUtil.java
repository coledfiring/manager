package com.whaty.framework.config.util;

import com.whaty.framework.config.BasicApplicationContext;
import com.whaty.framework.config.domain.PlatformConfig;

/**
 * 平台配置读取工具
 * @author weipengsen
 */
public class PlatformConfigUtil {

    /**
     * 获取当前站点平台配置
     * @return
     */
    public static PlatformConfig getPlatformConfig() {
        return getPlatformConfig(SiteUtil.getSiteCode());
    }

    /**
     * 获取平台信息
     *
     * @param siteCode
     * @return
     */
    public static PlatformConfig getPlatformConfig(String siteCode) {
        PlatformConfig config = BasicApplicationContext.PLATFORM_CONFIG_MAP.get(siteCode);
        return config == null ? new PlatformConfig() : config;
    }

    /**
     * 微信站点是否开启
     *
     * @param siteCode
     * @return
     */
    public static Boolean isWeChatSiteOpen(String siteCode) {
        PlatformConfig config = BasicApplicationContext.PLATFORM_CONFIG_MAP.get(siteCode);
        return config.getIsWeChatSiteOpen() == null ? false : config.getIsWeChatSiteOpen();
    }

    /**
     * 是否是在线培训
     *
     * @param siteCode
     * @return
     */
    public static Boolean isOnlineTraining(String siteCode) {
        PlatformConfig config = BasicApplicationContext.PLATFORM_CONFIG_MAP.get(siteCode);
        return config.getIsOnlineTraining() == null ? false : config.getIsOnlineTraining();
    }

}
