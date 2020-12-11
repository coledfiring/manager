package com.whaty.products.service.clazz.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.LiveCourse;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 直播课程管理
 *
 * @author weipengsen
 */
@Lazy
@Service("liveCourseManageService")
public class LiveCourseManageServiceImpl extends TycjGridServiceAdapter<LiveCourse> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(LiveCourse bean, Map<String, Object> params) throws EntityException {
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
    }

    @Override
    public void checkBeforeUpdate(LiveCourse bean) throws EntityException {
        bean.setUpdateBy(UserUtils.getCurrentUser());
        bean.setUpdateDate(new Date());
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.myGeneralDao.checkNotEmpty("select 1 from live_course c " +
                "inner join enum_const ac on ac.id = c.flag_active where ac.code = '1' and " +
                CommonUtils.madeSqlIn(idList, "c.id"))) {
            throw new EntityException("不能删除有效的数据");
        }
    }
}
