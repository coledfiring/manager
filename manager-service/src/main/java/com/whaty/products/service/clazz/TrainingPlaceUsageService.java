package com.whaty.products.service.clazz;

import java.util.List;
import java.util.Map;

/**
 * 培训地点使用一览
 *
 * @author weipengsen
 */
public interface TrainingPlaceUsageService {

    /**
     * 列举使用情况
     * @param unit
     * @param place
     * @param timeArea
     * @param capacity
     * @param feeLevel
     * @return
     */
    Map<String, Object> listUsageInfo(String unit, String place, List<String> timeArea,
                                      String capacity, String feeLevel);

    /**
     * 列举班级
     * @param type
     * @return
     */
    List<Map<String, Object>> listClass(String type);

    /**
     * 列举固定地点
     * @param classId
     * @return
     */
    List<Map<String, Object>> listFixedPlace(String classId);

    /**
     * 安排固定地点
     * @param classId
     * @param placeId
     * @param color
     */
    void doArrangeFixed(String classId, String placeId, String color);

    /**
     * 列举课程表
     * @param classId
     * @return
     */
    List<Map<String, Object>> listTime(String classId);

    /**
     * 列举流动地点
     * @param timeId
     * @return
     */
    List<Map<String, Object>> listFlowPlace(String timeId);

    /**
     * 安排流动地点
     * @param time
     * @param placeId
     * @param color
     */
    void doArrangeFlow(List<String> time, String placeId, String color);

    /**
     * 列举临时用途地点
     *
     * @param useDate
     * @param timeId
     * @return
     */
    List<Map<String, Object>> listTemporaryPlace(String useDate, String timeId);

    /**
     * 安排暂时借用
     * @param useDate
     * @param time
     * @param placeId
     * @param application
     * @param color
     */
    void doArrangeTemporary(String useDate, List<String> time, String placeId, String application, String color);

    /**
     * 删除安排
     * @param id
     * @param type
     */
    void deleteArrange(String id, String type);

    /**
     * 列举可删除的班级
     * @return
     */
    List<Map<String, Object>> listCanDeleteClass();

    /**
     * 删除班级地点安排
     * @param classId
     */
    void deleteClassArrange(String classId);
}
