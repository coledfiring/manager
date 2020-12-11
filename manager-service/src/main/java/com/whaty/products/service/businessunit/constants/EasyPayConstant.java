package com.whaty.products.service.businessunit.constants;

/**
 * 北交大易智付对接在线支付常量
 *
 * @author shanshuai
 */
public interface EasyPayConstant {

    /**
     * 首信易支付单笔订单查询接口
     */
    String EASY_PAY_SINGLE_CHECK_URL = "https://pay.yizhifubj.com/merchant/order/order_ack_oid_list.jsp";

    /**
     * 北交大易智付商户号
     */
    String EASY_PAY_MID = "9261";

    /**
     * 加密密钥
     */
    String EASY_PAY_KEY = "test";

    /**
     * 易智付同步回调时v_pstatus 支付状态 20：支付成功
     */
    int EASY_PAY_STATUS_SYNC_IS_OK = 20;

    /**
     * 易智付同步回调时v_pstatus 支付状态 30：支付失败
     */
    int EASY_PAY_STATUS_SYNC_IS_FAIL = 30;

    /**
     * 易智付同步回调时v_pstatus 支付状态 1：已提交
     */
    int EASY_PAY_STATUS_SYNC_IS_SUBMIT = 1;

    /**
     * 易智付异步回调时v_pstatus 支付状态 1：支付完成
     */
    int EASY_PAY_STATUS_ASYNC_IS_OK = 1;

    /**
     * 易智付允许更新订单号的默认时间间隔 ：分钟
     */
    int EASY_PAY_ORDER_NO_UPDATE_INTERVAL = 30;

}
