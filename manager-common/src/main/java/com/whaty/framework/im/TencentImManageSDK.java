package com.whaty.framework.im;

import com.alibaba.fastjson.JSON;
import com.whaty.framework.exception.UncheckException;
import com.whaty.handler.DivideWorkHandler;
import com.whaty.framework.httpClient.helper.HttpClientHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 腾讯im的自定义sdk
 *
 * @author weipengsen
 */
@Getter
public class TencentImManageSDK {

    private final HttpClientHelper helper = new HttpClientHelper();

    private final TencentImConfig config;

    private final String userSig;

    private final static String RESOURCE_CONFIG_PATH = "/tencentIm.properties";

    private final static String PROP_APP_ID_KEY = "tencent.im.appId";

    private final static String PROP_SECRET_KEY = "tencent.im.secret";

    private final static String PROP_IDENTIFIER_KEY = "tencent.im.identifier";

    private final static Logger logger = LoggerFactory.getLogger(TencentImManageSDK.class);

    public TencentImManageSDK() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        Properties prop = new Properties();
        prop.load(TencentImManageSDK.class.getResourceAsStream(RESOURCE_CONFIG_PATH));
        this.config = new TencentImConfig(Long.parseLong(prop.getProperty(PROP_APP_ID_KEY)),
                prop.getProperty(PROP_SECRET_KEY), prop.getProperty(PROP_IDENTIFIER_KEY));
        this.userSig = GenerateUserSig.genSig(this.config.appId, this.config.secret, this.config.identifier);
    }

    public TencentImManageSDK(String identifier) throws IOException,
            NoSuchAlgorithmException, InvalidKeyException {
        Properties prop = new Properties();
        prop.load(TencentImManageSDK.class.getResourceAsStream(RESOURCE_CONFIG_PATH));
        this.config = new TencentImConfig(Long.parseLong(prop.getProperty(PROP_APP_ID_KEY)),
                prop.getProperty(PROP_SECRET_KEY), identifier);
        this.userSig = GenerateUserSig.genSig(this.config.appId, this.config.secret, this.config.identifier);
    }

    public TencentImManageSDK(Long appId, String secret, String identifier)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        this.config = new TencentImConfig(appId, secret, identifier);
        this.userSig = GenerateUserSig.genSig(appId, secret, identifier);
    }

    /**
     * 插入用户
     *
     * @param userId
     */
    public void insertUser(String userId) throws Exception {
        this.insertUser(Collections.singletonList(userId));
    }

    /**
     * 批量插入用户
     *
     * @param userIds
     */
    public void insertUser(List<String> userIds) {
        String url = "https://console.tim.qq.com/v4/im_open_login_svc/multiaccount_import";
        new DivideWorkHandler<String>(80, e -> {
            ImResponseResult result;
            try {
                result = this.requestServer(url, Collections.singletonMap("Accounts", e));
            } catch (Exception e1) {
                throw new UncheckException(e1);
            }
            if (!result.isSuccess()) {
                throw new RuntimeException(String.format("im insert user failure code: [%s] message: %s",
                        result.getErrorCode(), result.getErrorInfo()));
            }
        }, e -> {
            String deleteUrl = "https://console.tim.qq.com/v4/im_open_login_svc/account_delete";
            new DivideWorkHandler<String>(80, e1 -> {
                List<Map<String, String>> userParams = e1.stream()
                        .map(e2 -> Collections.singletonMap("UserID", e2)).collect(Collectors.toList());
                try {
                    this.requestServer(deleteUrl, Collections.singletonMap("DeleteItem", userParams));
                } catch (Exception e2) {
                    throw new UncheckException(e2);
                }
            }, null).handle(e);
        }).handle(userIds);
    }

    /**
     * 删除用户
     *
     * @param userId
     */
    public void deleteUser(String userId) {
        this.deleteUser(Collections.singletonList(userId));
    }

    /**
     * 批量删除用户
     *
     * @param userIds
     */
    public void deleteUser(List<String> userIds) {
        String url = "https://console.tim.qq.com/v4/im_open_login_svc/account_delete";
        new DivideWorkHandler<String>(80, e -> {
            List<Map<String, String>> userParams = e.stream()
                    .map(e1 -> Collections.singletonMap("UserID", e1)).collect(Collectors.toList());
            ImResponseResult result;
            try {
                result = this.requestServer(url, Collections.singletonMap("DeleteItem", userParams));
            } catch (Exception e1) {
                throw new UncheckException(e1);
            }
            if (!result.isSuccess()) {
                throw new RuntimeException(String.format("im delete user failure code: [%s] message: %s",
                        result.getErrorCode(), result.getErrorInfo()));
            }
        }, e -> {
            String insertUrl = "https://console.tim.qq.com/v4/im_open_login_svc/multiaccount_import";
            new DivideWorkHandler<String>(80, e1 -> {
                try {
                    this.requestServer(insertUrl, Collections.singletonMap("Accounts", e1));
                } catch (Exception e2) {
                    throw new UncheckException(e2);
                }
            }, null).handle(e);
        }).handle(userIds);
    }

    /**
     * 请求im服务器
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    private ImResponseResult requestServer(String url, Map<String, Object> params) throws Exception {
        url += "?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json";
        long random = (long) (Math.random() * (10 ^ 32));
        url = String.format(url, this.config.appId, this.config.identifier, this.userSig, random);
        return ImResponseResult.convert(helper.doPostJSON(url, JSON.toJSONString(params)).getContent());
    }

    @Data
    @AllArgsConstructor
    private class TencentImConfig implements Serializable {

        private static final long serialVersionUID = -1645652292462936742L;

        final Long appId;

        final String secret;

        final String identifier;

    }

}
