package com.whaty.web.servlet;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 前端控制器，拦截匹配请求
 *
 * @author hanshichao
 * @create 1/25 0025 15:28
 */
public class MyDispatcherServlet extends DispatcherServlet {

    /**
     * 不拦截正则
     */
    private static final String NOT_INTERRUPT_REQUEST_STR = "((\\.jsp)|(\\.action))$";

    /**
     * 例外放行的正则
     */
    private static final String EXCEPTION_REQUEST_STR = "(/mobile/(.+)\\.action)$";

    /**
     * 不拦截正则模式
     */
    private static final Pattern NOT_INTERRUPT_REQUEST_PATTERN = Pattern.compile(NOT_INTERRUPT_REQUEST_STR);

    /**
     * 不拦截正则模式
     */
    private static final Pattern EXCEPTION_REQUEST_PATTERN = Pattern.compile(EXCEPTION_REQUEST_STR);

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        String uri = request.getRequestURI();
        // 不拦截struts的请求,放行例外请求
        if (NOT_INTERRUPT_REQUEST_PATTERN.matcher(uri).find()
                && !EXCEPTION_REQUEST_PATTERN.matcher(uri).find()) {
            return;
        }

        super.service(req, res);
    }
}
