package com.whaty.products.service.pay;

import com.alipay.api.AlipayApiException;
import org.dom4j.DocumentException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 微信支付服务接口
 *
 * @author weipengsen
 */
public interface PayService {

    /**
     * 生成订单
     * @param orderParams
     * @param orderType
     * @param payType
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    Map<String, Object> doGenerateOrder(Map<String, Object> orderParams, String payType, String orderType)
            throws IOException, DocumentException;

    /**
     * 完成微信订单
     * @param response
     * @param notifyXml
     * @throws DocumentException
     */
    void doFinishWeChatOrder(HttpServletResponse response, String notifyXml) throws DocumentException;

    /**
     * 重新支付
     * @param orderNo
     * @param payType
     * @param orderType
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    Map<String,Object> doPayOrderAgain(String orderNo, String payType, String orderType) throws IOException, DocumentException;

    /**
     * 生成订单不发起支付
     * @param orderParams
     * @param orderType
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    Map<String,Object> doGenerateOrderOnly(Map<String, Object> orderParams, String orderType) throws IOException, DocumentException;

    /**
     * 完成支付宝订单
     * @param response
     * @param paramsMap
     * @throws DocumentException
     */
    void doFinishAlipayOrder(HttpServletResponse response, Map<String, String> paramsMap) throws DocumentException, AlipayApiException;

    /**
     * 生成微信签名
     *
     * @param params
     * @return
     */
    String generateWeChatSign(Map params);
}
