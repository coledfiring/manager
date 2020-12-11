package com.whaty.framework.api.grant.controller;

import com.whaty.framework.api.grant.constant.GrantConstant;
import com.whaty.framework.api.grant.service.LearnSpaceSimpleLoginService;
import com.whaty.core.framework.api.domain.ResultDataModel;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 课程空间简单登录controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/api/simpleLogin/learnSpaceSimpleLogin")
public class ApiLearnSpaceSimpleLoginController {

    @Resource(name = "learnSpaceSimpleLoginService")
    private LearnSpaceSimpleLoginService learnSpaceSimpleLoginService;

    /**
     * 课程空间简单登录
     *
     * @param siteCode
     * @param loginId
     * @return
     */
    @RequestMapping("/login")
    public ResultDataModel simpleLogin(@RequestParam(GrantConstant.PARAM_SITE_CODE) String siteCode,
                                       @RequestParam(GrantConstant.PARAM_LOGIN_ID) String loginId) {
        this.learnSpaceSimpleLoginService.simpleLoginForHttpParams(siteCode, loginId);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 退出登录
     * @param siteCode
     * @param loginId
     * @return
     */
    @RequestMapping("/exitLogin")
    public ResultDataModel exitSimpleLogin(@RequestParam(GrantConstant.PARAM_SITE_CODE) String siteCode,
                                           @RequestParam(GrantConstant.PARAM_LOGIN_ID) String loginId) {
        this.learnSpaceSimpleLoginService.exitSimpleLogin(siteCode, loginId);
        return ResultDataModel.handleSuccessResult();
    }
}
