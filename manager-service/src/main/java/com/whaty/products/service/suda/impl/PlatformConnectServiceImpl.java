package com.whaty.products.service.suda.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.suda.PlatformConnectService;
import com.whaty.products.service.suda.domain.TrainDSA;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 苏大平台对接服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("platformConnectService")
public class PlatformConnectServiceImpl implements PlatformConnectService {

    /**
     * 成教平台用户中心url
     */
    private static final String CJ_PLATFORM_LOGIN_CENTER_URL =
            "http://jxjycj.suda.edu.cn/center/center/login_loginByDSA.action?";
    /**
     * 成教平台登录成功后url
     */
    private static final String CJ_PLATFORM_LOGIN_SUCCESS_URL_FORMAT =
            "http://jxjycj.suda.edu.cn/sso/login_getAllIdentity.action?loginId=%s";
    /**
     * 自考平台用户中心url
     */
    private static final String ZK_PLATFORM_LOGIN_CENTER_URL =
            "http://jxjyzk.suda.edu.cn/center/center/login_loginByDSA.action?";
    /**
     * 自考平台登录成功后url
     */
    private static final String ZK_PLATFORM_LOGIN_SUCCESS_URL_FORMAT =
            "http://jxjyzk.suda.edu.cn/sso/login_getAllIdentity.action?loginId=%s";
    /**
     * 成教官网url
     */
    private static final String CJ_OFFICIAL_WEBSITE_FORMAT =
            "http://jxjy.suda.edu.cn/JxjyServer/loginforos.aspx?username=%s&password=%s";
    /**
     * 培训官网url
     */
    private static final String TRAIN_OFFICIAL_WEBSITE_FORMAT =
            "http://jxjypx.suda.edu.cn/CjxyServer/loginforos.aspx?username=%s&password=%s";
    /**
     * 公钥
     */
    public static final String PUBLIC_KEY = "MIHxMIGoBgcqhkjOOAQBMIGcAkEAvp/dyYRnnLV+/Nfe0y/+OOeCLvO7WEfpK0XrXs2Com6+" +
            "HwCC5+sRhdTcyZiI3W5BZh/sBXbqg97yZQv/bAMj2QIVANhITE8PF4PycK89H/wpQRSQF3LRAkAM89vSthrdrLS8Yi27dN4fgDxgWZPg" +
            "HiTikAX7IJwYC18sid55laus8y/beXo4jGoFNOaPY/7ZbSDrlj2+2sz4A0QAAkEAoSbcy1BK/GiTvPy07p/RHwvgQ3VD59aDbu4m4csh" +
            "GuJIBReGYOPZ6yAcCvlTh64BdQVspuTuSrHv09hDqdKWHw==";
    /**
     * 私钥
     */
    public static final String PRIVATE_KEY = "MIHHAgEAMIGoBgcqhkjOOAQBMIGcAkEAvp/dyYRnnLV+/Nfe0y/+OOeCLvO7WEfpK0XrXs2" +
            "Com6+HwCC5+sRhdTcyZiI3W5BZh/sBXbqg97yZQv/bAMj2QIVANhITE8PF4PycK89H/wpQRSQF3LRAkAM89vSthrdrLS8Yi27dN4fgDx" +
            "gWZPgHiTikAX7IJwYC18sid55laus8y/beXo4jGoFNOaPY/7ZbSDrlj2+2sz4BBcCFQCcN9ZDbJodnjGAOPcdNp4OWPc6zw==";


    @Override
    public Map<String, String> getCjPlatformUrlMap() {
        return getPlatformUrlMap(CJ_PLATFORM_LOGIN_CENTER_URL, CJ_PLATFORM_LOGIN_SUCCESS_URL_FORMAT);
    }

    @Override
    public Map<String, String> getZkPlatformUrlMap() {
        return getPlatformUrlMap(ZK_PLATFORM_LOGIN_CENTER_URL, ZK_PLATFORM_LOGIN_SUCCESS_URL_FORMAT);
    }

    @Override
    public String getCjOfficialWebsite() {
        SsoUser user = UserUtils.getCurrentUser();
        return String.format(CJ_OFFICIAL_WEBSITE_FORMAT, user.getLoginId(), user.getPassword());
    }

    @Override
    public String getTrainOfficialWebsite() {
        SsoUser user = UserUtils.getCurrentUser();
        return String.format(TRAIN_OFFICIAL_WEBSITE_FORMAT, user.getLoginId(), user.getPassword());
    }

    /**
     * 获取平台url映射
     *
     * @param centerUrl
     * @param successUrl
     * @return
     */
    private Map<String, String> getPlatformUrlMap(String centerUrl, String successUrl) {
        String loginId = UserUtils.getCurrentUser().getLoginId();
        String loginCenterURL = getDSAUrl(loginId, centerUrl);
        String loginSuccessURL = String.format(successUrl, loginId);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("loginCenterURL", loginCenterURL);
        resultMap.put("loginSuccessURL", loginSuccessURL);
        return resultMap;
    }

    /**
     * 获取dsa签名url
     *
     * @param username
     * @param url
     * @return
     */
    private String getDSAUrl(String username, String url) {
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("loginId", username);
        StringBuilder dsaUrl = new StringBuilder(url);
        try {
            TrainDSA trainDSA = new TrainDSA(PUBLIC_KEY, PRIVATE_KEY, map);
            String signature = trainDSA.signWithBase64();
            map.put("signature", signature);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(CommonConstant.ERROR_STR);
        }
        map.forEach((k, v) -> {
            try {
                dsaUrl.append(k).append("=").append(URLEncoder.encode(v, "utf-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new ServiceException(CommonConstant.ERROR_STR);
            }
        });
        dsaUrl.deleteCharAt(dsaUrl.length() - 1);
        return dsaUrl.toString();
    }
}
