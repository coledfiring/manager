package com.whaty.framework.api.grant.controller;

import com.whaty.framework.api.grant.constant.GrantConstant;
import com.whaty.framework.api.grant.service.ApiLoginService;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.util.RequestUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * api登录controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/api/login")
public class ApiLoginController {

    @Resource(name = "apiLoginService")
    private ApiLoginService apiLoginService;

    /**
     * 模拟登录接口
     * @return
     */
    @RequestMapping("/simulateLogin")
    public ResultDataModel simulateLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ResultDataModel.handleSuccessResult(this.apiLoginService
                .doSimulateLogin(RequestUtils.getRequestMap(request), response));
    }

    /**
     * 工作室清除缓存
     * @return
     */
    @RequestMapping("/clearLoginCache")
    public ResultDataModel clearLoginCache(HttpServletRequest request) throws Exception {
        this.apiLoginService.clearLoginCache(RequestUtils.getRequestMap(request));
        return ResultDataModel.handleSuccessResult(GrantConstant.RESULT_SUCCESS);
    }
}
