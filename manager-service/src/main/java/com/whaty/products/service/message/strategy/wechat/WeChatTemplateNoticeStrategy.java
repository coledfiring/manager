package com.whaty.products.service.message.strategy.wechat;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.httpClient.domain.HttpClientResponseData;
import com.whaty.notice.constant.NoticeServerPollConstant;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.products.service.message.strategy.AbstractNoticeStrategy;
import com.whaty.products.service.wechat.WeChatAccessTokenService;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.util.SQLHandleUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 微信模板消息发送策略
 *
 * @author weipengsen
 */
@Lazy
@Component("weChatTemplateNoticeStrategy")
public class WeChatTemplateNoticeStrategy implements AbstractNoticeStrategy {

    private static final Logger logger = LoggerFactory.getLogger(WeChatTemplateNoticeStrategy.class);

    @Resource(name = "weChatAccessTokenService")
    private WeChatAccessTokenService weChatAccessTokenService;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao currentGeneralDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    /**
     * 发送群发信息接口的url
     */
    private static String templateNoticeURL;
    /**
     * 模板文字颜色
     */
    private static String templateTextColor;

    private final static int WECHAT_RETRY_NUM = 5;

    public WeChatTemplateNoticeStrategy() {
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
            templateTextColor = prop.getProperty(MessageConstants.PROP_TEMPLATE_TEXT_COLOR);
        }
    }

    @Override
    public void notice(Map<String, Object> params) throws Exception {
        if (!params.containsKey(CommonConstant.PARAM_IDS)
                || !params.containsKey(MessageConstants.PARAM_TEMPLATE_CODE)) {
            throw new ParameterIllegalException();
        }
        this.addExtraParams(params);
        String siteCode = params.get(MessageConstants.PARAM_SITE_CODE) == null ? SiteUtil.getSiteCode() :
                (String) params.get(MessageConstants.PARAM_SITE_CODE);
        Map<String, Object> templateConfig = this.getTemplateConfig((String) params
                .get(MessageConstants.PARAM_TEMPLATE_CODE), siteCode);
        String accessToken = this.weChatAccessTokenService.getTokenForSite(siteCode);
        List<Map<String, Object>> data = this.listData((String) templateConfig.get("dataSql"), params);
        Map<String, Object> extraParam = (Map<String, Object>) params.get("templateData");
        if (MapUtils.isNotEmpty(extraParam)) {
            data.forEach(e -> extraParam.forEach(e::put));
        }
        this.noticeAll(data, accessToken, (String) templateConfig.get("templateId"));
    }

    /**
     * 添加额外参数
     * @param params
     */
    private void addExtraParams(Map<String, Object> params) {
        if (CommonUtils.getRequest() != null) {
            params.put("basePath", CommonUtils.getBasicUrl());
        }
    }

    @Override
    public String getTableName() {
        return "wechat_template_message_group";
    }

    /**
     * 通知所有
     *
     * @param data
     * @param accessToken
     * @param templateId
     */
    private void noticeAll(List<Map<String, Object>> data, String accessToken, String templateId) {
        int errorNum = 0;
        for (Map<String, Object> datum : data) {
            if (StringUtils.isBlank((String) datum.get("openId"))) {
                errorNum ++;
                continue;
            }
            try {
                this.noticeSingle(datum, accessToken, templateId, 0);
            } catch (ServiceException e1) {
                errorNum ++;
            } catch (Exception e1) {
                throw new UncheckException(e1);
            }
        }
        if (data.size() <= errorNum) {
            throw new ServiceException("未关注公众号，无法发送");
        }
        if (errorNum > 0) {
            NoticeServerPollUtils.selfNotice(String
                    .format("发送微信模板消息成功，共发送%s条，成功%s条，失败%s条，未关注公众号的用户无法发送消息",
                            data.size(), data.size() - errorNum, errorNum),
                    NoticeServerPollConstant.NOTICE_ENUM_CONST_CODE_USER_OPERATE_TYPE);
        }
    }

    /**
     * 单个通知
     *
     * @param data
     * @param accessToken
     * @param templateId
     * @param retryNum
     * @throws Exception
     */
    private void noticeSingle(Map<String, Object> data, String accessToken, String templateId, int retryNum)
            throws Exception {
        if (retryNum >= WECHAT_RETRY_NUM) {
            return;
        }
        String jsonString = this.collectThirdParams(data, templateId);
        HttpClientResponseData result = this.requestHttp(String.format(templateNoticeURL, accessToken), jsonString);
        // 判断此次http请求是否请求成功
        if (!result.getStatus().equals(HttpStatus.OK.value())) {
            if (logger.isErrorEnabled()) {
                logger.error("部分微信模板信息发送失败，http错误码：" + result.getStatus());
            }
            throw new IOException("微信发送接口无法连接");
        }
        // 判断此次发送任务是否成功weChatAccessTokenService
        Map<String, String> map = JSON.parseObject(result.getContent(), HashMap.class);
        if (!String.valueOf(map.get(MessageConstants.WE_CHAT_RESPONSE_STATUS_CODE))
                .equals(MessageConstants.RESPONSE_STATUS_CODE_SUCCESS)) {
            if (logger.isWarnEnabled()) {
                logger.warn("部分微信模板信息发送失败，错误代码：" +
                        String.valueOf(map.get(MessageConstants.WE_CHAT_RESPONSE_STATUS_CODE))
                        + ",错误信息：" + String.valueOf(map.get(MessageConstants.WE_CHAT_RESPONSE_ERROR_INFO)));
            }
            if ("40001".equals(String.valueOf(map.get("errcode")))) {
                accessToken = this.weChatAccessTokenService.getToken();
                this.noticeSingle(data, accessToken, templateId, retryNum + 1);
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
            template.put("color", templateTextColor);
        });
        return JSONObject.fromObject(thirdParams).toString();
    }

    /**
     * 列举数据
     *
     * @param dataSql
     * @param params
     * @return
     */
    private List<Map<String, Object>> listData(String dataSql, Map<String, Object> params) {
        if (params.get(CommonConstant.PARAM_IDS) instanceof String) {
            params.put(CommonConstant.PARAM_IDS, ((String) params.get(CommonConstant.PARAM_IDS))
                    .split(CommonConstant.SPLIT_ID_SIGN));
        }
        dataSql = SQLHandleUtils.replaceSignUseParams(dataSql, params);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                dataSql = dataSql.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
        }
        List<Map<String, Object>> data = this.currentGeneralDao.getMapBySQL(dataSql);
        if (CollectionUtils.isEmpty(data)) {
            throw new ServiceException("没有可发送的数据或所选用户都未关注公众号");
        }
        return data;
    }

    /**
     * 获得模板配置
     *
     * @param code
     * @param siteCode
     * @return
     */
    private Map<String, Object> getTemplateConfig(String code, String siteCode) {
        String dataSourceCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                     ");
        sql.append(" 	g.data_sql as dataSql,                                                  ");
        sql.append(" 	s.template_id as templateId                                             ");
        sql.append(" FROM                                                                       ");
        sql.append(" 	wechat_template_message_group g                                         ");
        sql.append(" INNER JOIN wechat_template_message_site s ON s.fk_template_group_id = g.id ");
        sql.append(" WHERE                                                                      ");
        sql.append(" 	g.code = '" + code + "'                                                 ");
        sql.append(" AND s.fk_web_site_id = '" + SiteUtil.getSite(siteCode).getId() + "'        ");
        Map<String, Object> config = this.openGeneralDao.getOneMapBySQL(sql.toString());
        if (config == null) {
            throw new ParameterIllegalException();
        }
        MasterSlaveRoutingDataSource.setDbType(dataSourceCode);
        return config;
    }

}
