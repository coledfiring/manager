package com.whaty.framework.api.grant.service.impl;

import com.whaty.framework.api.grant.handler.LearnSpaceSimpleLoginHandler;
import com.whaty.framework.api.grant.service.LearnSpaceSimpleLoginService;
import com.whaty.framework.api.grant.utils.BxqkGrantUtils;
import com.whaty.framework.config.util.SiteUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 课程空间简单登录服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("learnSpaceSimpleLoginService")
public class LearnSpaceSimpleLoginServiceImpl implements LearnSpaceSimpleLoginService {

    @Resource(name = "learnSpaceSimpleLoginHandler")
    private LearnSpaceSimpleLoginHandler learnSpaceSimpleLoginHandler;

    @Override
    public void simpleLoginForHttpParams(String siteCode, String loginId) {
        this.learnSpaceSimpleLoginHandler.simpleLogin(siteCode, loginId);
    }

    @Override
    public void simpleLoginUseDataBaseConfig(String variableName) {
        this.learnSpaceSimpleLoginHandler.simpleLogin(SiteUtil.getSiteCode(),
                BxqkGrantUtils.getSimpleLoginLoginId(variableName));
    }

    @Override
    public void exitSimpleLogin(String siteCode, String loginId) {
        this.learnSpaceSimpleLoginHandler.exitSimpleLogin(siteCode, loginId);
    }

    @Override
    public void simpleLoginForLoginId(String loginId) {
        this.learnSpaceSimpleLoginHandler.simpleLogin(SiteUtil.getSiteCode(), loginId);
    }

    @Override
    public void simulateLoginForLoginId(String loginId) {
        this.learnSpaceSimpleLoginHandler.simulateLogin(SiteUtil.getSiteCode(), loginId);
    }
}
