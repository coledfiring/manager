package com.whaty.products.service.pay.strategy.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.whaty.domain.bean.SitePayInfo;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.pay.command.AbstractPayOrderCommand;
import com.whaty.products.service.pay.constant.PayConstant;
import com.whaty.products.service.pay.strategy.AbstractPayStrategy;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝pc端支付策略
 *
 * @author suoqiangqiang
 */
@Lazy
@Component("alipayNativeStrategy")
public class AlipayNativeStrategy extends AbstractPayStrategy {

    @Override
    public Map<String, Object> generateOrder(Map<String, Object> orderParams, AbstractPayOrderCommand command) throws IOException, DocumentException {
        throw new IOException();
    }

    @Override
    public Map<String, Object> generateOrderWithoutThirdParty(Map<String, Object> orderParams, AbstractPayOrderCommand command) throws IOException, DocumentException {
        throw new IOException();
    }

    @Override
    public Map<String, Object> payOrderAgain(String orderNo, AbstractPayOrderCommand command) throws IOException,
            DocumentException {
        SitePayInfo sitePayInfo = SiteUtil.getPayInfo(SiteUtil.getSiteCode(), PayConstant.PAY_WAY_ALIPAY);
        if (sitePayInfo == null) {
            throw new ServiceException("平台暂无支付宝账户信息，请联系管理员完善账户信息");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(sitePayInfo.getAlipayGateway(), sitePayInfo.getAppId(),
                sitePayInfo.getAlipaySecretKey(), "JSON", "utf-8", sitePayInfo.getAlipayPublicKey(), "RSA2");
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        Map<String, Object> orderInfo = command.payOrderAgain(orderNo);
        alipayRequest.setNotifyUrl(sitePayInfo.getAlipayNotifyUrl());
        String returnUrl = (String) orderInfo.get("returnUrl");
        if (StringUtils.isNotBlank(returnUrl)) {
            alipayRequest.setReturnUrl(returnUrl);
        }
        String timeExpire = (String) orderInfo.get("timeExpire");
        String timeoutExpress = (String) orderInfo.get("timeoutExpress");
        timeoutExpress = StringUtils.isNotBlank(timeoutExpress) ? "    \"timeout_express\":\"" + timeoutExpress + "\"," : "";
        timeExpire = StringUtils.isNotBlank(timeExpire) ? "    \"time_expire\":\"" + timeExpire + "\"," : "";
        // 填充业务参数
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + orderInfo.get("orderNo") + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + orderInfo.get("totalPrice") + "," +
                timeoutExpress + timeExpire +
                "    \"subject\":\"" + orderInfo.get("description") + "\"," +
                "    \"body\":\"" + orderInfo.get("attach") + "\"" +
                "  }");
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            throw new ServiceException("获取支付宝第三方支付表单失败");
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("form", form);
        return resultMap;
    }

    @Override
    public Map<String, Object> getOrderInfo(String orderNo, AbstractPayOrderCommand command) throws IOException, DocumentException {
        Map<String, Object> tradeMap = new HashMap<>();
        SitePayInfo sitePayInfo = SiteUtil.getPayInfo(SiteUtil.getSiteCode(), PayConstant.PAY_WAY_ALIPAY);
        AlipayClient alipayClient = new DefaultAlipayClient(sitePayInfo.getAlipayGateway(), sitePayInfo.getAppId(),
                sitePayInfo.getAlipaySecretKey(), "JSON", "utf-8", sitePayInfo.getAlipayPublicKey(), "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + orderNo + "\"" +
                "  }");
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            tradeMap.put(PayConstant.ORDER_QUERY_TRADE_STATUS_KEY, response.getTradeStatus());
            tradeMap.put(PayConstant.ORDER_QUERY_TRADE_AMOUNT_KEY, response.getTotalAmount());
        } catch (AlipayApiException e) {
            throw new ServiceException("获取支付宝第三方支付表单失败");
        }
        return tradeMap;
    }
}
