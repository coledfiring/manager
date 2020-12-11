package com.whaty.products.service.wechat.impl;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.httpClient.domain.HttpClientResponseData;
import com.whaty.framework.httpClient.helper.HttpClientHelper;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.products.service.message.strategy.wechat.WeChatTemplateNoticeStrategy;
import com.whaty.products.service.wechat.ApiWeChatPublicService;
import com.whaty.products.service.wechat.WeChatAccessTokenService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.json.JsonObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 微信公众号api
 *
 * @author weipengsen
 */

@Lazy
@Service("apiWeChatPublicServiceImpl")
public class ApiWeChatPublicServiceImpl implements ApiWeChatPublicService {
    private static final Logger logger = LoggerFactory.getLogger(WeChatTemplateNoticeStrategy.class);

    @Resource(name = "weChatAccessTokenService")
    private WeChatAccessTokenService weChatAccessTokenService;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 发送群发信息接口的url
     */
    private static String templateNoticeURL;

    private final static int WECHAT_RETRY_NUM = 5;

    public ApiWeChatPublicServiceImpl() {
        if (templateNoticeURL == null) {
            // 获取资源
            Properties prop = null;
            try {
                prop = new Properties();
                prop.load(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(MessageConstants.PROP_FILE_PATH));
            } catch (IOException e) {
                logger.error("资源文件加载失败", e);
            }
            templateNoticeURL = prop.getProperty(MessageConstants.PROP_TEMPLATE_SEND_TEXT_MESSAGE);
        }
    }

    @Override
    public void doSendTemplateMsg (Map<String, String> requestMap, String domain, String ip) {
        //1 、 根据ip和域名双层判断， 防盗链
        checkDomain(domain, ip);
        //2、  获取token，并判断token所剩时长， 如果时长小于300 则更新token并返回新的token
        try {
            doSendTemplateMsg(requestMap.get("data"), requestMap.get("templateId"), 0);
        } catch (Exception e) {
            throw new ServiceException("");
        }
        //3' 发送模板消息
    }

    public void checkDomain(String domain, String ip) {
        if(!myGeneralDao.checkNotEmpty("SELECT 1 FROM pe_wechat_public WHERE domain= ? AND ip = ?", domain, ip)) {
            throw new ServiceException("域名未被允许访问，请联系管理员！");
        }


    }



    public void doSendTemplateMsg(String data, String templateId, int retryNum) throws Exception {
        if (retryNum >= WECHAT_RETRY_NUM) {
            return;
        }
        JSONObject dataJson = JSONObject.fromObject(data);
        String accessToken = this.weChatAccessTokenService.getToken();
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        HttpClientResponseData result = httpClientHelper.doPostJSON(String.format(templateNoticeURL, accessToken), dataJson.toString());
        // 判断此次http请求是否请求成功
        if (!result.getStatus().equals(HttpStatus.OK.value())) {
            if (logger.isErrorEnabled()) {
                logger.error("部分微信模板信息发送失败，http错误码：" + result.getStatus());
            }
            throw new IOException("微信发送接口无法连接");
        }
        // 判断此次发送任务是否成功
        Map<String, String> map = JSON.parseObject(result.getContent(), HashMap.class);
        if (!String.valueOf(map.get(MessageConstants.WE_CHAT_RESPONSE_STATUS_CODE))
                .equals(MessageConstants.RESPONSE_STATUS_CODE_SUCCESS)) {
            if (logger.isWarnEnabled()) {
                logger.warn("部分微信模板信息发送失败，错误代码：" +
                        String.valueOf(map.get(MessageConstants.WE_CHAT_RESPONSE_STATUS_CODE))
                        + ",错误信息：" + String.valueOf(map.get(MessageConstants.WE_CHAT_RESPONSE_ERROR_INFO)));
            }
            if ("40001".equals(String.valueOf(map.get("errcode")))) {
                this.doSendTemplateMsg(data, templateId, retryNum + 1);
            }
            throw new ServiceException("未关注微信公众号");
        }
    }

    /**
     * 将数据转化为第三方参数格式
     *
     * @param data
     * @param templateId
     * @return
     */
    private String collectThirdParams(Map<String, Object> data, String templateId) {
        Map<String, Object> thirdParams = new LinkedHashMap<>();
        thirdParams.put("touser", data.get("openId"));
        String url = (String) data.remove("url");
        if (StringUtils.isNotBlank(url)) {
            thirdParams.put("url", url);
        }
        data.remove("openId");
        thirdParams.put("template_id", templateId);
        Map<String, Map<String, String>> templateData = new LinkedHashMap<>();
        thirdParams.put("data", templateData);
        data.forEach((k, v) -> {
            Map<String, String> template = new LinkedHashMap<>();
            templateData.put(k, template);
            template.put("value", String.valueOf(v == null ? "" : v));
           // template.put("color", templateTextColor);
        });
        return JSONObject.fromObject(thirdParams).toString();
    }

}
