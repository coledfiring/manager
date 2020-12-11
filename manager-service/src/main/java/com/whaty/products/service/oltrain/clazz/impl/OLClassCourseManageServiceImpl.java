package com.whaty.products.service.oltrain.clazz.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.util.Page;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.SsoUser;
import com.whaty.domain.bean.online.OlClassCourse;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.products.service.resource.constant.ResourceConstants;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
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
 * @author suoqiangqiang
 */
@Lazy
@Service("olClassCourseManageService")
public class OLClassCourseManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<OlClassCourse> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 添加课程到班级
     * @param ids
     * @param itemId
     * @return
     */
    public int addToClass(String ids, String itemId) {
        TycjParameterAssert.isAllNotBlank(ids, itemId);
        String sql = "INSERT INTO ol_class_course(id, fk_course_id, fk_class_id, create_by, create_date)" +
                " SELECT REPLACE (uuid(), '-', ''), c.id, '" + itemId + "', '" +
                UserUtils.getCurrentUserId() + "', now() FROM ol_pe_course c WHERE " +
                "NOT EXISTS ( SELECT 1 FROM ol_class_course tic WHERE tic.fk_course_id = c.id " +
                "AND tic.fk_class_id = '" + itemId + "') and " + CommonUtils.madeSqlIn(ids, "c.id");
        return this.myGeneralDao.executeBySQL(sql);
    }

    /**
     * 设置考核分数
     *
     * @param ids
     * @param passScore
     * @return
     */
    public int doSetPassScore(String ids, String passScore) {
        TycjParameterAssert.isAllNotBlank(ids, passScore);
        return this.myGeneralDao.executeBySQL("update ol_class_course set pass_score = ? where "
                + CommonUtils.madeSqlIn(ids, "id"), passScore);
    }

    @Override
    public void checkBeforeAdd(OlClassCourse bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        if (this.myGeneralDao.checkNotEmpty("select 1 from ol_class_course where fk_course_id = ? " +
                        "and fk_class_id = ?", bean.getOlPeCourse().getId(),
                bean.getOlPeClass().getId())) {
            throw new EntityException("此课程已存在");
        }
        bean.setCreateBy(this.myGeneralDao.getById(SsoUser.class, UserUtils.getCurrentUserId()));
        bean.setCreateDate(new Date());
    }

    @Override
    protected Page afterList(Page page) {
        String loginId = UserUtils.getCurrentUser().getLoginId();
        String domain;
        try {
            domain = CommonUtils.getServerName() + ":" + CommonUtils.getServerPort();
        } catch (MalformedObjectNameException e) {
            throw new UncheckException(e);
        }
        ((List<Map<String, Object>>) page.getItems()).stream().filter(this::predicatePictureNotExists)
                .forEach(e -> e.put("olPeCourse.courseUrl", CommonConstant.COURSE_DEFAULT_PATH));
        //课程预览按钮
        String urlPreview = String.format(ResourceConstants.LEARNING_SPACE_COURSE_PREPARE_SHOW,
                LearnSpaceUtil.getHttpClientUrl(),
                loginId, LearnSpaceUtil.getLearnSpaceSiteCode(), domain);
        ((List<Map<String, Object>>) page.getItems())
                .forEach(e -> e.put("urlPreview", urlPreview.replace("${value}", (String) e.get("olPeCourse.id"))));
        return super.afterList(page);
    }

    /**
     * 断言图片不存在
     * @param ele
     * @return
     */
    private boolean predicatePictureNotExists(Map<String, Object> ele) {
        String courseUrl = (String) ele.get("olPeCourse.courseUrl");
        return StringUtils.isBlank(courseUrl) || !new File(CommonUtils.getRealPath(courseUrl)).exists();
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "olPeClass.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "olPeClass.id";
    }

}
