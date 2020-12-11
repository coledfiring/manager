package com.whaty.products.service.superadmin.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.hibernate.dao.impl.ControlGeneralDao;
import com.whaty.core.framework.util.UUIDUtil;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.LearningSpaceException;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.idocv.WhatyIdocvSdk;
import com.whaty.framework.ucenter.utils.UCenterUtils;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.products.service.superadmin.ExternalDataHandleService;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 外部数据处理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("externalDataHandleService")
public class ExternalDataHandleServiceImpl implements ExternalDataHandleService {

    @Resource(name = CommonConstant.CONTROL_GENERAL_DAO_BEAN_NAME)
    private ControlGeneralDao controlGeneralDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    private final WhatyIdocvSdk idocvSdk = new WhatyIdocvSdk();

    @Override
    public void removeLearnSpaceCourse(String searchSql, String ids, String siteCode) {
        this.checkCourseParams(searchSql, ids, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        try {
            if (!this.learningSpaceWebService.removeCourse(ids, LearnSpaceUtil.getLearnSpaceSiteCode(siteCode))) {
                throw new ServiceException("课程空间删除课程失败");
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new LearningSpaceException(e);
        }
    }

    @Override
    public void syncLearnSpaceCourse(String searchSql, String ids, String siteCode) {
        this.checkCourseParams(searchSql, ids, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        List<Object[]> courseObjList = this.openGeneralDao.getBySQL("select id,name,code,site_code from pe_course where "
                + CommonUtils.madeSqlIn(ids, "id"));
        List<String[]> courseList = courseObjList.stream()
                .map(course -> new String[]{(String) course[0], (String) course[1], (String) course[2],
                        (String) course[3]})
                .collect(Collectors.toList());
        try {
            if (!this.learningSpaceWebService.saveCourseBatch(courseList)) {
                throw new ServiceException("课程同步课程空间失败");
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new LearningSpaceException(e);
        }
    }

    @Override
    public void removeLearnSpaceCourseTeacherRel(String searchSql, String ids, String siteCode) {
        this.checkCourseParams(searchSql, ids, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        List<Map<String, String>> courseTeaRelList;
        try {
            courseTeaRelList = com.whaty.core.commons.util.CommonUtils
                    .convertIdsToList(ids)
                    .stream()
                    .map(relId -> new HashMap<String, String>() {{
                        put("teacherid", relId.split("\\*")[0]);
                        put("courseid", relId.split("\\*")[1]);
                        put("siteCode", siteCode);
                    }}).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("ids格式或查询语句有误");
        }
        try {
            if (!this.learningSpaceWebService.removeCourseTeacherBatch(courseTeaRelList)) {
                throw new ServiceException("删除教师课程关系失败");
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new LearningSpaceException(e);
        }
    }

    @Override
    public void syncLearnSpaceCourseTeacherRel(String searchSql, String ids, String siteCode) {
        this.checkCourseParams(searchSql, ids, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        List<String[]> courseTeaRelList;
        try {
            courseTeaRelList = com.whaty.core.commons.util.CommonUtils
                    .convertIdsToList(ids)
                    .stream()
                    .map(relId -> new String[]{UUIDUtil.getUUID().replace("-", ""), relId.split("\\*")[0],
                            relId.split("\\*")[1], siteCode, "1"})
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("ids格式或查询语句有误");
        }
        try {
            if (!this.learningSpaceWebService.saveCourseTeacherBatch(courseTeaRelList)) {
                throw new ServiceException("同步教师课程关系关系失败");
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new LearningSpaceException(e);
        }
    }

    @Override
    public void removeLearnSpaceElective(String searchSql, String ids, String siteCode) {
        this.checkCourseParams(searchSql, ids, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        List<String> idList = Arrays.stream(ids.split(CommonConstant.SPLIT_ID_SIGN))
                .filter(id -> !this.removeSingleLearnSpaceElective(id, siteCode)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(idList)) {
            throw new ServiceException(CommonUtils.join(idList, ",", "") + "同步失败");
        }
    }

    /**
     * 删除单个选课
     * @param id
     * @param siteCode
     * @return
     */
    private boolean removeSingleLearnSpaceElective(String id, String siteCode) {
        try {
            return this.learningSpaceWebService.removeElective(id, LearnSpaceUtil.getLearnSpaceSiteCode(siteCode));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void syncLearnSpaceElective(String searchSql, String ids, String siteCode) {
        this.checkCourseParams(searchSql, ids, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        List<Map<String, Object>> data = this.openGeneralDao.getMapBySQL("select id as id, FK_STU_ID as stuId," +
                " FK_COURSE_ID as courseId, DATE_FORMAT(ELECTIVE_DATE, '%Y-%m-%d') as date " +
                "from pr_tch_stu_elective where " + CommonUtils.madeSqlIn(ids, "id"));
        List<String> idList = data.stream()
                .filter(e -> !this.syncSingleLearnSpaceElective((String) e.get("id"), (String) e.get("stuId"),
                        (String) e.get("courseId"), (String) e.get("date"), siteCode))
                .map(e -> (String) e.get("id")).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(idList)) {
            throw new ServiceException(CommonUtils.join(idList, ",", "") + "同步失败");
        }
    }

    @Override
    public void removeTchElective(String searchSql, String ids, String siteCode) {
        this.checkCourseParams(searchSql, ids, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        this.openGeneralDao.executeBySQL("delete from pr_tch_stu_elective where " + CommonUtils.madeSqlIn(ids, "id"));
    }

    @Override
    public void doSyncToIdocv(String searchSql, String ids, String siteCode) {
        TycjParameterAssert.isAllNotBlank(ids, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        List<Map<String, Object>> fileList = this.openGeneralDao.getMapBySQL("select id, name, url from attach_file where " +
                CommonUtils.madeSqlIn(ids, "id"));
        fileList.stream().peek(e -> e.put("file", new File(CommonUtils.getRealPath((String) e.get("url")))))
                .filter(e -> ((File) e.get("file")).exists())
                .peek(e -> {
                    try {
                        e.put("uuid", this.idocvSdk.upload((File) e.get("file"), (String) e.get("name")));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }).filter(e -> e.get("uuid") != null)
                .forEach(e -> this.openGeneralDao
                        .executeBySQL("update attach_file set idocv_uuid = ? where id = ?",
                                e.get("uuid"), e.get("id")));
    }

    /**
     * 同步单个选课
     * @param id
     * @param stuId
     * @param courseId
     * @param date
     * @param siteCode
     * @return
     */
    private boolean syncSingleLearnSpaceElective(String id, String stuId, String courseId, String date, String siteCode) {
        try {
            return learningSpaceWebService.saveElective(id, stuId, courseId, date, false,
                    LearnSpaceUtil.getLearnSpaceSiteCode(siteCode));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeLearnSpaceUser(String searchSql, String ids, String userType, String siteCode) {
        this.checkUserParams(searchSql, ids, userType, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        switch (userType) {
            case "manager":
                this.removeLearnSpaceManager(ids, siteCode);
                break;
            case "teacher":
                this.removeLearnSpaceTeacher(ids, siteCode);
                break;
            case "student":
                this.removeLearnSpaceStudent(ids, siteCode);
                break;
            default:
                throw new ServiceException("角色不存在");
        }
    }

    @Override
    public void syncLearnSpaceUser(String searchSql, String ids, String userType, String siteCode) {
        this.checkUserParams(searchSql, ids, userType, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        List<Map<String, Object>> userDataList = this.openGeneralDao
                .getMapBySQL(UserSyncDataSearchSqlEnum.getByCode(userType).getSql()
                        + CommonUtils.madeSqlIn(ids, "user.id"));
        if (CollectionUtils.isEmpty(userDataList)) {
            throw new ServiceException("用户信息不存在");
        }
        switch (userType) {
            case "manager":
                this.syncLearnSpaceManager(userDataList);
                break;
            case "teacher":
                this.syncLearnSpaceTeacher(userDataList);
                break;
            case "student":
                this.syncLearnSpaceStudent(userDataList);
                break;
            default:
                throw new ServiceException("角色不存在");
        }
    }

    @Override
    public void removeUCenterUser(String searchSql, String ids, String userType, String siteCode) {
        this.checkUserParams(searchSql, ids, userType, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        List<String> loginIds = this.openGeneralDao.getMapBySQL(
                UserSyncDataSearchSqlEnum.getByCode(userType).getSql() + CommonUtils.madeSqlIn(ids, "user.id"))
                .stream().map(e -> (String) e.get("loginId")).filter(Objects::nonNull).collect(Collectors.toList());
        UCenterUtils.removeUserFromUCenter(loginIds, siteCode);
    }

    @Override
    public void syncUCenterUser(String searchSql, String ids, String userType, String siteCode) {
        this.checkUserParams(searchSql, ids, userType, siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        ids = this.getIds(searchSql, ids);
        List<Map<String, Object>> userDataList = this.openGeneralDao
                .getMapBySQL(UserSyncDataSearchSqlEnum.getByCode(userType).getSql()
                        + CommonUtils.madeSqlIn(ids, "user.id"));
        if (CollectionUtils.isEmpty(userDataList)) {
            throw new ServiceException("用户信息不存在");
        }
        UCenterUtils.synchronousUserToUCenter(userDataList, siteCode);
    }

    /**
     * 移除课程空间管理员
     *
     * @param ids
     * @param siteCode
     */
    private void removeLearnSpaceManager(String ids, String siteCode) {
        try {
            if (!learningSpaceWebService.removeManager(ids, siteCode)) {
                throw new ServiceException("课程空间删除管理员失败");
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new LearningSpaceException();
        }
    }

    /**
     * 移除课程空间教师
     *
     * @param ids
     * @param siteCode
     */
    private void removeLearnSpaceTeacher(String ids, String siteCode) {
        try {
            if (!learningSpaceWebService.removeTeacher(ids, siteCode)) {
                throw new ServiceException("课程空间删除教师失败");
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new LearningSpaceException();
        }
    }

    /**
     * 移除课程空间学生
     *
     * @param ids
     * @param siteCode
     */
    private void removeLearnSpaceStudent(String ids, String siteCode) {
        try {
            if (!learningSpaceWebService.removeStudent(ids, siteCode)) {
                throw new ServiceException("课程空间删除学生失败");
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new LearningSpaceException();
        }
    }

    /**
     * 同步管理员到课程空间
     *
     * @param userDataList
     */
    private void syncLearnSpaceManager(List<Map<String, Object>> userDataList) {
        try {
            if (!learningSpaceWebService.saveTeacherBatch(this.getLearnSpaceUserSyncData(userDataList))) {
                throw new ServiceException("管理员同步到课程空间失败");
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new LearningSpaceException();
        }
    }

    /**
     * 同步教师到课程空间
     *
     * @param userDataList
     */
    private void syncLearnSpaceTeacher(List<Map<String, Object>> userDataList) {
        try {
            if (!learningSpaceWebService.saveTeacherBatch(this.getLearnSpaceUserSyncData(userDataList))) {
                throw new ServiceException("教师同步到课程空间失败");
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new LearningSpaceException();
        }
    }

    /**
     * 同步学生到课程空间
     *
     * @param userDataList
     */
    private void syncLearnSpaceStudent(List<Map<String, Object>> userDataList) {
        try {
            if (!learningSpaceWebService.saveSudentBatch(this.getLearnSpaceUserSyncData(userDataList))) {
                throw new ServiceException("学生同步到课程空间失败");
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new LearningSpaceException();
        }
    }

    /**
     * 获取课程空间同步用户数据
     *
     * @param useData
     * @return
     */
    private List<String[]> getLearnSpaceUserSyncData(List<Map<String, Object>> useData) {
        return useData.stream().map(user -> new String[]{(String) user.get("id"),
                (String) user.get("loginId"), (String) user.get("trueName"), (String) user.get("userId"), null,
                (String) user.get("siteCode"), (String) user.get("password")}).collect(Collectors.toList());

    }

    /**
     * 获取处理id
     *
     * @param searchSql
     * @param ids
     * @return
     */
    private String getIds(String searchSql, String ids) {
        if (StringUtils.isBlank(searchSql)) {
            return ids;
        }
        try {
            List<String> idList = this.controlGeneralDao.getBySQL(searchSql);
            if (CollectionUtils.isEmpty(idList)) {
                throw new ServiceException("没有需要处理的数据");
            }
            return String.join(CommonConstant.SPLIT_ID_SIGN, idList);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("查询语句有误");
        }
    }

    /**
     * 检查用户参数
     *
     * @param searchSql
     * @param ids
     * @param userType
     * @param siteCode
     */
    private void checkUserParams(String searchSql, String ids, String userType, String siteCode) {
        TycjParameterAssert.isAllNotBlank(userType, siteCode);
        if (SiteUtil.getSite(siteCode) == null) {
            throw new ServiceException("站点编码不存在！");
        }
        if (StringUtils.isBlank(searchSql) && StringUtils.isBlank(ids)) {
            throw new ParameterIllegalException();
        }
    }

    /**
     * 检查课程参数
     *
     * @param searchSql
     * @param ids
     * @param siteCode
     */
    private void checkCourseParams(String searchSql, String ids, String siteCode) {
        TycjParameterAssert.isAllNotBlank(siteCode);
        if (SiteUtil.getSite(siteCode) == null) {
            throw new ServiceException("站点编码不存在！");
        }
        if (StringUtils.isBlank(searchSql) && StringUtils.isBlank(ids)) {
            throw new ParameterIllegalException();
        }
    }
}
