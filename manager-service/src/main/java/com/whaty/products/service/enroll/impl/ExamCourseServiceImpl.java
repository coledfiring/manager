package com.whaty.products.service.enroll.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.ExamCourse;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 考试科目service
 *
 * @author shanshuai
 */
@Lazy
@Service("examCourseService")
public class ExamCourseServiceImpl extends TycjGridServiceAdapter<ExamCourse> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(ExamCourse bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
        bean.setSiteCode(SiteUtil.getSiteCode());
    }

    @Override
    public void checkBeforeUpdate(ExamCourse bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
    }

    /**
     * 添加或更新前检查
     * @param bean
     */
    private void checkBeforeAddOrUpdate(ExamCourse bean) throws EntityException {
        if (!ValidateUtils.checkCustomValidate(ValidateUtils.NUMBER_REG_STR, bean.getCode())) {
            throw new EntityException("科目编号只能填写数字");
        }
        String additionalSql = bean.getId() == null ? "" : " AND id <> '" + bean.getId() + "'";
        if (CollectionUtils.isNotEmpty(this.myGeneralDao
                .getBySQL("SELECT 1 FROM exam_course WHERE site_code = ? AND (NAME = ? OR CODE = ?)" + additionalSql,
                        SiteUtil.getSiteCode(), bean.getName(), bean.getCode()))) {
            throw new EntityException("已存在相同的科目名称或科目编号");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                           ");
        sql.append("    	1                                                            ");
        sql.append("    FROM                                                             ");
        sql.append("    	exam_course course                                           ");
        sql.append("    INNER JOIN enum_const active ON active.id = course.flag_active   ");
        sql.append("    WHERE                                                            ");
        sql.append("    	course.site_code = ?                                         ");
        sql.append("    AND active. CODE = '1'                                           ");
        sql.append("    AND " + CommonUtils.madeSqlIn(idList, "course.id"));
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString(), SiteUtil.getSiteCode()))) {
            throw new EntityException("存在有效的科目，不能删除");
        }
    }

}
