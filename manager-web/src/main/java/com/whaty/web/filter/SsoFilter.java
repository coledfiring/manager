package com.whaty.web.filter;

import com.whaty.core.framework.util.RequestUtils;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.core.framework.oauth.CustomUserAuthenticationToken;
import com.whaty.ucenter.oauth2.client.context.DomainHolder;
import com.whaty.ucenter.oauth2.client.provider.OAuth2UserInfo;
import com.whaty.ucenter.oauth2.exception.ApiInvokeFailedException;
import com.whaty.utils.StaticBeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import sun.misc.Request;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 用户中心客户端过滤器，只负责定时检测用户中心是否登录
 *
 * @author hanshichao
 */
public class SsoFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoFilter.class);

    private static final String WHATY_UCENTER_CHECK_DATE = "WHATY_UCENTER_CHECK_DATE";

    private static final int CHECK_SESSION_PERIOD = 60 * 1000;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        DomainHolder.setDomain(request.getServerName());
        if (request.getRequestURI().endsWith("/oauth/token")) {
            chain.doFilter(req, resp);
            return;
        }

        OAuth2Authentication oAuth2Authentication = StaticBeanUtils.getUserService().getAuthentication();
        if (oAuth2Authentication != null) {
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            if (authentication instanceof CustomUserAuthenticationToken) {
                CustomUserAuthenticationToken customUserAuthenticationToken = (CustomUserAuthenticationToken) authentication;
                Map<String, Object> otherData = customUserAuthenticationToken.getOtherData();
                if (MapUtils.isNotEmpty(otherData)) {
                    String ucSessionId = (String) otherData.get("ucSessionId");
                    if (StringUtils.isBlank(ucSessionId) || !this.checkUCenterSessionId(request, ucSessionId)) {
                        this.removeToken(oAuth2Authentication);
                    }
                } else {
                    this.removeToken(oAuth2Authentication);
                }
            }
        }
        chain.doFilter(req, resp);
    }

    private boolean checkUCenterSessionId(HttpServletRequest request, String ucSessionId) {
        Long checkDate = (Long) request.getSession().getAttribute(WHATY_UCENTER_CHECK_DATE);
        if (checkDate == null) {
            checkDate = System.currentTimeMillis();
            request.getSession().setAttribute(WHATY_UCENTER_CHECK_DATE, checkDate);
        }
        if ((checkDate + CHECK_SESSION_PERIOD) < System.currentTimeMillis()) {
            OAuth2UserInfo oAuth2UserInfo = null;
            try {
                PeWebSite site = SiteUtil.getSiteByDomain(DomainHolder.getDomain());
                oAuth2UserInfo = StaticBeanUtils.getUCenterService().checkSession(ucSessionId, site.getSsoAppId());
            } catch (ApiInvokeFailedException e) {
                LOGGER.error("check login status failure", e);
            }
            return oAuth2UserInfo != null && oAuth2UserInfo.getUsername() != null;
        }
        return true;
    }

    private void removeToken(OAuth2Authentication oAuth2Authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails)
                oAuth2Authentication.getDetails();
        StaticBeanUtils.getRedisTokenStore().removeAccessToken(oAuth2AuthenticationDetails.getTokenValue());
    }

    @Override
    public void destroy() {
    }

}
