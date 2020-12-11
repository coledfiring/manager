package com.whaty.framework.config.util;

import com.whaty.framework.config.BasicApplicationContext;
import com.whaty.framework.config.domain.LearnSpaceConfig;
import com.whaty.util.CommonUtils;

/**
 * 课程空间工具类
 *
 * @author weipengsen
 */
public class LearnSpaceUtil {

    /**
     * 获取站点的课程空间配置
     *
     * @param siteCode
     * @return
     */
    public static LearnSpaceConfig getLearnSpaceConfig(String siteCode) {
        return BasicApplicationContext.LEARN_SPACE_CONFIG_MAP.get(siteCode);
    }

    /**
     * 获取当前站点课程空间站点编号
     *
     * @return
     */
    public static String getLearnSpaceSiteCode() {
        return getLearnSpaceSiteCode(SiteUtil.getSiteCode());
    }

    /**
     * 获取课程空间站点编号
     *
     * @param siteCode
     * @return
     */
    public static String getLearnSpaceSiteCode(String siteCode) {
        return getLearnSpaceConfig(siteCode).getLeanSpaceSiteCode();
    }

    /**
     * 是否可以使用新课程空间
     *
     * @return
     */
    public static Boolean learnSpaceIsOpen() {
        return learnSpaceIsOpen(SiteUtil.getSiteCode());
    }

    /**
     * 是否可以使用新课程空间
     *
     * @param siteCode
     * @return
     */
    public static Boolean learnSpaceIsOpen(String siteCode) {
        return getLearnSpaceConfig(siteCode).getIsOpen();
    }

    /**
     * 获取新课程空间域名
     *
     * @return
     */
    public static String getLearnSpaceConfigDomain() {
        return getLearnSpaceConfigDomain(SiteUtil.getSiteCode());
    }

    /**
     * 获取新课程空间域名
     *
     * @param siteCode
     * @return
     */
    public static String getLearnSpaceConfigDomain(String siteCode) {
        return getLearnSpaceConfig(siteCode).getDomain();
    }

    /**
     * 获取当前站点httpClient url
     * @return
     */
    public static String getHttpClientUrl() {
        return CommonUtils.getScheme() + getDomain(SiteUtil.getSiteCode()) +
                getHttpClientUrl(SiteUtil.getSiteCode());
    }

    /**
     * 获取课程空间域名
     * @param siteCode
     * @return
     */
    private static String getDomain(String siteCode) {
        return getLearnSpaceConfig(siteCode).getDomain();
    }

    /**
     * 获取当前站点课程空间域名
     * @return
     */
    private static String getDomain() {
        return getLearnSpaceConfig(SiteUtil.getSiteCode()).getDomain();
    }

    /**
     * 获取httpClient url
     * @param siteCode
     * @return
     */
    public static String getHttpClientUrl(String siteCode) {
        return getLearnSpaceConfig(siteCode).getHttpClientUrl();
    }
}
