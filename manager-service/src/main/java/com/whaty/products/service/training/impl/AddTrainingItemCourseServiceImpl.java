package com.whaty.products.service.training.impl;

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
@Service("addTrainingItemCourseService")
public class AddTrainingItemCourseServiceImpl extends AbstractTwoLevelListGridServiceImpl<PeCourse> {

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 添加课程到培训项目
     * @param ids
     * @param itemId
     * @return
     */
    public int addToTrainingItem(String ids, String itemId) {
        TycjParameterAssert.isAllNotBlank(ids, itemId);
        String sql = "INSERT INTO training_item_course(id, fk_course_id, fk_training_item_id, create_by, create_date)" +
                " SELECT REPLACE (uuid(), '-', ''), c.id, '" + itemId + "', '" +
                this.userService.getCurrentUser().getId() + "', now() FROM pe_course c WHERE " +
                "NOT EXISTS ( SELECT 1 FROM training_item_course tic WHERE tic.fk_course_id = c.id " +
                "AND tic.fk_training_item_id = '" + itemId + "') and " + CommonUtils.madeSqlIn(ids, "c.id");
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
