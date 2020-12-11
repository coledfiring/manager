package com.whaty.products.service.superadmin;

/**
 * 外部数据处理服务类
 *
 * @author suoqiangqiang
 */
public interface ExternalDataHandleService {

    /**
     * 移除课程空间课程
     *
     * @param searchSql
     * @param ids
     * @param siteCode
     */
    void removeLearnSpaceCourse(String searchSql, String ids, String siteCode);

    /**
     * 同步课程到课程空间
     *
     * @param searchSql
     * @param ids
     * @param siteCode
     */
    void syncLearnSpaceCourse(String searchSql, String ids, String siteCode);

    /**
     * 移除教师-课程关联关系
     *
     * @param searchSql
     * @param ids
     * @param siteCode
     */
    void removeLearnSpaceCourseTeacherRel(String searchSql, String ids, String siteCode);

    /**
     * 同步教师-课程关联关系
     *
     * @param searchSql
     * @param ids
     * @param siteCode
     */
    void syncLearnSpaceCourseTeacherRel(String searchSql, String ids, String siteCode);

    /**
     * 移除课程空间用户
     *
     * @param searchSql
     * @param ids
     * @param userType
     * @param siteCode
     */
    void removeLearnSpaceUser(String searchSql, String ids, String userType, String siteCode);

    /**
     * 同步用户到课程空间
     *
     * @param searchSql
     * @param ids
     * @param userType
     * @param siteCode
     */
    void syncLearnSpaceUser(String searchSql, String ids, String userType, String siteCode);

    /**
     * 移除用户中心用户
     *
     * @param searchSql
     * @param ids
     * @param userType
     * @param siteCode
     */
    void removeUCenterUser(String searchSql, String ids, String userType, String siteCode);

    /**
     * 同步用户到用户中心
     *
     * @param searchSql
     * @param ids
     * @param userType
     * @param siteCode
     */
    void syncUCenterUser(String searchSql, String ids, String userType, String siteCode);

    /**
     * 移除课程空间选课
     * @param searchSql
     * @param ids
     * @param siteCode
     */
    void removeLearnSpaceElective(String searchSql, String ids, String siteCode);

    /**
     * 同步课程空间选课
     * @param searchSql
     * @param ids
     * @param siteCode
     */
    void syncLearnSpaceElective(String searchSql, String ids, String siteCode);

    /**
     * 删除选课
     * @param searchSql
     * @param ids
     * @param siteCode
     */
    void removeTchElective(String searchSql, String ids, String siteCode);

    /**
     * 同步文件到idocv
     * @param searchSql
     * @param ids
     * @param siteCode
     */
    void doSyncToIdocv(String searchSql, String ids, String siteCode);

}
