package com.whaty.products.service.clazz.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeCourse;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 添加课程到培训项目
 *
 * @author weipengsen
 */
@Lazy
@Service("addClassCourseService")
public class AddClassCourseServiceImpl extends AbstractTwoLevelListGridServiceImpl<PeCourse> {

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

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
        String sql = "INSERT INTO class_course(id, fk_course_id, fk_class_id, create_by, create_date)" +
                " SELECT REPLACE (uuid(), '-', ''), c.id, '" + itemId + "', '" +
                this.userService.getCurrentUser().getId() + "', now() FROM pe_course c WHERE " +
                "NOT EXISTS ( SELECT 1 FROM class_course tic WHERE tic.fk_course_id = c.id " +
                "AND tic.fk_class_id = '" + itemId + "') and " + CommonUtils.madeSqlIn(ids, "c.id");
        return this.myGeneralDao.executeBySQL(sql);
    }

    @Override
    protected String getParentIdSearchParamName() {
        return null;
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return null;
    }
}
