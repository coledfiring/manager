package com.whaty.framework.api.grant.controller;

import com.whaty.framework.api.grant.service.ApiTokenService;
import com.whaty.core.framework.api.domain.ResultDataModel;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * api接口获取token的控制器
 *
 * @author weipengsen
 */
@Lazy
@RestController("apiTokenController")
@RequestMapping("/api/info/apiToken")
public class ApiTokenController {

    @Resource(name = "apiTokenService")
    private ApiTokenService apiTokenService;

    /**
     * 获取token
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public ResultDataModel getToken() throws Exception {
        Map<String, Object> tokenMap = this.apiTokenService.getToken();
        return ResultDataModel.handleSuccessResult(tokenMap);
    }

}
