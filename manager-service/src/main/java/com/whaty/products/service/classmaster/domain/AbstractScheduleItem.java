package com.whaty.products.service.classmaster.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 日程项目
 *
 * @author weipengsen
 */
@Data
public abstract class AbstractScheduleItem implements Serializable, Comparable<AbstractScheduleItem> {

    private static final long serialVersionUID = 7448185929397473310L;

    protected String time;

    protected String title;

    protected String type;

    protected String day;

    @Override
    public int compareTo(AbstractScheduleItem o) {
        return this.time.compareTo(o.time);
    }
}