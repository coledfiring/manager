package com.whaty.products.service.pay.strategy.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.whaty.common.string.StringUtils;
import com.whaty.domain.bean.SitePayInfo;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.pay.command.AbstractPayOrderCommand;
import com.whaty.products.service.pay.constant.PayConstant;
import org.dom4j.DocumentException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝app端支付策略
 *
 * @author suoqiangqiang
 */
@Lazy
@Component("alipayAppStrategy")
public class AlipayAppStrategy extends AlipayNativeStrategy {

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
        Map<String, Object> tradeMap = new HashMap<>();
        SitePayInfo sitePayInfo = SiteUtil.getPayInfo(SiteUtil.getSiteCode(), PayConstant.PAY_WAY_ALIPAY);
        AlipayClient alipayClient = new DefaultAlipayClient(sitePayInfo.getAlipayGateway(), sitePayInfo.getAppId(),
                sitePayInfo.getAlipaySecretKey(), "JSON", "utf-8", sitePayInfo.getAlipayPublicKey(), "RSA2");
        Map<String, Object> orderInfo = command.payOrderAgain(orderNo);
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject((String) orderInfo.get("description"));
        model.setTotalAmount(orderInfo.get("totalPrice").toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        model.setOutTradeNo(orderNo);
        model.setBody((String) orderInfo.get("attach"));
        if (StringUtils.isNotBlank((String) orderInfo.get("timeExpire"))) {
            model.setTimeExpire((String) orderInfo.get("timeExpire"));
        }
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(sitePayInfo.getAlipayNotifyUrl());
        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            tradeMap.put("orderString", response.getBody());
            return tradeMap;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new ServiceException("获取支付宝第三方支付信息失败");
        }
    }
}
