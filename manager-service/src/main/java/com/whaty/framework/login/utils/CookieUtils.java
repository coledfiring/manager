package com.whaty.framework.login.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.whaty.framework.login.constant.LoginConstant;
import com.whaty.util.CommonUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * cookie的工具类
 *
 * @author weipengsen
 */
public class CookieUtils {


    /**
     * 设置cookie
     * @param value cookie值
     * @param maxAge cookie有效时长
     */
    public static void setCookie(String value, int maxAge, HttpServletResponse response){
        Cookie cookie = new Cookie(LoginConstant.COOKIE_DOMAIN_NAME, value);
        // 存两周(这个值应该大于或等于validTime)
        cookie.setMaxAge(maxAge);
        // cookie有效路径是网站根目录
        cookie.setPath("/");
        // 向客户端写入
        response.addCookie(cookie);
    }

    /**
     * 更换cookie的值
     *
     * @param response
     * @param cookie
     * @param loginId
     * @throws Exception cookieValues 数组 cookieValues[0]当前登录id cookieValues[1]有效期 cookieValues[2]用户验证信息
     *                   cookieValues[3]存放模拟登录之前的loginId,未模拟登录默认为"null"
     */
    public static void changeCookie(HttpServletResponse response, Cookie cookie,
                                    String loginId, String password) throws Exception {
        String cookieValueAfterDecode = new String(Base64.decode(cookie.getValue()), "utf-8");
        StringBuilder cookieValue = new StringBuilder();
        String[] cookieValues = cookieValueAfterDecode.split(":");
        if ("null".equals(cookieValues[3])) {
            // 模拟登陆
            cookieValues[3] = cookieValues[0];
            cookieValues[0] = loginId;
        } else {
            // 退出模拟登陆
            cookieValues[0] = cookieValues[3];
            cookieValues[3] = "null";
        }

        if (StringUtils.isNotBlank(password)) {
            String newMd5Value = CommonUtils.md5(loginId + ":" + password + ":" + "-1" + ":" + "whaty");
            cookieValues[2] = newMd5Value;
        }

        for (int i = 0; i < cookieValues.length; i++) {
            cookieValue.append(cookieValues[i] + ":");
        }
        cookieValue.setCharAt(cookieValue.lastIndexOf(":"), ' ');
        String cookieValueBase64 = new String(Base64.encode(cookieValue.toString().trim().getBytes()));
        setCookie(cookieValueBase64, cookie.getMaxAge(), response);
    }

    /**
     * 获取cookie
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Cookie getCookie() throws UnsupportedEncodingException {
        Cookie[] cookies = CommonUtils.getRequest().getCookies();
        String cookieValue = null;
        Cookie cookie = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (LoginConstant.COOKIE_DOMAIN_NAME.equals(cookies[i].getName())) {
                    cookieValue = cookies[i].getValue();
                    cookie = cookies[i];
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
        return cookie;
    }

}
