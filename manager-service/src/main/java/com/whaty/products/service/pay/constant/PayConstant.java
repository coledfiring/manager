package com.whaty.products.service.pay.constant;

/**
 * 支付常量池
 *
 * @author weipengsen
 */
public interface PayConstant {

    /**
     * 参数，支付方式
     */
    String PARAM_PAY_TYPE = "payType";
    /**
     * 参数，订单类型
     */
    String PARAM_ORDER_TYPE = "orderType";
    /**
     * 参数，订单信息
     */
    String PARAM_ORDER_INFO = "orderInfo";

    /**
     * 资源配置，支付地址
     */
    String PROP_PAY_URL = "weChat.pay.payUrl";
    /**
     * 资源配置，商户号
     */
    String PROP_MCH_ID = "weChat.pay.mchId";
    /**
     * 资源配置，appId
     */
    String PROP_APP_ID = "weChat.pay.appId";
    /**
     * 资源配置，商户API密钥
     */
    String PROP_KEY = "weChat.pay.key";
    /**
     * 支付配置路径
     */
    String PAY_PROP_PATH = "pay/pay.properties";

    /**
     * 支付订单类型，学生报名订单
     */
    String PAY_ORDER_TYPE_STUDENT_ENROLL_ORDER = "studentEnrollOrder";
    /**
     * 支付订单类型，教材订阅支付
     */
    String PAY_ORDER_TYPE_MATERIAL_SOLICIT = "materialSolicit";
    /**
     * 支付订单类型，在线培训课程报名支付
     */
    String PAY_ORDER_TYPE_OL_STUDENT_COURSE_ENROLL = "olStudentCourseEnroll";
    /**
     * 支付订单类型，在线培训班级报名支付
     */
    String PAY_ORDER_TYPE_OL_STUDENT_CLASS_ENROLL = "olStudentClassEnroll";
    /**
     * 资源配置，通知地址
     */
    String PROP_NOTIFY_URL = "weChat.pay.notifyUrl";
    /**
     * 常量命名空间，是否支付（百校千课的常量）
     */
    String ENUM_CONST_NAMESPACE_IS_PAYED = "flagIsPayed";
    /**
     * 常量命名空间，是否支付（山东考试系统的常量）
     */
    String ENUM_CONST_NAMESPACE_IS_PAY = "flagIsPay";
    /**
     * 常量命名空间，支付方式（山东考试系统的常量）
     */
    String ENUM_CONST_NAMESPACE_EXAM_PAY_WAY = "flagExamPayWay";
    /**
     * 参数，订单号
     */
    String PARAM_ORDER_NO = "orderNo";
    /**
     * 订单类型，学费
     */
    String PAY_ORDER_TYPE_STUDY_FEE = "studyFee";
    /**
     * 订单类型，特殊考试费
     */
    String PAY_ORDER_TYPE_SPECIAL_EXAM_FEE = "specialExamFee";
    /**
     * 订单类型，英语测试正式机考费用
     */
    String PAY_ORDER_TYPE_YYCS_REGULAR_EXAM_FEE = "yycsRegularExamFee";
    /**
     * 订单类型，英语测试考前辅导、模拟练习费用
     */
    String PAY_ORDER_TYPE_YYCS_MOCK_EXAM_FEE = "yycsMOCKExamFee";
    /**
     * 参数，用户id
     */
    String PARAM_USER_ID = "userId";
    /**
     * 是否已支付常量命名空间
     */
    String ENUM_CONST_NAMESPACE_PAYED = "flagPayed";
    /**
     * 关闭订单地址
     */
    String PROP_CLOSE_URL = "wechat.pay.closeUrl";
    /**
     * 支付方式：支付宝
     */
    String PAY_WAY_ALIPAY = "2";
    /**
     * 支付方式：微信支付
     */
    String PAY_WAY_WX = "1";

    /**
     * 支付宝交易状态：交易支付成功
     */
    String ALIPAY_TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";
    /**
     * 支付宝交易状态：交易结束，不可退款
     */
    String ALIPAY_TRADE_STATUS_FINISHED = "TRADE_FINISHED";
    /**
     * 订单查询：支付状态
     */
    String ORDER_QUERY_TRADE_STATUS_KEY = "trade_state";
    /**
     * 订单查询：支付金额
     */
    String ORDER_QUERY_TRADE_AMOUNT_KEY = "total_fee";

}
