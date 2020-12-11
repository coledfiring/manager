package com.whaty.products.service.classmaster.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 日程
 *
 * @author weipengsen
 */
@Data
public class ScheduleDay implements Serializable {

    private static final long serialVersionUID = 1800994709862666041L;

    private String day;

    private List<? extends AbstractScheduleItem> items;

    public ScheduleDay(String day, List<? extends AbstractScheduleItem> items) {
        this.day = day;
        this.items = items.stream().sorted().collect(Collectors.toList());
    }
}
