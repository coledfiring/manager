package com.whaty.framework.api.custom.controller;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.util.RequestUtils;
import com.whaty.framework.api.custom.applet.service.CustomAppletUserAuthService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 三方小程序登录时提供给用户验证接口api controller
 *
 * @author shanshuai
 */
@Lazy
@RestController("customAppletUserAuthController")
@RequestMapping("/api/applet/customAppletUserAuth")
public class CustomAppletUserAuthController {

    @Resource(name = "customAppletUserAuthService")
    private CustomAppletUserAuthService customAppletUserAuthService;

    /**
     * 用户验证方法
     * @return
     */
    @RequestMapping("/validateUserInfo")
    public ResultDataModel validateUserInfo(HttpServletRequest request) {
        return ResultDataModel.handleSuccessResult(customAppletUserAuthService.
                validateUserInfo(RequestUtils.getRequestMap(request)));
    }

}
