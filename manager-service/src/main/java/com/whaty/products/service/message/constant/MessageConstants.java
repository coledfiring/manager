package com.whaty.products.service.message.constant;

/**
 * 常量池
 *
 * @author weipengsen
 */
public interface MessageConstants {
    /**
     * 参数：接受者
     */
    String TOS = "tos";
    /**
     * 参数：文本内容
     */
    String CONTENT = "content";
    /**
     * 参数：accessToken
     */
    String ACCESS_TOKEN = "accessToken";
    /**
     * 发送群发信息的url资源键名
     */
    String PROP_GROUP_SEND_TEXT_MESSAGE = "weChat.groupNotice.url";

    /**
     * 发送模板信息的url资源键名
     */
    String PROP_TEMPLATE_SEND_TEXT_MESSAGE = "weChat.templateNotice.url";
    /**
     * 资源，模板文字颜色
     */
    String PROP_TEMPLATE_TEXT_COLOR = "weChat.templateTextColor";

    /**
     * 资源文件路径
     */
    String PROP_FILE_PATH = "weChat/weChat.properties";

    /**
     * 微信接口参数：接受者
     */
    String PARAM_TO_USER = "touser";
    /**
     * 微信接口参数：文本
     */
    String PARAM_TEXT = "text";
    /**
     * 微信接口参数：内容
     */
    String PARAM_CONTENT = "content";
    /**
     * 微信接口参数：格式
     */
    String PARAM_MSG_TYPE = "msgtype";

    /**
     * 微信调用发送接口的返回json中状态code的key
     */
    String WE_CHAT_RESPONSE_STATUS_CODE = "errcode";
    /**
     * 微信的信息发送调用接口的返回json中成功的状态code
     */
    String RESPONSE_STATUS_CODE_SUCCESS = "0";
    /**
     * 微信的信息发送调用接口的返回json中的错误信息
     */
    String WE_CHAT_RESPONSE_ERROR_INFO = "errmsg";
    /**
     * 微信接口值：文本格式
     */
    String MSG_TYPE_TEXT = "text";
    /**
     * 参数，模板code
     */
    String PARAM_TEMPLATE_CODE = "code";
    /**
     * 参数，模板ID
     */
    String PARAM_TEMPLATE_ID = "templateId";
    /**
     * 参数，信息类型
     */
    String PARAM_MESSAGE_TYPE = "messageType";
    /**
     * 参数，名称
     */
    String PARAM_NAME = "name";
    /**
     * 参数id
     */
    String PARAM_ID = "id";
    /**
     * 转化信息配置时使用的参数，name
     */
    String PARAM_CONVERT_MESSAGE_CONFIG_NAME = "name";
    /**
     * 转化信息配置时使用的参数， triggerDate
     */
    String PARAM_CONVERT_MESSAGE_CONFIG_TRIGGER_DATE = "triggerDate";
    /**
     * 转化信息配置时使用的参数， messageConfigId
     */
    String PARAM_CONVERT_MESSAGE_CONFIG_MESSAGE_CONFIG_ID = "messageConfigId";
    /**
     * 信息推送策略组
     */
    String SCHEDULE_MESSAGE_NOTICE_GROUP = "messageNotice";
    /**
     * 常量命名空间，调度任务是否有效
     */
    String ENUM_CONST_NAMESPACE_JOB_VALID = "flagJobValid";
    /**
     * 常量命名空间，定时任务是否同时进行
     */
    String ENUM_CONST_NAMESPACE_IS_CONCURRENT = "FlagIsConcurrent";
    /**
     * 信息配置，调度方法名，invoke
     */
    String MESSAGE_CONFIG_INVOKE_METHOD = "invoke";
    /**
     * 转化信息配置时使用的参数，scheduleId
     */
    String PARAM_CONVERT_MESSAGE_CONFIG_SCHEDULE_ID = "scheduleId";
    /**
     * 参数，siteCode
     */
    String PARAM_SITE_CODE = "siteCode";
    /**
     * 参数，站内信配置
     */
    String PARAM_MESSAGE_CONFIG = "messageConfig";
    /**
     * 常量命名空间，是否星标
     */
    String ENUM_CONST_NAMESPACE_IS_STAR = "flagIsStar";
    /**
     * 常量命名空间，是否已读
     */
    String ENUM_CONST_NAMESPACE_READED = "flagReaded";
    /**
     * 参数，组id
     */
    String PARAM_GROUP_ID = "groupId";
    /**
     * 参数，groupCode
     */
    String PARAM_GROUP_CODE = "groupCode";
    /**
     * 邮件参数，发送数据
     */
    String EMAIL_ARG_DATA = "data";
    /**
     * 邮件参数，发送者
     */
    String EMAIL_ARG_SEND_USER = "sendUser";
    /**
     * 邮件参数，发送者密码
     */
    String EMAIL_ARG_SEND_PASSWORD = "sendPassword";
    /**
     * 邮件参数，发送smtp服务器
     */
    String EMAIL_ARG_SEND_SMTP_SERVER = "smtpServer";
    /**
     * 邮件参数，接收者
     */
    String EMAIL_ARG_RECEIVER = "receiver";
    /**
     * 邮件参数，主题
     */
    String EMAIL_ARG_SUBJECT = "subject";
    /**
     * 邮件参数，内容
     */
    String EMAIL_ARG_CONTENT = "content";
    /**
     * 常量命名空间，发送状态
     */
    String ENUM_CONST_NAMESPACE_SEND_STATUS = "FlagSendStatus";
    /**
     * 常量命名空间，消息类型
     */
    String ENUM_CONST_NAMESPACE_MESSAGE_TYPE = "FlagMessageType";
    /**
     * 发送消息任务 bean
     */
    String SEND_MESSAGE_JOB_SPRING_BEAN = "sendMessageJob";
    /**
     * 发送定时消息的组名称
     */
    String SCHEDULE_CONFIG_GROUP = "sendMessage";

    /**
     * 消息标题
     */
    String WECHAT_MESSAGR_FIRST = "first";

    /**
     * 消息备注
     */
    String WECHAT_MESSAGR_REMARK = "remark";

    /**
     * 学校
     */
    String WECHAT_MESSAGR_KEYWORD1 = "keyword1";

    /**
     * 通知人
     */
    String WECHAT_MESSAGR_KEYWORD2 = "keyword2";

    /**
     * 时间
     */
    String WECHAT_MESSAGR_KEYWORD3 = "keyword3";

    /**
     * 通知内容
     */
    String WECHAT_MESSAGR_KEYWORD4 = "keyword4";

    /**
     * 微信消息code
     */
    String WECHAT_MESSAGE_CODE = "peManagerMessage";

    /**
     * 微信消息类型
     */
    String WECHAT_MESSAGE_TYPE = "messageType";

    /**
     * 微信消息
     */
    String WECHAT_TEMPLATE = "weChatTemplate";

}
