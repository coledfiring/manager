package com.whaty.products.service.pay.strategy.wechat;

import com.whaty.domain.bean.SitePayInfo;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.httpClient.domain.HttpClientResponseData;
import com.whaty.framework.httpClient.helper.HttpClientHelper;
import com.whaty.products.service.pay.command.AbstractPayOrderCommand;
import com.whaty.products.service.pay.constant.PayConstant;
import com.whaty.products.service.pay.constant.WeChatApiConstant;
import com.whaty.products.service.pay.strategy.AbstractPayStrategy;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * 微信支付抽象策略
 *
 * @author weipengsen
 */
public abstract class AbstractWeChatPayStrategy extends AbstractPayStrategy {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWeChatPayStrategy.class);
    /**
     * 支付配置对象
     */
    protected static Properties prop;
    /**
     * 支付url
     */
    protected static String payUrl;
    /**
     * 商户号
     */
    protected static String mchId;
    /**
     * appId
     */
    protected static String appId;
    /**
     * 商户平台API密钥
     */
    protected static String key;

    @Override
    public Map<String, Object> generateOrder(Map<String, Object> orderParams,
                                             AbstractPayOrderCommand command) throws IOException, DocumentException {
        Map<String, Object> orderInfo = this.generateAndSaveOrder(orderParams, command);
        return this.generateThirdPartyOrder(orderInfo);
    }

    @Override
    public Map<String, Object> generateOrderWithoutThirdParty(Map<String, Object> orderParams, AbstractPayOrderCommand command) throws IOException, DocumentException {
        return this.generateAndSaveOrder(orderParams, command);
    }

    /**
     * 创建业务订单的方法
     * @param orderParams 订单信息
     * @param command 命令对象
     * @return
     */
    private Map<String, Object> generateAndSaveOrder(Map<String, Object> orderParams,
                                                    AbstractPayOrderCommand command) {
        return command.createAndSaveOrder(orderParams);
    }

    @Override
    public Map<String, Object> payOrderAgain(String orderNo, AbstractPayOrderCommand command)
            throws IOException, DocumentException {
        Map<String, Object> orderInfo = command.payOrderAgain(orderNo);
        return this.generateThirdPartyOrder(orderInfo);
    }

    @Override
    public Map<String, Object> getOrderInfo(String orderNo, AbstractPayOrderCommand command)
            throws IOException, DocumentException {
        String orderParams = this.collectThirdPartyOrderQueryInfo(orderNo);
        return this.invokeThirdParty(orderParams, WeChatApiConstant.WX_ORDER_QUERY_URL);
    }

    /**
     * 生成第三方订单
     *
     * @param orderInfo
     * @return
     */
    protected Map<String, Object> generateThirdPartyOrder(Map<String, Object> orderInfo)
            throws IOException, DocumentException {
        String orderParams = this.collectThirdPartyOrder(orderInfo);
        Map<String, Object> responseData = this.invokeThirdParty(orderParams, getSitePayInfo().getWechatPayUrl());
        return this.convertResponseDataToViewData(orderInfo, responseData);
    }

    /**
     * 将响应数据转换为视图数据
     *
     *
     * @param orderInfo
     * @param responseData
     * @return
     */
    protected Map<String, Object> convertResponseDataToViewData(Map<String, Object> orderInfo, Map<String, Object> responseData) {
        if (WeChatApiConstant.RESPONSE_STATUS_FAIL.equals(responseData.get(WeChatApiConstant.RESPONSE_STATUS))) {
            throw new RuntimeException("第三方调用失败，" + responseData.get(WeChatApiConstant.RESPONSE_MSG));
        }
        if (WeChatApiConstant.RESPONSE_BUSINESS_STATUS_FAIL.equals(responseData
                .get(WeChatApiConstant.RESPONSE_BUSINESS_STATUS))) {
            throw new ServiceException((String) responseData
                    .get(WeChatApiConstant.RESPONSE_BUSINESS_ERR_DESCRIPTION));
        }
        return this.convertDataToViewData(orderInfo, responseData);
    }

    /**
     * 将响应数据转换为视图数据
     *
     *
     * @param orderInfo
     * @param responseData
     * @return
     */
    protected abstract Map<String, Object> convertDataToViewData(Map<String, Object> orderInfo, Map<String, Object> responseData);

    /**
     * 根据生成的业务订单信息收集第三方需要的数据格式
     *
     * @param orderInfo
     * @return
     */
    protected String collectThirdPartyOrder(Map<String, Object> orderInfo) {
        Map<String, String> params = new TreeMap<>();
        Map<String, String> extraParams = this.addThirdPartyExtraParams(orderInfo);
        if (MapUtils.isNotEmpty(extraParams)) {
            params.putAll(extraParams);
        }
        String notifyUrl = String.format(getSitePayInfo().getWechatNotifyUrl(), SiteUtil.getSite().getDomain());
        params.put(WeChatApiConstant.PARAM_NOTIFY_URL, notifyUrl);
        params.put(WeChatApiConstant.PARAM_APP_ID,
                orderInfo.containsKey(WeChatApiConstant.PARAM_APP_ID) ?
                        (String) orderInfo.get(WeChatApiConstant.PARAM_APP_ID) : getSitePayInfo().getAppId());
        params.put(WeChatApiConstant.PARAM_MCH_ID,
                orderInfo.containsKey(WeChatApiConstant.PARAM_MCH_ID) ?
                        (String) orderInfo.get(WeChatApiConstant.PARAM_MCH_ID) : getSitePayInfo().getWechatMchId());
        params.put(WeChatApiConstant.PARAM_NONCE_STR, (String) orderInfo.get("orderNo"));
        params.put(WeChatApiConstant.PARAM_BODY, (String) orderInfo.get("description"));
        params.put(WeChatApiConstant.PARAM_OUT_TRADE_NO, (String) orderInfo.get("orderNo"));
        params.put(WeChatApiConstant.PARAM_TOTAL_FEE,
                String.valueOf(((BigDecimal) orderInfo.get("totalPrice"))
                        .multiply(new BigDecimal(100)).intValue()));
        params.put(WeChatApiConstant.PARAM_SP_BILL_CREATE_IP, CommonUtils.getIpAddress());
        params.put(WeChatApiConstant.PARAM_TRADE_TYPE, this.getTradeType());
        params.put(WeChatApiConstant.PARAM_ATTACH, (String) orderInfo.get("attach"));
        params.put(WeChatApiConstant.PARAM_SIGN, this.generateSign(params));
        return CommonUtils.convertMapToXML(params);
    }

    /**
     * 模板方法，添加额外的第三方接口参数
     *
     * @param orderInfo
     * @return
     */
    protected Map<String, String> addThirdPartyExtraParams(Map<String, Object> orderInfo) {
        return null;
    }

    /**
     * 生成签名
     *
     * @param params
     * @return
     */
    protected String generateSign(Map params) {
        StringBuilder sign = new StringBuilder();
        params.forEach((k, v) -> {
            if (StringUtils.isNotBlank((String) k) && v != null && StringUtils.isNotBlank(String.valueOf(v))) {
                sign.append(k).append("=").append(v).append("&");
            }
        });
        sign.append("key=").append(getSitePayInfo().getWechatApiKey());
        return CommonUtils.md5(sign.toString()).toUpperCase();
    }

    /**
     * 执行第三方接口
     *
     * @param url
     * @param orderParams
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    protected Map<String, Object> invokeThirdParty(String orderParams, String url)
            throws IOException, DocumentException {
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        HttpClientResponseData response = httpClientHelper.doPostEntity(url, orderParams);
        if (HttpStatus.OK.value() != response.getStatus()) {
            throw new HttpException("支付请求错误状态码:" + 200 + ",参数:" + orderParams);
        }
        return CommonUtils.convertXmlStringToMap(response.getContent());
    }


    /**
     * 获取交易类型
     *
     * @return
     */
    protected abstract String getTradeType();

    /**
     * 根据订单号生成第三方查询订单数据需要的数据格式
     * @param orderNo
     * @return
     */
    protected String collectThirdPartyOrderQueryInfo(String orderNo) {
        Map<String, String> params = new TreeMap<>();
        params.put(WeChatApiConstant.PARAM_APP_ID, getSitePayInfo().getAppId());
        params.put(WeChatApiConstant.PARAM_MCH_ID, getSitePayInfo().getWechatMchId());
        params.put(WeChatApiConstant.PARAM_OUT_TRADE_NO, orderNo);
        params.put(WeChatApiConstant.PARAM_NONCE_STR, orderNo);
        params.put(WeChatApiConstant.PARAM_SIGN, this.generateSign(params));
        return CommonUtils.convertMapToXML(params);
    }

    /**
     * 获取支付方式信息
     * @return
     */
    private SitePayInfo getSitePayInfo () {
        return SiteUtil.getPayInfo(SiteUtil.getSiteCode(), PayConstant.PAY_WAY_WX);
    }

}
