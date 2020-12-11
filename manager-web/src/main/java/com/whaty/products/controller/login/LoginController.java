package com.whaty.products.controller.login;

import com.whaty.framework.config.util.PlatformConfigUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.login.LoginService;
import com.whaty.framework.login.constant.LoginConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 登录controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/open/login")
public class LoginController {

    @Resource(name = "loginService")
    private LoginService loginService;

    /**
     * 登录逻辑
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/login")
    public ResultDataModel login(@RequestBody ParamsDataModel paramsDataModel)
            throws UnsupportedEncodingException {
        MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
        String loginId = paramsDataModel.getStringParameter(LoginConstant.PARAM_LOGIN_ID);
        Map<String, Object> resultMap = this.loginService.login(loginId);
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    @RequestMapping("/loginByUCenter")
    public ResultDataModel loginByUCenter(@RequestBody ParamsDataModel paramsDataModel) {
        MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
        String ucToken = paramsDataModel.getStringParameterEmptyToNull("uck");
        String clientId = paramsDataModel.getStringParameterEmptyToNull("client_id");
        Map<String, Object> resultMap = this.loginService.loginByUCenter(ucToken, clientId);
        return ResultDataModel.handleSuccessResult(resultMap);
    }

    /***
     * 获取主页配置项
     *
     * @return
     */
    @RequestMapping("/getHomeIndexConfig")
    public ResultDataModel getHomeIndexConfig() {
        return ResultDataModel.handleSuccessResult(PlatformConfigUtil
                .getPlatformConfig().getHomeIndexConfig());
    }

}
