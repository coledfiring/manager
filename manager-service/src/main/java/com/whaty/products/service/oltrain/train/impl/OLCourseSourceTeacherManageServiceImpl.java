package com.whaty.products.service.oltrain.train.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.util.UUIDUtil;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.online.OlCourseTeacher;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.LearningSpaceException;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 课程资源教师管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("olCourseSourceTeacherManageService")
public class OLCourseSourceTeacherManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<OlCourseTeacher> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    @Override
    public void checkBeforeAdd(OlCourseTeacher bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        EnumConst type = myGeneralDao
                .getEnumConstByNamespaceCode("FlagOnlineCourseTeacherType", "2");
        if (this.myGeneralDao.checkNotEmpty("select 1 from ol_course_teacher where flag_online_course_teacher_type = ?" +
                        " and fk_course_id = ? and fk_teacher_id = ?", type.getId(), bean.getOlPeCourse().getId(),
                bean.getOlPeTeacher().getId())) {
            throw new EntityException("当前课程已存在此资源教师");
        }
        bean.setEnumConstByFlagOnlineCourseTeacherType(type);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
    }

    @Override
    public void afterAdd(OlCourseTeacher bean) throws EntityException {
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            if (this.myGeneralDao.checkNotEmpty("select 1 from ol_course_teacher oct" +
                            " inner join enum_const ct on ct.id = oct.flag_online_course_teacher_type" +
                            " where ct.code = '1' and fk_course_id = ? and fk_teacher_id = ?",
                    bean.getOlPeCourse().getId(), bean.getOlPeTeacher().getId())) {
                return;
            }
            String[] courseTeaNewStrArray = {UUIDUtil.getUUID().replace("-", ""),
                    bean.getOlPeTeacher().getId(), bean.getOlPeCourse().getId(), SiteUtil.getSiteCode(), "1"};
            try {
                if (!learningSpaceWebService.saveCourseTeacherBatch(Collections
                        .singletonList(courseTeaNewStrArray))) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                throw new LearningSpaceException();
            }
        }
    }

    @Override
    protected void afterDelete(List idList) throws EntityException {
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            List<Map<String, Object>> diffIdList = this.myGeneralDao.getMapBySQL("select " +
                    "   ol.fk_teacher_id as teacherid," +
                    "   ol.fk_course_id as courseid" +
                    " from ol_course_teacher ol" +
                    " left join ol_course_teacher ol1 on ol1.fk_course_id = ol.fk_course_id" +
                    " and ol1.fk_teacher_id = ol.fk_teacher_id" +
                    " and ol.id<>ol1.id" +
                    " WHERE ol1.id is null");
            String siteCode = LearnSpaceUtil.getLearnSpaceSiteCode();
            List<Map<String, String>> removeMapList = diffIdList.stream().map(e -> new HashMap<String, String>() {{
                put("teacherid", (String) e.get("teacherid"));
                put("courseid", (String) e.get("courseid"));
                put("siteCode", siteCode);
            }}).collect(Collectors.toList());
            try {
                this.learningSpaceWebService.removeCourseTeacherBatch(removeMapList);
            } catch (Exception e) {
                throw new LearningSpaceException(e);
            }
        }
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "olPeCourse.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "olPeCourse.id";
    }
}
