package com.whaty.products.service.information.constant;

import java.util.regex.Pattern;

/**
 * 常量池
 * @author weipengsen
 */
public interface InformationConstant {
    /**
     * 参数，驳回原因
     */
    String PARAM_RETURN_REASON = "returnReason";
    /**
     * 常量，发送状态
     */
    String ENUM_CONST_NAMESPACE_SMS_STATUS = "FlagSmsStatus";
    /**
     * 参数，personInfo
     */
    String PARAM_PERSON_INFO = "personInfo";
    /**
     * 参数，姓名
     */
    String PARAM_TRUE_NAME = "trueName";
    /**
     * 参数，登录次数
     */
    String PARAM_LOGIN_NUM = "loginNum";
    /**
     * 参数，最后登录时间
     */
    String PARAM_LAST_LOGIN_TIME = "lastLoginTime";
    /**
     * 参数，性别
     */
    String PARAM_SEX = "sex";
    /**
     * 参数，身份证号
     */
    String PARAM_CARD_NO = "cardNo";
    /**
     * 参数，邮箱
     */
    String PARAM_EMAIL = "email";
    /**
     * 参数，固定电话
     */
    String PARAM_PHONE = "phone";
    /**
     * 参数，移动电话
     */
    String PARAM_MOBILE = "mobile";
    /**
     * 参数，地址
     */
    String PARAM_ADDRESS = "address";
    /**
     * 参数，职称
     */
    String PARAM_PROFESSIONAL = "professional";
    /**
     * 参数，微信昵称
     */
    String PARAM_WE_CHAT_NICK_NAME = "weChatNickName";
    /**
     * 参数，旧密码
     */
    String PARAM_OLD_PASSWORD = "oldPassword";
    /**
     * 参数，新密码
     */
    String PARAM_NEW_PASSWORD = "newPassword";
    /**
     * 参数，邮箱数据
     */
    String PARAM_EMAIL_DATA = "emailData";
    /**
     * 参数，写作指引数据
     */
    String PARAM_WRITING_DATA = "writingData";
    /**
     * 常量命名空间，发送类型
     */
    String ENUM_CONST_NAMESPACE_SMS_TYPE = "FlagSmsType";

    /**
     * 多个邮箱的正则字符串
     */
    String EMAIL_LIST_STRING_REGEXP = "^([0-9a-zA-Z]+@[0-9a-zA-Z]+\\.[0-9a-zA-Z]+;)+$";

    /**
     * 多个邮箱的正则对象
     */
    Pattern EMAIL_LIST_REGEXP_FORMAT = Pattern.compile(EMAIL_LIST_STRING_REGEXP);

    /**
     * 参数，公告标题
     */
    String PARAM_BULLETIN_TITLE = "name";
    /**
     * 参数，公告id
     */
    String PARAM_BULLETIN_ID = "id";
    /**
     * 参数，单位范围
     */
    String PARAM_BULLETIN_UNITS = "unitData";
    /**
     * 参数，班级范围
     */
    String PARAM_BULLETIN_CLASSES = "clazzIds";
    /**
     * 参数，表单
     */
    String PARAM_BULLETIN_FORM = "form";
    /**
     * 参数，是否发给管理员
     */
    String PARAM_BULLETIN_FORM_MANAGERS = "managers";
    /**
     * 参数，是否发给学生
     */
    String PARAM_BULLETIN_FORM_STUDENTS = "students";
    /**
     *  参数是否发给操作员
     */
    String  PARAM_BULLETIN_FORM_OPERATOR = "operator";
    /**
     * 参数，是否发给教师
     */
    String PARAM_BULLETIN_FORM_TEACHERS = "teachers";
    /**
     * 参数，是否置顶
     */
    String PARAM_BULLETIN_FORM_ISTOP = "isTop";
    /**
     * 参数，是否有效
     */
    String PARAM_BULLETIN_FORM_ISVALID = "isValid";
    /**
     * 参数，公告内容
     */
    String PARAM_BULLETIN_FORM_CONTENT = "content";
    /**
     * 常量命名空间，是否置顶
     */
    String ENUM_CONST_NAMESPACE_IS_TOP = "flagIsTop";
    /**
     * 常量命名空间，是否置顶
     */
    String ENUM_CONST_NAMESPACE_IS_VALID = "flagIsValid";
    /**
     * 公告选择班级
     */
    String PARAM_TRAIN_CLASS="classData";

    /**
     * 参数，businessUnitInfo
     */
    String PARAM_BUSINESS_UNIT_INFO = "businessUnitInfo";

    /**
     * 常量命名空间，公告类型
     */
    String ENUM_CONST_NAMESPACE_FLAG_BULLETIN_TYPE = "FlagBulletinType";
}
