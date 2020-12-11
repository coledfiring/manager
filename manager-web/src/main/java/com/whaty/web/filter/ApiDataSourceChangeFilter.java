package com.whaty.web.filter;

import com.whaty.core.framework.util.RequestUtils;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.web.handler.UrlPatternHandler;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 由于新框架不登录无法切换dataSource，所以使用过滤器对未登录的接口进行数据源切换
 *
 * @author weipengsen
 */
public class ApiDataSourceChangeFilter implements Filter {

    private static final List<String> EXCLUDE_URL_LIST = new ArrayList<>();

    private static final UrlPatternHandler EXCLUDE_URL_LIST_HANDLER;

    static {
        EXCLUDE_URL_LIST_HANDLER = new UrlPatternHandler(EXCLUDE_URL_LIST);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (!EXCLUDE_URL_LIST_HANDLER.match(request.getRequestURI().replace(request.getContextPath(), ""))) {
            PeWebSite currentSite = SiteUtil.getSite();
            if (currentSite != null) {
                MasterSlaveRoutingDataSource.setDbType(currentSite.getDatasourceCode());
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {}
}
