package com.whaty.framework.api.grant.service;

/**
 * 课程空间简单登录服务类
 *
 * @author suoqiangqiang
 */
public interface LearnSpaceSimpleLoginService {

    /**
     * 通过http传递参数进行简单登录
     * @param siteCode
     * @param loginId
     */
    void simpleLoginForHttpParams(String siteCode, String loginId);

    /**
     * 使用数据库参数进行简单登录
     * @param variableName
     */
    void simpleLoginUseDataBaseConfig(String variableName);

    /**
     * 退出登录
     * @param siteCode
     * @param loginId
     */
    void exitSimpleLogin(String siteCode, String loginId);

    /**
     * 为loginId简单登录
     * @param loginId
     */
    void simpleLoginForLoginId(String loginId);

    /**
     * 为loginId模拟登录
     * @param loginId
     */
    void simulateLoginForLoginId(String loginId);
}
