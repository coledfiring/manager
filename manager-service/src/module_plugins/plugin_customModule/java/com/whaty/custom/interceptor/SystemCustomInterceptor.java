package com.whaty.custom.interceptor;

import com.whaty.constant.CommonConstant;
import com.whaty.custom.handler.SystemCustomHandler;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 热更新定制配置加载拦截器
 *
 * @author weipengsen
 */
public class SystemCustomInterceptor extends HandlerInterceptorAdapter {

    @Resource(name = "systemCustomHandler")
    private SystemCustomHandler systemCustomHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setAttribute("client_id", "hbgr.jc.cn");
        request.getParameter("client_id");
        if (!CommonConstant.LOCAL_HOST_IP.equals(request.getServerName())) {
            String url = request.getServletPath();
            this.systemCustomHandler.putCustomConfigToContext(url);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        this.systemCustomHandler.removeMap();
    }
}
