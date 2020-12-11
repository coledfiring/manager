package com.whaty.products.service.wechat.impl;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.httpClient.domain.HttpClientResponseData;
import com.whaty.framework.httpClient.helper.HttpClientHelper;
import com.whaty.products.service.wechat.WeChatAccessTokenService;
import com.whaty.products.service.wechat.WeChatBasicService;
import com.whaty.products.service.wechat.domain.AccessToken;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信accessToken操作类
 *
 * @author hanshichao
 */
@Lazy
@Service("weChatAccessTokenService")
public class WeChatAccessTokenServiceImpl implements WeChatAccessTokenService {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = "weChatBasicService")
    private WeChatBasicService weChatBasicService;

    private static Log logger = LogFactory.getLog("WeChatAccessTokenService");
    /**
     * 数据库中token提前刷新时限，过期前提前10分钟刷新
     */
    private final static int REFRESH_LIMIT = 10 * 60;

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    private final static String TOKEN_CACHE_KEY = "weChat_token_key_%s";

    @Override
    public String getToken() throws Exception {
        List<Object> siteList = this.generalDao.getBySQL("select id from wechat_site where domain = '" +
                SiteUtil.getSite().getDomain() + "'");
        if (CollectionUtils.isEmpty(siteList)) {
            throw new ServiceException("未在系统中设置公众号");
        }
        return this.getToken((String) siteList.get(0), SiteUtil.getSiteCode());
    }

    @Override
    public String getToken(String siteId, String siteCode) throws Exception {
        AccessToken token = this.getFromCache(siteId);
        if (token == null) {
            token = getTokenFromDB(siteId);
            if (token == null || (token.expiresIn -
                    (System.currentTimeMillis() - token.readTime) / 1000) < REFRESH_LIMIT) {
                token = refreshToken(siteId, siteCode);
            }
            this.putToCache(siteId, token);
        }
        return token.accessToken;
    }

    public void putToCache(String siteId, AccessToken token) {
        int timeout = token.expiresIn - REFRESH_LIMIT;
        this.redisCacheService.putToCache(String.format(TOKEN_CACHE_KEY, siteId), token, timeout);
    }

    public AccessToken getFromCache(String siteId) {
        return this.redisCacheService.getFromCache(String.format(TOKEN_CACHE_KEY, siteId));
    }

    @Override
    public String getTokenForSite(String siteCode) throws Exception {
        List<Object> siteList = this.generalDao.getBySQL("select id from wechat_site where domain = '" +
                SiteUtil.getSite(siteCode).getDomain() + "'");
        if (CollectionUtils.isEmpty(siteList)) {
            throw new ServiceException("未在系统中设置公众号");
        }
        return this.getToken((String) siteList.get(0), siteCode);
    }

    /**
     * 从数据库中拿出accessToken
     *
     * @param siteId
     * @return
     */
    private AccessToken getTokenFromDB(String siteId) {
        String sql = "select access_token,expires_in - (UNIX_TIMESTAMP() - input_time) from wechat_access_token" +
                " where fk_wechat_site_id = '" + siteId + "'";
        List<Object[]> list = this.generalDao.getBySQL(sql);
        if (list != null && list.size() > 0) {
            Object[] objects = list.get(0);
            AccessToken accessToken = new AccessToken();
            accessToken.accessToken = (String) objects[0];
            accessToken.expiresIn = ((BigInteger) objects[1]).intValue();
            accessToken.readTime = System.currentTimeMillis();
            return accessToken;
        }
        return null;
    }

    /**
     * 刷新token
     *
     * @param siteId
     * @return
     * @throws Exception
     */
    public AccessToken refreshToken(String siteId, String siteCode) throws Exception {
        AccessToken token = new AccessToken();
        String appId = weChatBasicService.getValue(siteId, siteCode, "AppID");
        String appSecret = weChatBasicService.getValue(siteId, siteCode, "AppSecret");
        if (appId != null && appSecret != null) {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId +
                    "&secret=" + appSecret;
            HttpClientResponseData responseData = new HttpClientHelper().doGet(url);
            logger.info("http get token:" + responseData);
            JSONObject jsonObj = JSONObject.fromObject(responseData.getContent());
            Map resultMap = (Map) JSONObject.toBean(jsonObj, HashMap.class);
            Object errcode = resultMap.get("errcode");
            if (errcode != null) {
                throw new ServiceException("获取token出错,errcode:" + errcode + " errmsg:" + resultMap.get("errmsg"));
            } else {
                String accessToken = (String) resultMap.get("access_token");
                Integer expiresIn = (Integer) resultMap.get("expires_in");
                if (accessToken != null) {
                    token.accessToken = accessToken;
                    token.expiresIn = expiresIn;
                    token.readTime = System.currentTimeMillis();
                    this.generalDao.executeBySQL("delete from wechat_access_token where fk_wechat_site_id='" + siteId + "'");
                    this.generalDao.executeBySQL("insert into wechat_access_token(id,fk_wechat_site_id,access_token,expires_in,input_time) "
                            + "values(replace(uuid(),'-',''),'" + siteId + "','" + accessToken + "'," + expiresIn + ",UNIX_TIMESTAMP())");
                    return token;
                } else {
                    throw new ServiceException("获取token出错,token为空");
                }
            }
        } else {
            logger.error("appId and appSecret is null");
            throw new ServiceException("未设置公众号AppId和AppSecret");
        }
    }

}
