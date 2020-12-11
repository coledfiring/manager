package com.whaty.framework.login.impl;

import com.whaty.constant.SiteConstant;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.api.grant.service.LearnSpaceSimpleLoginService;
import com.whaty.cache.CacheKeys;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.cache.service.CacheService;
import com.whaty.core.commons.cache.util.CacheUtil;
import com.whaty.core.commons.httpClient.WhatyHttpClient;
import com.whaty.core.framework.bean.Site;
import com.whaty.core.framework.oauth.CustomUserAuthenticationToken;
import com.whaty.core.framework.oauth.RedisTokenStore;
import com.whaty.core.framework.service.SiteService;
import com.whaty.core.framework.user.util.SsoConstant;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UCenterException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.login.LoginService;
import com.whaty.framework.login.utils.LoginUtils;
import com.whaty.util.CommonUtils;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = "learnSpaceSimpleLoginService")
    private LearnSpaceSimpleLoginService learnSpaceSimpleLoginService;

    @Resource(name = BeanNames.SITE_SERVICE)
    private SiteService siteService;

    @Resource(name = "customTokenStore")
    private RedisTokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private OAuth2RequestFactory oAuth2RequestFactory;

    @Autowired
    private TokenGranter tokenGranter;

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    @Resource(name = "core_cacheService")
    private CacheService cacheService;

    /**
     * 用户中心登录接口
     */
    private static final String U_CENTER_LOGIN_API_URL = "/api/user/me3";

    @Override
    @LogAndNotice("登录")
    public Map<String, Object> login(String loginId) throws UnsupportedEncodingException {
        return this.login(loginId, true);
    }

    private Map<String, Object> login(String loginId, boolean checkCookie) throws UnsupportedEncodingException {
        String roleType = this.getRoleTypeFromLoginId(loginId);
        if (StringUtils.isBlank(loginId) || !this.checkLogin(loginId, roleType, checkCookie)) {
            throw new ParameterIllegalException();
        }
        Map<String, Object> resultMap = new HashMap<>(4);
        if (CommonConstant.ROLE_CODE_STUDENT.equals(roleType)
                || CommonConstant.ROLE_CODE_TEACHER.equals(roleType)) {
            this.learnSpaceSimpleLoginService.simpleLoginForLoginId(loginId);
            resultMap.put("loginType", "workspace");
            resultMap.put("workspaceUrl", LoginUtils.getWorkspaceUrl(loginId, false));
        } else {
            resultMap.put("token", this.authorize(loginId));
            resultMap.put("loginType", "manager");
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> loginByUCenter(String ucToken, String clientId) {
        Site site = this.siteService.getSiteByAppId(clientId);
        if (site == null) {
            throw new ServiceException("登录失败， client_id错误。");
        }
        String url = site.getSsoBasePath() + U_CENTER_LOGIN_API_URL;
        Map<String, String> params = new HashMap<>(2);
        params.put("access_token", ucToken);
        try {
            WhatyHttpClient whatyHttpClient = new WhatyHttpClient();
            HttpClient httpClient = whatyHttpClient.initHttpClient();
            PostMethod postMethod = whatyHttpClient.postMethodSimple(url, params);
            int status = httpClient.executeMethod(postMethod);
            if (status != HttpStatus.SC_OK) {
                throw new UCenterException();
            }
            String result = postMethod.getResponseBodyAsString();
            if (StringUtils.isBlank(result)) {
                throw new ServiceException("登录失败");
            }
            JSONObject jsonObject = JSONObject.fromObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            Map<String, Object> resultMap = this.login(data.getString("loginId"), false);
            String token = (String) resultMap.get("token");
            // 如果是管理员登录时token存在,则保存用户中心的sessionId到认证对象中,如果是学生/教师登录则不做处理
            if (StringUtils.isNotBlank(token)) {
                this.addUcSessionIdToAuthentication(token, data.getString("sessionId"));
            }
            String siteCode = SiteUtil.getSiteCode();
            if (!SiteConstant.SITE_CODE_CONTROL.equals(siteCode)) {
                this.doSaveLoginUserInfo(data.getString("loginId"), siteCode);
            }
            return resultMap;
        } catch (IOException e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 将用户中心的sessionId添加到auth2认证对象中
     */
    private void addUcSessionIdToAuthentication(String token, String sessionId) {
        OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(token);
        OAuth2Authentication auth2Authentication = this.tokenStore.readAuthentication(token);
        if (auth2Authentication.getUserAuthentication() != null
                && auth2Authentication.getUserAuthentication() instanceof CustomUserAuthenticationToken) {
            CustomUserAuthenticationToken customUserAuthenticationToken = (CustomUserAuthenticationToken)
                    auth2Authentication.getUserAuthentication();
            Map<String, Object> otherData = new HashMap<>(2);
            otherData.put("ucSessionId", sessionId);
            customUserAuthenticationToken.setOtherData(otherData);
            OAuth2Authentication newAuth2Authentication = new OAuth2Authentication(
                    auth2Authentication.getOAuth2Request(), customUserAuthenticationToken);
            this.tokenStore.storeAccessToken(accessToken, newAuth2Authentication);
        }
    }

    /**
     * 新框架登录授权
     *
     * @param loginId
     */
    private String authorize(String loginId) {
        String clientId = CommonUtils.getRequest().getServerName();
        Authentication clientAuth = new UsernamePasswordAuthenticationToken(clientId, null);
        SecurityContextHolder.getContext().setAuthentication(clientAuth);

        Map<String, Object> userInfo = this.getUserInfo(loginId);

        Map<String, String> parameters = new HashMap<>(4);
        parameters.put("client_id", clientId);
        parameters.put("username", (String) userInfo.get("loginId"));
        parameters.put("password", (String) userInfo.get("password"));
        parameters.put("grant_type", "password");

        // 清除缓存
        String cacheKey = CacheKeys.LOGIN_CACHE_KEYS.getKeyWithParams(clientId, loginId);
        try {
            cacheKey = URLDecoder.decode(cacheKey, "UTF-8");
            this.redisCacheService.remove(cacheKey);
            // 清除框架内用户缓存
            cacheKey = CacheUtil.getCacheKeyWithParams("%s_%s", clientId, loginId);
            this.cacheService.remove(cacheKey);
        } catch (UnsupportedEncodingException e) {
            throw new UncheckException(e);
        }

        ClientDetails authenticatedClient = this.clientDetailsService.loadClientByClientId(clientId);
        TokenRequest tokenRequest = this.oAuth2RequestFactory.createTokenRequest(parameters, authenticatedClient);
        OAuth2AccessToken token = this.tokenGranter.grant(tokenRequest.getGrantType(), tokenRequest);
        if (token == null) {
            throw new ServiceException("登录失败，在管理端获取token失败！");
        }
        return token.getValue();
    }

    /**
     * 获取用户信息
     *
     * @param loginId
     * @return
     */
    private Map<String, Object> getUserInfo(String loginId) {
        Map<String, Object> userInfo = this.generalDao
                .getOneMapBySQL("select login_Id as loginId, password as password from sso_user" +
                        " where login_id = ? and site_code = ?", loginId, SiteUtil.getSiteCode());
        return userInfo;
    }

    @Override
    public String getRoleTypeFromLoginId(String loginId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                  ");
        sql.append(" 	ty.code                                              ");
        sql.append(" FROM                                                    ");
        sql.append(" 	sso_user ss                                          ");
        sql.append(" INNER JOIN pe_pri_role pri on pri.id = ss.FK_ROLE_ID    ");
        sql.append(" INNER JOIN enum_const ty on ty.id = pri.FLAG_ROLE_TYPE  ");
        sql.append(" WHERE                                                   ");
        sql.append(" 	ss.LOGIN_ID = ?    ");
        sql.append(" AND ss.site_code = ?  ");
        String role = this.generalDao.getOneBySQL(sql.toString(), loginId, SiteUtil.getSiteCode());
        if (role == null) {
            throw new ServiceException("用户名或密码错误");
        }
        return role;
    }

    @Override
    public boolean checkLogin(String loginId, String roleType, boolean checkCookie)
            throws UnsupportedEncodingException {
        if (checkCookie && !loginId.equals(LoginUtils.getLoginIdFromCookie())) {
            return false;
        }
        StringBuilder sql = new StringBuilder();
        if (CommonConstant.ROLE_CODE_STUDENT.equals(roleType)) {
            // 学生
            sql.append(" SELECT                                                        ");
            sql.append(" 	va. CODE AS active,                                        ");
            sql.append(" 	st. CODE AS status,                                        ");
            sql.append(" 	stu.id as id,		                                       ");
            sql.append(" 	stu.graduation_date as graduationDate		               ");
            sql.append(" FROM                                                          ");
            sql.append(" 	pe_student stu                                             ");
            sql.append(" INNER JOIN sso_user ss ON ss.id = stu.FK_SSO_USER_ID          ");
            sql.append(" INNER JOIN pe_pri_role ppr ON ppr.id = ss.FK_ROLE_ID          ");
            sql.append(" INNER JOIN enum_const rt ON rt.id = ppr.FLAG_ROLE_TYPE        ");
            sql.append(" INNER JOIN enum_const va ON va.id = ss.FLAG_ISVALID           ");
            sql.append(" INNER JOIN enum_const st ON st.id = stu.flag_student_status   ");
            sql.append(" WHERE                                                         ");
            sql.append(" 	ss.login_id = '" + loginId + "'                            ");
            sql.append(" AND ss.site_code = '" + SiteUtil.getSiteCode() + "'           ");
        } else if (CommonConstant.ROLE_CODE_TEACHER.equals(roleType)) {
            // 教师
            sql.append(" SELECT                                                        ");
            sql.append(" 	ac. CODE AS active                                         ");
            sql.append(" FROM                                                          ");
            sql.append(" 	pe_teacher tea                                             ");
            sql.append(" INNER JOIN sso_user ss ON ss.id = tea.FK_SSO_USER_ID          ");
            sql.append(" INNER JOIN pe_pri_role ppr ON ppr.id = ss.FK_ROLE_ID          ");
            sql.append(" INNER JOIN enum_const rt ON rt.id = ppr.FLAG_ROLE_TYPE        ");
            sql.append(" INNER JOIN enum_const ac ON ac.id = tea.FLAG_ACTIVE           ");
            sql.append(" WHERE                                                         ");
            sql.append(" 	ss.login_id = '" + loginId + "'                            ");
            sql.append(" AND ss.site_code = '" + SiteUtil.getSiteCode() + "'           ");
        } else if (CommonConstant.ROLE_CODE_SUPER_ADMIN.equals(roleType)) {
            // 超管
            return true;
        } else {
            // 总站管理员
            sql.append(" SELECT                                                        ");
            sql.append(" 	va. CODE AS active                                         ");
            sql.append(" FROM                                                          ");
            sql.append(" 	pe_manager ma                                              ");
            sql.append(" INNER JOIN sso_user ss ON ss.id = ma.FK_SSO_USER_ID           ");
            sql.append(" INNER JOIN pe_pri_role ppr ON ppr.id = ss.FK_ROLE_ID          ");
            sql.append(" INNER JOIN enum_const rt ON rt.id = ppr.FLAG_ROLE_TYPE        ");
            sql.append(" INNER JOIN enum_const va ON va.id = ma.flag_active            ");
            sql.append(" WHERE                                                         ");
            sql.append(" 	ss.login_id = '" + loginId + "'                            ");
            sql.append(" AND ss.site_code = '" + SiteUtil.getSiteCode() + "'           ");
        }
        Map<String, Object> activeCode = this.generalDao.getOneMapBySQL(sql.toString());
        if (activeCode == null) {
            return false;
        }
        // 拿出用户是否有效常量的code，查看用户是否可登录
        String isActive = (String) activeCode.get("active");
        if (StringUtils.isBlank(isActive) || !SsoConstant.SSO_USER_IS_VALID.equals(isActive)) {
            throw new ServiceException("您的帐号处于无效状态，不能登陆");
        }
        if (CommonConstant.ROLE_CODE_STUDENT.equals(roleType)) {
            // 登录用户是学生角色
            if (!"4".equals(activeCode.get("status"))) {
                // 登录者是学生且不是报到状态，且当前日期小于毕业后三个月
                Date graduationDate = (Date) activeCode.get("graduationDate");
                if (graduationDate == null) {
                    throw new ServiceException("您不是报到状态，不能登陆平台");
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(graduationDate);
                calendar.add(Calendar.MONTH, 3);
                if (!CommonUtils.compareDate(new Date(), calendar.getTime())) {
                    throw new ServiceException("您已毕业超过三个月，不能登陆平台");
                }
            }
        }
        return true;
    }

    @Override
    @LogAndNotice("保存登录记录")
    public void doSaveLoginUserInfo(String loginId, String siteCode) {
        String userId = this.generalDao
                .getOneBySQL(" select id from sso_user where login_id = '" + loginId + "' and site_code = '"
                        + siteCode + "'");
        String loginIp = CommonUtils.getIpAddress();
        //pc登录
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO sso_user_login (             ");
        sql.append("   id,                                    ");
        sql.append("   FK_SSO_USER_ID,                        ");
        sql.append("   LOGIN_DATETIME,                        ");
        sql.append("   LOGIN_IP,                              ");
        sql.append("   LOGIN_DATE,                            ");
        sql.append("   FLAG_LOGIN_TYPE                        ");
        sql.append(" )                                        ");
        sql.append(" values (                                 ");
        sql.append("   replace(uuid(),'-',''),                ");
        sql.append("   '" + userId + "',                      ");
        sql.append("   now(),                                 ");
        sql.append("   '" + loginIp + "',                     ");
        sql.append("   date_format(now(),'%Y-%m-%d'),         ");
        sql.append("   (SELECT id FROM enum_const WHERE NAMESPACE = 'FlagLoginType' AND CODE = '1') ");
        sql.append(" )                                        ");
        this.generalDao.executeBySQL(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" UPDATE sso_user                               ");
        sql.append(" SET LOGIN_NUM = ifnull(LOGIN_NUM, 0) + 1,     ");
        sql.append("  LAST_LOGIN_IP = '" + loginIp + "',           ");
        sql.append("  LAST_LOGIN_DATE = now()                      ");
        sql.append(" WHERE                                         ");
        sql.append(" 	id = '" + userId + "'                      ");
        this.generalDao.executeBySQL(sql.toString());
    }

}
