package com.whaty.framework.login.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.whaty.framework.login.constant.LoginConstant;
import com.whaty.util.CommonUtils;
import org.apache.commons.lang.ArrayUtils;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;

/**
 * 登录工具类
 *
 * @author weipengsen
 */
public class LoginUtils {

    /**
     * 获得cookie中的 用户名
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getLoginIdFromCookie() throws UnsupportedEncodingException {
        String[] cookieValue = getCookieValue();
        return ArrayUtils.isEmpty(cookieValue) ? null : cookieValue[0];
    }

    private static String[] getCookieValue() throws UnsupportedEncodingException {
        Cookie[] cookies = CommonUtils.getRequest().getCookies();
        String cookieValue = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (LoginConstant.COOKIE_DOMAIN_NAME.equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }

        // 如果cookieValue为空,返回,
        if (cookieValue == null) {
            return null;
        }

        // 如果cookieValue不为空,才执行下面的代码
        // 先得到的CookieValue进行Base64解码
        String cookieValueAfterDecode = new String(Base64.decode(cookieValue), "utf-8");

        // 对解码后的值进行分拆,得到一个数组,如果数组长度不为5,就是非法登陆
        String[] cookieValues = cookieValueAfterDecode.split(":");
        if (cookieValues.length != 5) {
            return null;
        }
        return cookieValues;
    }

    /**
     * 获取工作室url地址
     *
     * @return
     */
    public static String getWorkspaceUrl(String loginId, Boolean isSimulateLogin) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return CommonUtils.getBasicUrl() + LoginConstant.WORKSPACE_CONTEXT_PATH
                + "/open/auth/login?"
                + "loginId=" + loginId
                + "&timestamp=" + timestamp
                + "&isSimulateLogin=" + isSimulateLogin
                + "&sign=" + CommonUtils.md5(loginId + timestamp + LoginConstant.SIGNATURE_AUTH_KEY);
    }

    /**
     * 判断是否是模拟登录
     * @return
     */
    public static boolean isSimulateLogin() throws UnsupportedEncodingException {
        String[] cookieValues = getCookieValue();
        if (cookieValues == null) {
            throw new IllegalArgumentException("当前不是登录状态");
        }
        return !"null".equals(cookieValues[3]);
    }
}
