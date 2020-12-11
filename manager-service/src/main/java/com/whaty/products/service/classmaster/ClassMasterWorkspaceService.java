package com.whaty.products.service.classmaster;

import com.whaty.products.service.classmaster.domain.ScheduleDay;

import java.util.List;
import java.util.Map;

/**
 * 班主任工作台
 *
 * @author weipengsen
 */
public interface ClassMasterWorkspaceService {

    /**
     * 列举日程
     *
     * @param classId
     * @return
     */
    List<ScheduleDay> listSchedules(String classId);

    /**
     * 列举推荐通知公告
     * @param classId
     * @return
     */
    List<Map<String, Object>> listNotices(String classId);

    /**
     * 更改准备工作状态
     * @param classId
     * @param id
     * @param done
     */
    void updatePrepareItemStatus(String classId, String id, Boolean done);

    /**
     * 列举准备工作
     * @param classId
     * @return
     */
    List<Map<String, Object>> listPrepareItems(String classId);

    /**
     * 列举课程表
     * @param classId
     * @return
     */
    List<ScheduleDay> listCourseTimeTable(String classId);

    /**
     * 删除课程表
     * @param classId
     * @param id
     */
    void deleteCourseTimeTable(String classId, String id);

    /**
     * 根据id获取课程表
     * @param classId
     * @param courseTimeTableId
     * @return
     */
    Map<String, Object> getCourseTimeTable(String classId, String courseTimeTableId);
}
