package com.whaty.framework.login.support;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.whaty.framework.api.grant.service.LearnSpaceSimpleLoginService;
import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.login.utils.CookieUtils;
import com.whaty.framework.login.utils.LoginUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模拟登录操作提供类
 *
 * @author weipengsen
 */
@Lazy
@Component("simulateLoginSupport")
public class SimulateLoginSupport {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Resource(name = "learnSpaceSimpleLoginService")
    private LearnSpaceSimpleLoginService learnSpaceSimpleLoginService;

    /**
     * 模拟登录
     *
     * @param loginId
     * @param response
     * @throws Exception
     */
    public Map<String, Object> simulate(String loginId, HttpServletResponse response) throws Exception {
        return this.simulate(loginId, this.userService.getCurrentUser().getId(), response);
    }

    /**
     * 提供操作人的模拟登录
     * @param loginId
     * @param userId
     * @param response
     * @return
     */
    public Map<String, Object> simulate(String loginId, String userId, HttpServletResponse response) throws Exception {
        String sql = "SELECT 1 FROM sso_user s INNER JOIN pe_pri_role r ON s.FK_ROLE_ID = r.ID " +
                "INNER JOIN enum_const ec ON r.FLAG_ROLE_TYPE = ec.id WHERE ec. CODE = '0' " +
                "AND s.id = '%s'";
        List list = this.generalDao.getBySQL(String.format(sql, userId));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new ServiceException("没有模拟登录权限");
        }
        Cookie cookie = CookieUtils.getCookie();
        if (cookie != null) {
            //如果cookie存在,更换cookie
            String cookieValueAfterDecode = new String(Base64.decode(cookie.getValue()), "utf-8");
            String[] cookieValues = cookieValueAfterDecode.split(":");
            if ("null".equals(cookieValues[3])) {
                list = this.generalDao.getBySQL("select password from sso_user where login_id='" + loginId + "'");
                CookieUtils.changeCookie(response, cookie, loginId, String.valueOf(list.get(0)));
                // 模拟登录时放一个1小时有效时间的缓存，在课程空间回调我们平台时用于判断用户是否登录
                this.learnSpaceSimpleLoginService.simulateLoginForLoginId(loginId);
            } else {
                throw new ServiceException("您已模拟登录,此用户无该功能的操作权限");
            }
        }
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("workspaceUrl", LoginUtils.getWorkspaceUrl(loginId, true));
        return resultMap;
    }

}
