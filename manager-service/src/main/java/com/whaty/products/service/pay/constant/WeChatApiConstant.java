package com.whaty.products.service.pay.constant;

/**
 * 微信接口使用的参数常量池
 *
 * @author weipengsen
 */
public interface WeChatApiConstant {

    /**
     * appId
     */
    String PARAM_APP_ID = "appid";
    /**
     * 商户号
     */
    String PARAM_MCH_ID = "mch_id";
    /**
     * 随机码
     */
    String PARAM_NONCE_STR = "nonce_str";
    /**
     * 二维码链接
     */
    String PARAM_CODE_URL = "code_url";
    /**
     * 商品描述
     */
    String PARAM_BODY = "body";
    /**
     * 订单号
     */
    String PARAM_OUT_TRADE_NO = "out_trade_no";
    /**
     * 总金额
     */
    String PARAM_TOTAL_FEE = "total_fee";
    /**
     * 客户端ip
     */
    String PARAM_SP_BILL_CREATE_IP = "spbill_create_ip";
    /**
     * 异步通知地址
     */
    String PARAM_NOTIFY_URL = "notify_url";
    /**
     * 支付类型
     */
    String PARAM_TRADE_TYPE = "trade_type";
    /**
     * 场景信息
     */
    String PARAM_SCENE_INFO = "scene_info";
    /**
     * 签名
     */
    String PARAM_SIGN = "sign";
    /**
     * 场景信息中的根
     */
    String PARAM_H5_INFO = "h5_info";
    /**
     * 场景信息中的类型
     */
    String PARAM_TYPE = "type";
    /**
     * 场景信息中的url
     */
    String PARAM_WAP_URL = "wap_url";
    /**
     * 场景信息中的网站名
     */
    String PARAM_WAP_NAME = "wap_name";
    /**
     * 公众号openId
     */
    String PARAM_OPEN_ID = "openid";
    /**
     * 商品ID
     */
    String PARAM_PRODUCT_ID = "product_id";

    /**
     * 返回状态
     */
    String RESPONSE_STATUS = "return_code";
    /**
     * 返回状态，失败
     */
    String RESPONSE_STATUS_FAIL = "FAIL";
    /**
     * 返回信息
     */
    String RESPONSE_MSG = "return_msg";
    /**
     * 返回数据，业务状态
     */
    String RESPONSE_BUSINESS_STATUS = "result_code";
    /**
     * 返回数据，业务状态为FAIL
     */
    String RESPONSE_BUSINESS_STATUS_FAIL = "FAIL";
    /**
     * 返回数据，业务错误描述
     */
    String RESPONSE_BUSINESS_ERR_DESCRIPTION = "err_code_des";
    /**
     * 商家数据包
     */
    String PARAM_ATTACH = "attach";
    /**
     * 回调通知url
     */
    String RESPONSE_NOTIFY_URL = "mweb_url";
    /**
     * 预支付订单id
     */
    String PARAM_PREPAY_ID = "prepay_id";
    /**
     * 回调响应code
     */
    String FINISH_RESPONSE_RETURN_CODE = "return_code";
    /**
     * 回调响应msg
     */
    String FINISH_RESPONSE_RETURN_MSG = "return_msg";

    /**
     * jsApi的支付类型
     */
    String JS_API_TRADE_TYPE = "JSAPI";
    /**
     * NATIVE的支付类型
     */
    String NATIVE_TRADE_TYPE = "NATIVE";
    /**
     * h5的支付类型
     */
    String H5_TRADE_TYPE = "MWEB";
    /**
     * app的支付类型
     */
    String APP_TRADE_TYPE = "APP";
    /**
     * 微信订单查询url
     */
    String WX_ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
}
