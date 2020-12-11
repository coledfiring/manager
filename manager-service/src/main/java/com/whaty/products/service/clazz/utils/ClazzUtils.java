package com.whaty.products.service.clazz.utils;

import java.util.List;
import java.util.Map;

/**
 * 工具类
 *
 * @author weipengsen
 */
public class ClazzUtils {

    /**
     * 判断时间段冲突
     * @param timeArea
     * @param date
     * @param time
     * @return
     */
    public static boolean predicateTimeAreaCoincide(List<Map<String, Object>> timeArea, String date, String time) {
        return timeArea.stream().anyMatch(e -> {
            String date1 = (String) e.get("date");
            String time1 = (String) e.get("time");
            return date1.equals(date) && time1.equals(time);
        });
    }

    /**
     * 判断时间段冲突
     * 若时间段重合返回 true 反之 false
     */
    public static boolean verificationPeriodConflict(List<Map<String, Object>> timeMap, String startTime, String endTime){
        return timeMap.stream().anyMatch(e -> {
            boolean startInArea = startTime.compareTo((String) e.get("startTime")) > 0 && startTime.compareTo((String)e.get("endTime")) < 0 ;
            boolean endInArea = endTime.compareTo((String)e.get("startTime")) > 0 && endTime.compareTo((String)e.get("endTime")) < 0 ;
            boolean areaInclude = startTime.compareTo((String)e.get("startTime")) <= 0 && endTime.compareTo((String)e.get("endTime")) >= 0 ;
            return startInArea || endInArea || areaInclude;
        });
    }

}
