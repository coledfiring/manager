package com.whaty.constant;

import java.time.format.DateTimeFormatter;

/**
 * 常量类
 * @author weipengsen
 */
public interface CommonConstant {

    /**
     * 前台传参，sqlId
     */
    String PARAM_SQL_ID = "sqlId";
    /**
     * 前台传参，bean
     */
    String PARAM_BEAN_NAME = "bean";

    /**
     * beanName enumConst
     */
    String BEAN_NAME_ENUM_CONST_PREFIX = "EnumConstBy";
    /**
     * 下拉框数据获得方法控制参数，有效的bean数据
     */
    boolean BEAN_NEED_ACTIVE = true;
    /**
     * 下拉框数据获得方法控制参数，bean数据不需要有效
     */
    boolean BEAN_NOT_NEED_ACTIVE = false;
    /**
     * 常量命名空间，课程是否有效
     */
    String ENUM_CONST_NAMESPACE_FLAG_IS_VALID = "FlagIsValid";

    /**
     * 前台传参id集合
     */
    String PARAM_IDS = "ids";
    /**
     * 父列表传递id
     */
    String PARAM_PARENT_ID = "parentId";

    /**
     * split方法分隔id的默认分割符
     */
    String SPLIT_ID_SIGN = ",";

    /**
     * generalDao的springBeanName
     */
    String GENERAL_DAO_BEAN_NAME = "generalDao";
    /**
     * redisCache的springBeanName
     */
    String REDIS_CACHE_SERVICE_BEAN_NAME = "redisCacheService";
    /**
     * 新课程空间接口类springBeanName
     */
    String LEARNING_SPACE_WEB_SERVICE_BEAN_NAME = "learningSpaceWebService";
    /**
     * sql横向权限设置springBeanName
     */
    String SQL_SCOPE_MANAGER_BEAN_NAME = "sqlScopeManager";
    /**
     * 导入工具服务类springBeanName
     */
    String EXCEL_UPLOAD_BEAN_NAME = "excelUploadService";
    /**
     * 业务服务类springBeanName
     */
    String UTIL_SERVICE_BEAN_NAME = "utilService";

    /**
     * controlGeneralDao的springBeanName
     */
    String CONTROL_GENERAL_DAO_BEAN_NAME = "controlGeneralDao";

    /**
     * 服务器内部错误提示信息
     */
    String ERROR_STR = "服务器内部错误！请联系管理员。";
    /**
     * 参数错误提示信息
     */
    String PARAM_ERROR = "参数错误";

    /**
     * yyyy-mm-dd格式字符串
     */
    String DEFAULT_DATE_STR = "yyyy-MM-dd";

    /**
     * yyyy-mm-dd格式format
     */
    DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_DATE_STR);

    /**
     * yyyyMMddHHmm格式字符串到分钟
     */
    String NO_SIGN_DATETIME_TO_MINUTE_STR = "yyyyMMddHHmm";

    /**
     * yyyyMMddHHmmss格式到秒
     */
    String NO_SIGN_DATETIME_TO_SECOND_STR = "yyyyMMddHHmmss";
    /**
     * yyMMddHHmmssSS格式到毫秒
     */
    String NO_SIGN_DATETIME_TO_MILLIS_STR = "yyMMddHHmmssSS";

    /**
     * yyyyMMddHHmm格式format
     */
    DateTimeFormatter NO_SIGN_DATETIME_TO_MINUTE_FORMAT = DateTimeFormatter.ofPattern(NO_SIGN_DATETIME_TO_MINUTE_STR);
    /**
     * yyyy-MM-dd HH:mm:ss的mysql默认日期格式
     */
    String MYSQL_DEFAULT_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyyMMddHHmm格式format
     */
    DateTimeFormatter NO_SIGN_DATETIME_TO_SECOND_FORMAT = DateTimeFormatter.ofPattern(NO_SIGN_DATETIME_TO_SECOND_STR);

    /**
     * 参数上传文件
     */
    String PARAM_UPLOAD = "upload";
    /**
     * 上传临时文件存放处
     */
    String TEMP_FILE_PATH = "/incoming/temp/upload";
    /**
     * 课程空间异常提示信息
     */
    String LEARNING_SPACE_ERROR = "课程空间同步失败";
    /**
     * 文件上传导入通用参数名
     */
    String PARAM_FILE_UPLOAD = "upload";
    /**
     * 参数站点code
     */
    String PARAM_WEB_SITE_CODE = "webSiteCode";
    /**
     * 常量是否有效的命名空间
     */
    String ENUM_CONST_NAMESPACE_FLAG_ACTIVE = "flagActive";
    /**
     * 常量是否有效的命名空间
     */
    String ENUM_CONST_NAMESPACE_FLAG_ISVALID = "FlagIsvalid";
    /**
     * HTML的换行标签
     */
    String HTML_TURN_LINE_TAG = "<br/>";
    /**
     * excel导入外观对象springBeanName
     */
    String SPRING_BEAN_NAME_EXCEL_UPLOAD_FACADE = "excelUploadFacade";
    /**
     * 编码集utf-8
     */
    String CHARSET_UTF_8 = "utf-8";
    /**
     * 角色编号，学生
     */
    String ROLE_CODE_STUDENT = "0";
    /**
     * 角色编号，教师
     */
    String ROLE_CODE_TEACHER = "1";
    /**
     * 参数，站点编号
     */
    String PARAM_SITE_CODE = "siteCode";
    /**
     * openSession的generalDao的beanName
     */
    String OPEN_GENERAL_DAO_BEAN_NAME = "openGeneralDao";
    /**
     * 参数，params
     */
    String PARAM_PARAMS = "params";
    /**
     * 生成文件的临时存储位置
     */
    String TEMP_GENERATE_FILE_PATH = "/incoming/temp/generate";

    /**
     * 本机ip地址
     */
    String LOCAL_HOST_IP = "127.0.0.1";
    /**
     * 参数，用户id
     */
    String PARAM_USER_ID = "userId";
    /**
     * 角色编号，超管
     */
    String ROLE_CODE_SUPER_ADMIN = "9999";
    /**
     * 错误信息，同步用户中心失败
     */
    String U_CENTER_ERROR = "同步用户中心失败";
    /**
     * spring bean 用户中心
     */
    String U_CENTER_SERVICE_BEAN_NAME = "uCenterService";
    /**
     * 参数，文件名
     */
    String PARAM_FILE_NAME = "fileName";
    /**
     * UTC时间格式
     */
    String UTC_DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * 头像地址
     */
    String PROFILE_PICTURE_PATH = "/incoming/profilePicture/%s/%s/%s.png";

    /**
     * 工作安排
     */
    String JOB_ARRANGE = "jobArrange";

    /**
     * 政策法规
     */
    String  POLICY_REGULATION = "policyRegulation";

    /**
     * 政策法规
     */
    String  SCHOOL_INFO = "schoolInfo";

    /**
     * 常量是否授课教师的命名空间
     */
    String ENUM_CONST_NAMESPACE_FLAG_COURSE_TEACHER = "FlagCourseTeacher";

    /**
     * 常量是否资源教师的命名空间
     */
    String ENUM_CONST_NAMESPACE_FLAG_TUTOR_TEACHER = "FlagTutorTeacher";

    /**
     * 常量是否班主任的命名空间
     */
    String ENUM_CONST_NAMESPACE_FLAG_IS_CLASSMASTER = "FlagIsClassmaster";

    /**
     * 钉钉机器人
     */
    String DING_DING_ROBOT_URL = "https://oapi.dingtalk.com/robot/send?access_token=05d2634e7131a60990817d80566f1121e735af4c0546b78e9e9656a7f1d018e5";

    /**
     * 默认图片地址
     */
    String DEFAULT_ALBUM_PICTURE_PATH = "/images/album/default_picture.png";
    /**
     * 默认课程图片
     */
    String COURSE_DEFAULT_PATH = "/images/course/default_picture.png";

    String PARAM_RESULT = "result";
}
