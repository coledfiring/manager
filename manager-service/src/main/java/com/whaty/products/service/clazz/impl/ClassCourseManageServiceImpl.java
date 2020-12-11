package com.whaty.products.service.clazz.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.ClassCourse;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.products.service.resource.constant.ResourceConstants;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.MalformedObjectNameException;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 班级课程管理
 *
 * @author weipengsen
 */
@Lazy
@Service("classCourseManageService")
public class ClassCourseManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<ClassCourse> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public void checkBeforeAdd(ClassCourse bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        if (this.myGeneralDao.checkNotEmpty("select 1 from class_course where fk_course_id = ? " +
                        "and fk_class_id = ?", bean.getPeCourse().getId(),
                bean.getPeClass().getId())) {
            throw new EntityException("此课程已存在");
        }
        bean.setCreateBy(this.myGeneralDao.getById(SsoUser.class, this.userService.getCurrentUser().getId()));
        bean.setCreateDate(new Date());
    }

    @Override
    protected Page afterList(Page page) {
        String loginId = this.userService.getCurrentUser().getLoginId();
        String domain;
        try {
            domain = CommonUtils.getServerName() + ":" + CommonUtils.getServerPort();
        } catch (MalformedObjectNameException e) {
            throw new UncheckException(e);
        }
        ((List<Map<String, Object>>) page.getItems()).stream().filter(this::predicatePictureNotExists)
                .forEach(e -> e.put("peCourse.courseUrl", CommonConstant.COURSE_DEFAULT_PATH));
        //课程预览按钮
        String urlPreview = String.format(ResourceConstants.LEARNING_SPACE_COURSE_PREPARE_SHOW,
                LearnSpaceUtil.getHttpClientUrl(),
                loginId, LearnSpaceUtil.getLearnSpaceSiteCode(), domain);
        ((List<Map<String, Object>>) page.getItems())
                .forEach(e -> e.put("urlPreview", urlPreview.replace("${value}", (String) e.get("peCourse.id"))));
        return super.afterList(page);
    }

    /**
     * 断言图片不存在
     * @param ele
     * @return
     */
    private boolean predicatePictureNotExists(Map<String, Object> ele) {
        String courseUrl = (String) ele.get("peCourse.courseUrl");
        return StringUtils.isBlank(courseUrl) || !new File(CommonUtils.getRealPath(courseUrl)).exists();
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "peClass.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "peClass.id";
    }

}
