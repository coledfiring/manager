package com.whaty.framework.grid.base.controller;

import com.whaty.framework.exception.AbstractBasicException;
import com.whaty.constant.CommonConstant;
import com.whaty.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.function.Consumer;

/**
 * 通用成教业务controller
 *
 * @author weipengsen
 */
@Lazy
@Controller("tycjBaseController")
public class TycjBaseControllerOperateSupport {

    private final static Logger logger = LoggerFactory.getLogger(TycjBaseControllerOperateSupport.class);

    /**
     * 跳转到错误页面
     * @param message
     */
    public void toErrorPage(String message, HttpServletResponse response) throws ServletException, IOException {
        String url = String.format(CommonUtils.getRequest().getScheme() + "://" +
                CommonUtils.getRequest().getServerName() + ":" + CommonUtils.getRequest().getServerPort() +
                        "/np/#/api/error?message=%s", URLEncoder.encode(message, CommonConstant.CHARSET_UTF_8));
        response.sendRedirect(url);
    }

    /**
     * 下载文件
     * @param response
     * @param downFileFunction
     * @param fileName
     * @throws ServletException
     * @throws IOException
     */
    public void downFile(HttpServletResponse response, Consumer<OutputStream> downFileFunction, String fileName) throws ServletException, IOException {
        try {
            CommonUtils.setContentToDownload(response, fileName);
            downFileFunction.accept(response.getOutputStream());
        } catch (AbstractBasicException e) {
            this.toErrorPage(e.getInfo(), response);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("系统错误,url:" + CommonUtils.getRequest().getRequestURI(), e);
            }
            this.toErrorPage(CommonConstant.ERROR_STR, response);
        }
    }
}
