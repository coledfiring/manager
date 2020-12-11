package com.whaty.products.service.businessunit.constants;

/**
 * 单位学生报名常量
 *
 * @author shanshuai
 */
public interface BusinessUnitStudentEnrollConstant {

    /**
     * 批量导入学生报名
     */
    String[] UPLOAD_EXCEL_STUDENT_ENROLL = new String[]{"姓名*", "所报期次*", "身份证号*", "性别*", "手机号*",
            "电子邮箱*", "通讯地址*", "职务*", "职称*", "公司名称*", "是否需要培训用书", "是否需要餐券", "是否需要单间住宿"};

    /**
     * 批量导入学生缴费信息
     */
    String[] UPLOAD_EXCEL_STUDENT_FEE_DETAIL = new String[]{"期次名称*", "身份证号*", "姓名", "订单编号*"};

    /**
     * 上传单位订单汇款单保存路径
     */
    String MONEY_ORDER_PATH = "/incoming/business";

    /**
     * 注册时用户名只能是数字或者字母正则
     */
    String LOGIN_ID_REGEX = "^[0-9a-zA-Z]+$";

}
