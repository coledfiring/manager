package com.whaty.products.service.pay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.products.service.pay.PayService;
import com.whaty.products.service.pay.command.AbstractPayOrderCommand;
import com.whaty.products.service.pay.constant.AlipayApiConstant;
import com.whaty.products.service.pay.constant.PayConstant;
import com.whaty.products.service.pay.constant.WeChatApiConstant;
import com.whaty.products.service.pay.factory.PayOrderCommandFactory;
import com.whaty.products.service.pay.factory.PayStrategyFactory;
import com.whaty.products.service.pay.strategy.AbstractPayStrategy;
import com.whaty.util.CommonUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 微信支付服务实现类
 *
 * @author weipengsen
 */
@Lazy
@Service("payServiceImpl")
public class PayServiceImpl implements PayService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    private final static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Override
    @LogAndNotice("生成在线支付订单")
    public Map<String, Object> doGenerateOrder(Map<String, Object> orderParams, String payType, String orderType)
            throws IOException, DocumentException {
        AbstractPayStrategy strategy = this.getPayStrategy(payType);
        AbstractPayOrderCommand command = this.getPayOrderCommand(orderType);
        return strategy.generateOrder(orderParams, command);
    }

    @Override
    @LogAndNotice("回调完成微信在线支付订单")
    public void doFinishWeChatOrder(HttpServletResponse response, String notifyXml) throws DocumentException {
        Map<String, Object> params = CommonUtils.convertXmlStringToMap(notifyXml);
        String orderType = (String) params.get(WeChatApiConstant.PARAM_ATTACH);
        String orderNo = (String) params.get(WeChatApiConstant.PARAM_OUT_TRADE_NO);
        String payType = (String) params.get(WeChatApiConstant.PARAM_TRADE_TYPE);
        this.finishWeChatOrder(response, orderNo, payType, orderType, params);
    }

    /**
     * 回调完成订单
     * @param response
     * @param orderNo
     * @param payType
     * @param orderType
     * @param params
     */
    private void finishWeChatOrder(HttpServletResponse response, String orderNo, String payType,
                                   String orderType, Map<String, Object> params) {
        String msg;
        // 检查是否已保存成功的第三方回调信息
        List<Object> callDataList = this.myGeneralDao.getBySQL("select 1 from third_pay_data where order_no = ? " +
                "AND return_code = 'success' and site_code = ?", orderNo, SiteUtil.getSiteCode());
        if (CollectionUtils.isNotEmpty(callDataList)) {
            msg = "success";
        } else {
            try {
                try {
                    if ("SUCCESS".equals(params.get(WeChatApiConstant.FINISH_RESPONSE_RETURN_CODE))) {
                        this.getPayOrderCommand(orderType).finishOrder(orderNo, PayConstant.PAY_WAY_ALIPAY);
                    }
                } finally {
                    // 保存第三方信息
                    saveThirdPayData(params, orderNo, orderType, payType);
                }
                msg = "success";
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("交费失败，params:" + params, e);
                }
                msg = "fail";
            }
        }
        writeResponseInfo(response, msg, params);
    }

    @Override
    @LogAndNotice("再次支付订单")
    public Map<String, Object> doPayOrderAgain(String orderNo, String payType, String orderType)
            throws IOException, DocumentException {
        AbstractPayStrategy strategy = this.getPayStrategy(payType);
        AbstractPayOrderCommand command = this.getPayOrderCommand(orderType);
        return strategy.payOrderAgain(orderNo, command);
    }

    @Override
    @LogAndNotice("只生成订单不发起支付")
    public Map<String, Object> doGenerateOrderOnly(Map<String, Object> orderParams, String orderType) throws IOException, DocumentException {
        AbstractPayOrderCommand command = this.getPayOrderCommand(orderType);
        return command.createAndSaveOrder(orderParams);
    }

    @Override
    @LogAndNotice("回调完成支付宝在线支付订单")
    public void doFinishAlipayOrder(HttpServletResponse response, Map<String, String> paramsMap) throws DocumentException,
            AlipayApiException {
        String msg = "failure";
        String status = paramsMap.get(AlipayApiConstant.ALIPAY_ASYNC_PARAMS_TRADE_STATUS);
        String orderType = paramsMap.get(AlipayApiConstant.ALIPAY_ASYNC_PARAMS_BODY);
        String orderNo = paramsMap.get(AlipayApiConstant.ALIPAY_ASYNC_PARAMS_OUT_TRADE_NO);
        AbstractPayOrderCommand command = this.getPayOrderCommand(orderType);
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(paramsMap,
                    SiteUtil.getPayInfo(SiteUtil.getSiteCode(), PayConstant.PAY_WAY_ALIPAY).getAlipayPublicKey(), "utf-8", "RSA2");
        } catch (AlipayApiException e) {
            // 签名验证失败，查询订单是否已完成
            try {
                Map<String, Object> orderInfo = this.getPayStrategy("alipayNative").getOrderInfo(orderNo, command);
                status = (String) orderInfo.get(PayConstant.ORDER_QUERY_TRADE_STATUS_KEY);
                // 订单已完成，调用订单完成功能
                if (AlipayApiConstant.ALIPAY_TRADE_STATUS_SUCCESS.equalsIgnoreCase(status)) {
                    command.finishOrder(orderNo, PayConstant.PAY_WAY_ALIPAY);
                    msg = "success";
                    saveThirdPayData(paramsMap, orderNo, msg, "aliPay");
                    writeResponseInfo(response, msg, paramsMap);
                    return;
                }
            } catch (Exception e1) {
                // 订单查询或完成订单异常，保存第三方支付信息
                msg = e1.getMessage();
                saveThirdPayData(paramsMap, orderNo, msg, "aliPay");
                return;
            }
        }
        if (signVerified) {
            try {
                if (PayConstant.ALIPAY_TRADE_STATUS_SUCCESS.equals(status)) {
                    this.getPayOrderCommand(orderType).finishOrder(orderNo, PayConstant.PAY_WAY_ALIPAY);
                    msg = "success";
                }
            } finally {
                // 保存第三方信息
                saveThirdPayData(paramsMap, orderNo, msg, "aliPay");
            }
        } else {
            // 保存第三方参数信息信息
            saveThirdPayData(paramsMap, orderNo, "check false", "aliPay");
        }
        writeResponseInfo(response, msg, paramsMap);
    }

    /**
     * 获取支付策略
     * @param payType
     * @return
     */
    private AbstractPayStrategy getPayStrategy(String payType) {
        return PayStrategyFactory.newInstance(payType);
    }

    /**
     * 获取支付订单命令对象
     * @param orderType
     * @return
     */
    private AbstractPayOrderCommand getPayOrderCommand(String orderType) {
        return PayOrderCommandFactory.newInstance(orderType);
    }

    /**
     * 写入响应信息
     * @param response
     * @param msg
     */
    private void writeResponseInfo(HttpServletResponse response, String msg, Map params) {
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().println(msg);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("交费失败，写入响应信息失败，params:" + params, e);
            }
        }
    }

    /**
     * 保存第三方支付结果
     * @param params
     * @param orderNo
     * @param orderType
     * @param payType
     */
    private void saveThirdPayData(Map params, String orderNo, String orderType, String payType) {
        // 保存第三方信息
        String responseData = JSONObject.fromObject(params).toString();
        String sql = "insert into third_pay_data(order_no, response_data, pay_type, order_type, "
                + " return_code, site_code) values('"
                + orderNo + "', '" + responseData + "', '" + payType + "', '" + orderType + "', '"
                + (("SUCCESS".equals(params.get(WeChatApiConstant.FINISH_RESPONSE_RETURN_CODE))
                || "TRADE_SUCCESS".equals(params.get(AlipayApiConstant.ALIPAY_ASYNC_PARAMS_TRADE_STATUS)))
                ? "success" : "failure") + "', '" + SiteUtil.getSiteCode() + "')";
        this.myGeneralDao.executeBySQL(sql);
    }

    /**
     * 生成微信签名
     *
     * @param params
     * @return
     */
    public String generateWeChatSign(Map params) {
        StringBuilder sign = new StringBuilder();
        params.forEach((k, v) -> {
            if (StringUtils.isNotBlank((String) k) && v != null && StringUtils.isNotBlank(String.valueOf(v))) {
                sign.append(k).append("=").append(v).append("&");
            }
        });
        sign.append("key=").append(SiteUtil.getPayInfo(SiteUtil.getSiteCode(),
                PayConstant.PAY_WAY_WX).getWechatApiKey());
        return CommonUtils.md5(sign.toString()).toUpperCase();
    }

}
