package com.whaty.products.service.oltrain.classmaster;


import java.util.List;
import java.util.Map;

/**
 * 班主任班级信息
 *
 * @author suoqiangqiang
 */
public interface OLClassMasterClassService {

    /**
     * 获取班级信息
     * @param classId
     * @return
     */
    Map<String,Object> getClassInfo(String classId);

    /**
     * 获取班级详情
     * @param classId
     * @return
     */
    Map<String, Object> getClassDetail(String classId);

    /**
     * 列举网络课程
     * @param classId
     * @return
     */
    List<Object[]> listOnlineCourses(String classId);

    /**
     * 保存内容
     * @param classId
     * @param code
     * @param content
     */
    void saveContent(String classId, String code, String content);

}
