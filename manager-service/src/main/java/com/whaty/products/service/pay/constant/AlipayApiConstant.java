package com.whaty.products.service.pay.constant;

/**
 * 支付宝接口使用的参数常量池
 *
 * @author suoqiangqiang
 */
public interface AlipayApiConstant {
    /**
     * 支付宝回调参数：交易状态
     */
    String ALIPAY_ASYNC_PARAMS_TRADE_STATUS = "trade_status";
    /**
     * 支付宝回调参数：商品详情
     */
    String ALIPAY_ASYNC_PARAMS_BODY = "body";
    /**
     * 支付宝回调参数：订单号
     */
    String ALIPAY_ASYNC_PARAMS_OUT_TRADE_NO = "out_trade_no";
    /**
     * 支付宝交易状态：交易支付成功
     */
    String ALIPAY_TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";
    /**
     * 支付宝交易状态：交易结束，不可退款
     */
    String ALIPAY_TRADE_STATUS_FINISHED = "TRADE_FINISHED";
}
