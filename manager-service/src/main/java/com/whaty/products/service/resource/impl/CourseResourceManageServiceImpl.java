package com.whaty.products.service.resource.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.bean.menu.AbstractGridMenu;
import com.whaty.core.framework.grid.bean.menu.ToUrlGridMenu;
import com.whaty.core.framework.util.UUIDUtil;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeCourse;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.LearningSpaceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.products.service.resource.constant.ResourceConstants;
import com.whaty.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.MalformedObjectNameException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 课程资源服务类 操作课程空间
 *
 * @author weipengsen
 */
@Lazy
@Service("courseResourceManageService")
public class CourseResourceManageServiceImpl extends CourseResourceManageBaseServiceImpl {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    private static final Logger logger = LoggerFactory.getLogger(CourseResourceManageServiceImpl.class);

    @Override
    public void initGrid(GridConfig gridConfig, Map<String, Object> mapParam) {
        String loginId = this.userService.getCurrentUser().getLoginId();
        String domain;
        try {
            domain = CommonUtils.getServerName() + ":" + CommonUtils.getServerPort();
        } catch (MalformedObjectNameException e) {
            throw new UncheckException(e);
        }
        List<AbstractGridMenu> gridMenuList = gridConfig.getGridMenuList();
        //课程设计按钮
        String urlDesign = String.format(ResourceConstants.LEARNING_SPACE_COURSE_DESIGN,
                LearnSpaceUtil.getHttpClientUrl(),
                loginId, LearnSpaceUtil.getLearnSpaceSiteCode(), domain);
        ((ToUrlGridMenu)gridMenuList.get(0)).setUrl(urlDesign);
        //课程预览按钮
        String urlPreview = String.format(ResourceConstants.LEARNING_SPACE_COURSE_PREPARE_SHOW,
                LearnSpaceUtil.getHttpClientUrl(),
                loginId, LearnSpaceUtil.getLearnSpaceSiteCode(), domain);
        ((ToUrlGridMenu)gridMenuList.get(1)).setUrl(urlPreview);
    }

    @Override
    public void afterAdd(PeCourse bean) throws EntityException {
        // name 根据id从数据库中查询
        if (LearnSpaceUtil.learnSpaceIsOpen() && "网络课".equals(this.openGeneralDao
                .getById(EnumConst.class, bean.getEnumConstByFlagCourseType().getId()).getName())) {
            try {
                String siteCode = SiteUtil.getSiteCode();
                // 保存课程
                if (!learningSpaceWebService
                        .saveCourse(bean.getId(), bean.getName(), bean.getCode(), siteCode)) {
                    throw new LearningSpaceException();
                }
                String[] courseTeaNewStrArray = {UUIDUtil.getUUID().replace("-", ""),
                        bean.getPeTeacher().getId(), bean.getId(), SiteUtil.getSiteCode(), "1"};
                try {
                    if (!learningSpaceWebService.saveCourseTeacherBatch(Collections
                            .singletonList(courseTeaNewStrArray))) {
                        throw new LearningSpaceException();
                    }
                } catch (Exception e) {
                    try {
                        learningSpaceWebService.removeCourse(bean.getId(), siteCode);
                    } catch (Exception e1) {
                        logger.error("course remove error", e);
                    }
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                logger.error("course add error", e);
                throw new EntityException("课程空间同步失败");
            }
        }
    }

    @Override
    protected void afterUpdate(PeCourse bean) {
        //更新学习平台的课程
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            String siteCode = LearnSpaceUtil.getLearnSpaceSiteCode();
            Map<String, Object> rawCourseInfo = openGeneralDao.getOneMapBySQL("select fk_teacher_id as fk_teacher_id," +
                    "code as code,name as name from pe_course where id=?", bean.getId());
            try {
                learningSpaceWebService.updateCourse(bean.getId(), bean.getName(),
                        bean.getCode(), null, siteCode);
            } catch (Exception e) {
                throw new LearningSpaceException(e);
            }
            if (!bean.getPeTeacher().getId().equals(rawCourseInfo.get("fk_teacher_id"))) {
                String[] courseTeaNewStrArray = {UUIDUtil.getUUID().replace("-", ""),
                        bean.getPeTeacher().getId(), bean.getId(), siteCode, "1"};
                List<Map<String, String>> removeOldMapList = new ArrayList<>();
                List<Map<String, String>> removeNewMapList = new ArrayList<>();
                removeOldMapList.add(new HashMap<String, String>() {{
                    put("teacherid", (String) rawCourseInfo.get("fk_teacher_id"));
                    put("courseid", bean.getId());
                    put("siteCode", siteCode);
                }});
                removeNewMapList.add(new HashMap<String, String>() {{
                    put("teacherid", bean.getPeTeacher().getId());
                    put("courseid", bean.getId());
                    put("siteCode", siteCode);
                }});
                try {
                    if (!learningSpaceWebService.saveCourseTeacherBatch(Collections
                            .singletonList(courseTeaNewStrArray))) {
                        throw new LearningSpaceException();
                    }
                } catch (Exception e) {
                    try {
                        learningSpaceWebService.updateCourse(bean.getId(), (String) rawCourseInfo.get("name"),
                                (String) rawCourseInfo.get("code"), null, siteCode);
                    } catch (Exception e1) {
                        logger.error(e1.getMessage());
                    }
                    throw new LearningSpaceException(e);
                }
                try {
                    if (!learningSpaceWebService.removeCourseTeacherBatch(removeOldMapList)) {
                        logger.error("删除旧教师-课程关联更新失败");
                    }
                } catch (Exception e) {
                    logger.error("删除旧教师-课程关联更新失败");
                }
            }
        }
    }

    @Override
    public void afterDelete(List idList) {
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            List<Map<String, Object>> courseTeaMapList = openGeneralDao.
                    getMapBySQL("SELECT id as courseid, fk_teacher_id as teacherid FROM pe_course WHERE" +
                            CommonUtils.madeSqlIn(idList, "id"));
            String siteCode = LearnSpaceUtil.getLearnSpaceSiteCode();
            List<Map<String, String>> removeMapList = new ArrayList<>();
            List<String[]> courseTeacherList = new ArrayList<>();
            for (Map courseTeaMap : courseTeaMapList) {
                // 删除的课程中有资源教师，则需要删除教师课程关联关系
                if (courseTeaMap.get("teacherid") != null) {
                    String[] courseTeaStrArray = {UUIDUtil.getUUID().replace("-", ""),
                            (String) courseTeaMap.get("teacherid"), (String) courseTeaMap.get("courseid"), siteCode, "1"};
                    removeMapList.add(new HashMap<String, String>() {{
                        put("teacherid", (String) courseTeaMap.get("teacherid"));
                        put("courseid", (String) courseTeaMap.get("courseid"));
                        put("siteCode", siteCode);
                    }});
                    courseTeacherList.add(courseTeaStrArray);
                }
            }
            String ids = CommonUtils.join(idList, CommonConstant.SPLIT_ID_SIGN, null);
            try {
                if (!learningSpaceWebService.removeCourseTeacherBatch(removeMapList)) {
                    throw new LearningSpaceException("删除教师-课程关联关系失败");
                }
            } catch (Exception e) {
                throw new LearningSpaceException(e);
            }
            try {
                if (!learningSpaceWebService.removeCourse(ids, siteCode)) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                try {
                    if (!learningSpaceWebService.saveCourseTeacherBatch(courseTeacherList)) {
                        logger.error("同步已删除的教师-课程关联关系失败");
                    }
                } catch (Exception e1) {
                    logger.error("同步已删除的教师-课程关联关系失败");
                }
                throw new LearningSpaceException(e);
            }
        }
    }

    @Override
    public void afterExcelImport(List<PeCourse> beanList) throws EntityException {
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            List<String> ids = beanList.stream().map(bean -> bean.getId()).collect(Collectors.toList());
            String siteCode = LearnSpaceUtil.getLearnSpaceSiteCode();
            try {
                List<String[]> batchSaveList =
                        beanList.stream().map(e -> new String[]{e.getId(), e.getName(),
                                e.getCode(), siteCode}).collect(Collectors.toList());
                learningSpaceWebService.saveCourseBatch(batchSaveList);
            } catch (Exception e) {
                throw new EntityException("课程空间同步失败");
            }
            List<String[]> courseTeacherList = beanList.stream()
                    .map(course -> new String[]{UUIDUtil.getUUID().replace("-", ""), course.getPeTeacher().getId(),
                            course.getId(), siteCode, "1"}).collect(Collectors.toList());
            try {
                if (!learningSpaceWebService.saveCourseTeacherBatch(courseTeacherList)) {
                    throw new LearningSpaceException("课程-教师关联关系同步失败");
                }
            } catch (Exception e) {
                try {
                    learningSpaceWebService.removeCourse(CommonUtils.join(ids, CommonConstant.SPLIT_ID_SIGN, ""),
                            siteCode);
                } catch (Exception e1) {
                    logger.error("课程移除失败");
                }
                throw new LearningSpaceException(e);
            }
        }
    }
}
