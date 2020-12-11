package com.whaty.products.service.clazz.constants;

import com.whaty.redisson.lock.DistributedBlockLock;

/**
 * 班级管理常量池
 *
 * @author weipengsen
 */
public interface ClazzConstants {

    /**
     * 地点安排的互斥值
     */
    String PLACE_ARRANGE_MUTEX = "arrangePlaceMutex";

    /**
     * 地点安排锁
     */
    DistributedBlockLock PLACE_ARRANGE_LOCK = new DistributedBlockLock(PLACE_ARRANGE_MUTEX);

    /**
     * 批量导入课程表
     */
    String[] UPLOAD_EXCEL_COURSE_TIMETABLE = new String[]{"课程名称*", "培训日期*", "开始时间*", "结束时间*",
            "师资费用*", "培训地点*"};

    /**
     * 搜索参数，query
     */
    String PARAM_QUERY = "query";

    /**
     * 搜索参数，上传类型
     */
    String PARAM_UPLOAD_TYPE = "uploadType";

    /**
     * 学员证书信息下载的表头
     */
    String[] UPLOAD_EXCEL_STUDENT_CERTIFICATE = new String[]{"姓名", "证件类型", "身份证号", "性别",
            "出生日期（请按下面的日期格式导入）", "填写合作方管理里面对应的合作",
            "行业", "单位", "联系电话", "邮箱", "职务", "文化程度", "国籍"};

}
