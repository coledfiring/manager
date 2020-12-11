package com.whaty.notice.constant;

import java.util.regex.Pattern;

/**
 * 通知推送常量
 * @author weipengsen
 */
public interface NoticeServerPollConstant {

    /**
     * 即时通知用户注册id缓存键
     */
    String NOTICE_USER_KEY = "%s_%s_notice";

    /**
     * 回收线程的名称
     */
    String NOTICE_DESTROY_THREAD_NAME = "ServerPollDestroyThread";

    /**
     * 通知推送占位符：响应信息中的超链接占位符
     */
    String NOTICE_PLACE_HOLDER_LINK = "{a:%s:%s}";
    /**
     * 通知推送占位符，响应信息中的路由跳转占位符
     */
    String NOTICE_PLACE_ROUTER_LINK = "{r::%s::%s::%s}";
    /**
     * 通知推送提示：星标设置失败
     */
    String NOTICE_INFO_SET_STAR_FAILURE = "设置星标失败";
    /**
     * 通知推送提示：删除通知信息失败
     */
    String NOTICE_INFO_DEL_FAILURE = "删除通知信息失败";

    /**
     * 删除通知信息判断的秒数条件
     */
    int NOTICE_DEL_SECTION = 30*24*60*60;
    /**
     * 通知中文件连接的通配符匹配正则
     */
    Pattern NOTICE_FILE_LINK_PATTERN = Pattern.compile("\\{a:[\\s\\S]*:[\\s\\S]*\\}");

    /**
     * 通知推送常量namespace：通知类型
     */
    String NOTICE_ENUM_CONST_NAMESPACE_NOTICE_TYPE = "flagNoticeType";
    /**
     * 通知推送常量namespace：传播类型
     */
    String NOTICE_ENUM_CONST_NAMESPACE_SCOPE_TYPE = "flagScopeType";
    /**
     * 通知推送常量namespace：是否已读
     */
    String NOTICE_ENUM_CONST_NAMESPACE_READ = "flagReaded";
    /**
     * 通知推送常量namespace：是否星标
     */
    String NOTICE_ENUM_CONST_NAMESPACE_STAR = "flagIsStar";
    /**
     * 通知推送常量code：单播类型code
     */
    String NOTICE_ENUM_CONST_CODE_UNICAST_TYPE = "1";
    /**
     * 通知推送常量code：未读code
     */
    String NOTICE_ENUM_CONST_CODE_NO_READ = "0";
    /**
     * 通知推送常量code：非星标信息code
     */
    String NOTICE_ENUM_CONST_CODE_NO_STAR = "0";

    /**
     * 通知推送常量code：用户操作类型code
     */
    String NOTICE_ENUM_CONST_CODE_USER_OPERATE_TYPE = "0";

    /**
     * 通知推送提示：空闲超时时间
     */
    String NOTICE_PROPERTIES_IDLE_TIMEOUT = "notice.idleTimeout";
    /**
     * 通知推送提示：回收检查时间
     */
    String NOTICE_PROPERTIES_CHECK_DESTROY_TIME = "notice.checkDestroyTime";
    /**
     * 通知推送提示：请求超时时间
     */
    String NOTICE_PROPERTIES_WAIT_TIME_OUT = "notice.waitTimeout";
    /**
     * 通知推送提示：标志位检查等待时间
     */
    String NOTICE_PROPERTIES_CHECK_FLAG_TIME = "notice.checkFlagTime";
    /**
     * 通知推送提示：空闲队列最大容量
     */
    String NOTICE_PROPERTIES_IDLE_MAX_QUEUE_SIZE = "notice.idleMaxQueueSize";
    /**
     * 通知推送提示：单用户同时可开启服务端数量
     */
    String NOTICE_PROPERTIES_USER_SERVER_NUM = "notice.userServerNum";
    /**
     * 通知推送提示：定时检测删除超时通知信息的配置小时数
     */
    String NOTICE_PROPERTIES_DEL_HOUR = "notice.delHour";
    /**
     * 通知推送提示：定时检测删除超时通知信息的配置小时数
     */
    String NOTICE_PROPERTIES_DISABLE = "notice.disable";
    /**
     * 通知推送请求参数：全部通知的查找范围
     */
    String NOTICE_PARAM_ALL_LIMIT = "allLimit";
    /**
     * 通知推送请求参数：未读通知的查找范围
     */
    String NOTICE_PARAM_NO_READ_LIMIT = "noReadLimit";
    /**
     * 通知推送请求参数：已读通知的查找范围
     */
    String NOTICE_PARAM_READ_LIMIT = "readLimit";
    /**
     * 通知推送请求参数：星标通知的查找范围
     */
    String NOTICE_PARAM_STAR_LIMIT = "starLimit";
    /**
     * 通知推送请求参数：是否此次请求必获得数据
     */
    String NOTICE_PARAM_GET_INFO = "getInfo";
    /**
     * 通知推送请求参数：通知id
     */
    String NOTICE_PARAM_NOTICE_ID = "noticeId";
    /**
     * 通知推送请求参数：星标code
     */
    String NOTICE_PARAM_STAR_CODE = "starCode";
    /**
     * 通知推送参数：用户id
     */
    String NOTICE_PARAM_USER_ID = "userId";
    /**
     * 通知推送参数：注册id
     */
    String NOTICE_PARAM_REGISTER_ID = "registerId";
    /**
     * 通知推送参数：是否第一次进入
     */
    String NOTICE_PARAM_IS_NEW = "isNew";
    /**
     * 通知推送参数：是否销毁服务类
     */
    String NOTICE_PARAM_DESTROY = "destroy";
    /**
     * 通知推送参数：缓存key
     */
    String NOTICE_PARAM_CACHE_KEY ="cacheKey";

    /**
     * 通知推送提示：参数错误
     */
    String NOTICE_INFO_ARGS_ERROR = "参数错误";

    /**
     * 通知推送提示：超过单用户可同时开启的服务端数量提示
     */
    String NOTICE_INFO_USER_SERVER_NUM = "超过可同时开启的通知窗口数";

    /**
     * 通知推送返回值：全部信息
     */
    String NOTICE_RESULT_ALL_INFO = "allInfo";
    /**
     * 通知推送返回值：未读信息
     */
    String NOTICE_RESULT_NO_READ_INFO = "noReadInfo";
    /**
     * 通知推送返回值：已读信息
     */
    String NOTICE_RESULT_READED_INFO = "readedInfo";
    /**
     * 通知推送返回值：星标信息
     */
    String NOTICE_RESULT_STAR_INFO = "starInfo";
    /**
     * 通知推送返回值：md5校验code
     */
    String NOTICE_RESULT_CHECK_CODE = "checkCode";

    /**
     * 通知推送提示：获取信息失败
     */
    String NOTICE_INFO_GET_INFO_FAILURE = "获取通知信息失败";
    /**
     * 审核流程
     */
    String NOTICE_ENUM_CONST_CODE_CHECK_FLOW = "1";
}
