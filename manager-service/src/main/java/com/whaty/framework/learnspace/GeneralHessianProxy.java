package com.whaty.framework.learnspace;

import com.caucho.hessian.client.HessianProxyFactory;
import com.whaty.framework.config.BasicApplicationContext;
import com.whaty.framework.config.domain.LearnSpaceConfig;
import com.whaty.framework.config.util.SiteUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

/**
 * hessian代理辅助类
 */
@Component("generalHessianProxy")
public class GeneralHessianProxy implements InitializingBean {

    private final static HessianProxyFactory HESSIAN_PROXY_FACTORY = new HessianProxyFactory();

    @Value("${hessian.defaultUrl}")
    private String defaultUrl;

    @Value("${hessian.readTimeOut}")
    private long readTimeOut;

    @Value("${hessian.connectTimeOut}")
    private long connectTimeOut;

    @Value("${hessian.overloadEnabled}")
    private boolean overloadEnabled;

    @Override
    public void afterPropertiesSet() {
        HESSIAN_PROXY_FACTORY.setReadTimeout(this.readTimeOut);
        HESSIAN_PROXY_FACTORY.setReadTimeout(this.connectTimeOut);
        HESSIAN_PROXY_FACTORY.setOverloadEnabled(this.overloadEnabled);
    }

    /**
     * 创建代理对象
     * @param clazz
     * @param siteCode
     * @param <T>
     * @return
     */
    public <T> T getTargetSource(Class<T> clazz, String siteCode) {
        try {
            String url = getServiceUrl(SiteUtil.getSiteCodeByLearnSpaceCode(siteCode));
            if (url == null || "".equals(url)) {
                url = defaultUrl;
            }
            return (T) HESSIAN_PROXY_FACTORY.create(clazz, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前站点hessian访问地址
     * @return
     */
    private String getServiceUrl() {
        return this.getServiceUrl(SiteUtil.getSiteCode());
    }

    /**
     * 获取指定站点hessian访问地址
     * @return
     */
    private String getServiceUrl(String siteCode) {
        LearnSpaceConfig learnSpaceConfig = BasicApplicationContext.LEARN_SPACE_CONFIG_MAP
                .get(StringUtils.isBlank(siteCode) ? SiteUtil.getSiteCode() : siteCode);
        return "http://" + learnSpaceConfig.getDomain() + learnSpaceConfig.getServiceUrl();
    }

}
