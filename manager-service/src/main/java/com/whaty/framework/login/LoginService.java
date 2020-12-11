package com.whaty.framework.login;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 登录服务类接口
 *
 * @author weipengsen
 */
public interface LoginService {

    /**
     * 登录
     * @param loginId
     * @return
     * @throws UnsupportedEncodingException
     */
    Map<String,Object> login(String loginId) throws UnsupportedEncodingException;

    /**
     * 使用用户中心的token进行登录
     *
     * @param ucToken
     * @param clientId
     * @return
     */
    Map<String, Object> loginByUCenter(String ucToken, String clientId);

    /**
     * 使用loginId获取用户角色类型
     *
     * @param loginId
     * @return
     */
    String getRoleTypeFromLoginId(String loginId);

    /**
     * 校验是否可以登录
     *
     * @param loginId
     * @param roleType
     * @param checkCookie
     * @return
     * @throws UnsupportedEncodingException
     */
    boolean checkLogin(String loginId, String roleType, boolean checkCookie) throws UnsupportedEncodingException;

    /**
     * 保存用户登录信息
     * @param loginId
     * @param siteCode
     */
    void doSaveLoginUserInfo(String loginId, String siteCode);
}
