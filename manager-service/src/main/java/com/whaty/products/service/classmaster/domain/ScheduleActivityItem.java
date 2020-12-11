package com.whaty.products.service.classmaster.domain;

import com.whaty.products.service.classmaster.constants.ScheduleItemType;
import lombok.Data;

/**
 * 日程项目-活动
 *
 * @author weipengsen
 */
@Data
public class ScheduleActivityItem extends AbstractScheduleItem {

    private static final long serialVersionUID = -1493392060829035648L;

    private String courseName;

    private String timeScope;

    private String activityName;

    public ScheduleActivityItem() {
        this.type = ScheduleItemType.ACTIVITY.getType();
    }
}
