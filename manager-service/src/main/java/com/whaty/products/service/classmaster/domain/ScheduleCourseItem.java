package com.whaty.products.service.classmaster.domain;

import com.whaty.products.service.classmaster.constants.ScheduleItemType;
import lombok.Data;

/**
 * 日程项目-课程
 *
 * @author weipengsen
 */
@Data
public class ScheduleCourseItem extends AbstractScheduleItem {

    private static final long serialVersionUID = 7051544200125035518L;

    private String id;

    private String teacherName;

    private String courseType;

    private Number teacherFee;

    private String address;

    public ScheduleCourseItem() {
        this.type = ScheduleItemType.COURSE.getType();
    }
}
