package com.whaty.framework.config.util;

import com.whaty.domain.bean.SitePayInfo;
import com.whaty.framework.config.BasicApplicationContext;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.core.framework.hibernate.dao.impl.ControlGeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.MapUtils;

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点相关工具类
 *
 * @author weipengsen
 */
public class SiteUtil {

    /**
     * 软引用站点支付信息对象缓存
     */
    private static final Map<String, Map<String, SoftReference<SitePayInfo>>> WEB_SITE_PAY_INFO_CACHE = new HashMap<>();

    /**
     * 获取当前站点的siteCode
     *
     * @return
     */
    public static String getSiteCode() {
        String serverName = CommonUtils.getServerName();
        return serverName == null ? null : BasicApplicationContext.DOMAIN_KEY_SITE_MAP.get(serverName).getCode();
    }

    /**
     * 获取所有站点
     *
     * @return
     */
    public static Collection<PeWebSite> listSite() {
        return BasicApplicationContext.DOMAIN_KEY_SITE_MAP.values();
    }

    /**
     * 获取当前站点的id
     *
     * @return
     */
    public static String getSiteId() {
        return getSiteId(getSiteCode());
    }

    /**
     * 获取站点的id
     *
     * @param siteCode
     * @return
     */
    public static String getSiteId(String siteCode) {
        return BasicApplicationContext.CODE_KEY_SITE_MAP.get(siteCode).getId();
    }

    /**
     * 获取当前站点的site对象
     *
     * @return
     */
    public static PeWebSite getSite() {
        return getSite(getSiteCode());
    }

    /**
     * 根据siteCode获取站点的site对象
     *
     * @return
     */
    public static PeWebSite getSite(String siteCode) {
        if (siteCode == null) {
            return null;
        }
        return BasicApplicationContext.CODE_KEY_SITE_MAP.get(siteCode);
    }

    /**
     * 根据siteCode和支付方式获取站点的SitePayInfo对象
     *
     * @return
     */
    public static SitePayInfo getPayInfo(String siteCode, String payWay) {
        SitePayInfo sitePayInfo;
        if (MapUtils.isEmpty(WEB_SITE_PAY_INFO_CACHE.get(siteCode))) {
            sitePayInfo = getSitePayInfo(siteCode, payWay);
            Map<String, SoftReference<SitePayInfo>> payInfoMap = new HashMap<>(16);
            payInfoMap.put(payWay, new SoftReference<>(sitePayInfo));
            WEB_SITE_PAY_INFO_CACHE.put(siteCode, payInfoMap);
        } else {
            SoftReference<SitePayInfo> payInfoCache = WEB_SITE_PAY_INFO_CACHE.get(siteCode).get(payWay);
            if (payInfoCache == null || payInfoCache.get() == null) {
                sitePayInfo = getSitePayInfo(siteCode, payWay);
                WEB_SITE_PAY_INFO_CACHE.get(siteCode).put(payWay, payInfoCache);
            } else {
                sitePayInfo = payInfoCache.get();
            }
        }
        return sitePayInfo;
    }

    /**
     * 根据siteCode和支付方式获取支付方式信息
     *
     * @param siteCode
     * @param payWay
     * @return
     */
    private static SitePayInfo getSitePayInfo(String siteCode, String payWay) {
        String dataSourceCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List sitePayInfoList = ((ControlGeneralDao) SpringUtil
                .getBean(CommonConstant.CONTROL_GENERAL_DAO_BEAN_NAME))
                .getByHQL(" from SitePayInfo where peWebSite.code = '"
                        + siteCode + "' and enumConstByFlagPayType.code = '" + payWay + "'");
        SitePayInfo sitePayInfo = (SitePayInfo) sitePayInfoList.get(0);
        MasterSlaveRoutingDataSource.setDbType(dataSourceCode);
        return sitePayInfo;
    }

    /**
     * 根据learnSpaceCode获取站点的siteCode对象
     *
     * @return
     */
    public static String getSiteCodeByLearnSpaceCode(String learnSpaceCode) {
        String siteCode = SiteUtil.getSiteCode();
        for (Map.Entry<String, PeWebSite> entry : BasicApplicationContext.CODE_KEY_SITE_MAP.entrySet()) {
            if (SiteConstant.SITE_CODE_CONTROL.equals(entry.getKey())) {
                continue;
            }
            if (learnSpaceCode.equals(LearnSpaceUtil.getLearnSpaceConfig(entry.getKey()).getLeanSpaceSiteCode())) {
                siteCode = entry.getValue().getCode();
                break;
            }
        }
        return siteCode;
    }

    /**
     * 根据域名获取站点
     *
     * @param domain
     * @return
     */

    public static PeWebSite getSiteByDomain(String domain) {
        return BasicApplicationContext.DOMAIN_KEY_SITE_MAP.get(domain);
    }

    /**
     * 根据用户中心code获取站点的site对象
     *
     * @return
     */
    public static PeWebSite getSiteCodeBySsoCode(String ssoCode) {
        for (Map.Entry<String, PeWebSite> entry : BasicApplicationContext.CODE_KEY_SITE_MAP.entrySet()) {
            if (ssoCode.equals(entry.getValue().getSsoAppId())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
