package com.whaty.framework.api.grant.constant;

/**
 * api接口产量
 *
 * @author weipengsen
 */
public interface GrantConstant {
    /**
     * 公共api的登录地址
     */
    String OPEN_API_LOGIN_URL = "%s://%s/oauth/token";
    /**
     * 参数，密码
     */
    String PARAM_PASSWORD = "password";
    /**
     * 参数，访问验证方式
     */
    String PARAM_GRANT_TYPE = "grant_type";
    /**
     * 参数，客户端id
     */
    String PARAM_CLIENT_ID = "client_id";
    /**
     * 访问验证方式，密码验证
     */
    String GRANT_TYPE_PASSWORD = "password";
    /**
     * 参数，签名
     */
    String PARAM_SIGN = "sign";
    /**
     * 签名校验类型，排序并使用md5处理
     */
    String CHECK_SIGN_TYPE_SORT_MD5 = "sortMd5";

    /**
     * 课程空间简单登录缓存key
     */
    String LEARN_SPACE_SIMPLE_LOGIN_CACHE_KEY_FORMAT = "%s@@<%s>@@";
    /**
     * 参数：用户名
     */
    String PARAM_LOGIN_ID = "loginId";
    /**
     * 参数：siteCode
     */
    String PARAM_SITE_CODE = "siteCode";
    /**
     * 简单登录系统常量名称，管理员
     */
    String SIMPLE_LOGIN_VARIABLES_NAME_MANAGER = "openLoginManage";
    /**
     * 模拟登录缓存key
     */
    String LEARN_SPACE_SIMULATE_LOGIN_CACHE_KEY_FORMAT = "%s::simulate<%s>::";
    /**
     * 返回结果：成功
     */
    String RESULT_SUCCESS = "success";
}
