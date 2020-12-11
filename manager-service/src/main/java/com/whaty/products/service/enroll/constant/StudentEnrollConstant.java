package com.whaty.products.service.enroll.constant;

/**
 * 学生报名常量类
 */
public interface StudentEnrollConstant {

    /**
     * 参数 班级id
     */
    String PARAMS_CLAZZ_ID = "clazzId";
    /**
     * 参数 项目id
     */
    String PARAMS_ITEM_ID = "itemId";
    /**
     * 参数 报名费
     */
    String PARAMS_ENROLL_FEE = "enrollFee";

    /**
     * 导入票据号表头
     */
    String[] UPLOAD_EXCEL_INVOICE_NO = new String[]{"学生姓名*", "手机号*", "项目名称*", "项目编号*", "票据号*"};

    /**
     * 导入考场信息
     */
    String[] UPLOAD_EXCEL_EXAM_INFO = new String[]{"身份证号*", "考试地点*", "考试时间*", "座位号*", "备注"};

    /**
     * 考点编号正则 两位数字
     */
    String REGEX_EXAM_SITE_CODE = "^([0-9][0-9])$";

}
