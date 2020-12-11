package com.whaty.products.service.enroll.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.ExamSite;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.enroll.constant.StudentEnrollConstant;
import com.whaty.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 考点管理service
 *
 * @author shanshuai
 */
@Lazy
@Service("examSiteService")
public class ExamSiteServiceImpl extends TycjGridServiceAdapter<ExamSite> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(ExamSite bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
        bean.setSiteCode(SiteUtil.getSiteCode());
    }

    @Override
    public void checkBeforeUpdate(ExamSite bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
    }

    /**
     * 添加或更新前检查
     * @param bean
     */
    private void checkBeforeAddOrUpdate(ExamSite bean) throws EntityException {
        if (!ValidateUtils.checkCustomValidate(StudentEnrollConstant.REGEX_EXAM_SITE_CODE, bean.getCode())) {
            throw new EntityException("考点编号只允许填写两位整数数字");
        }

        String additionalSql = bean.getId() == null ? "" : " AND id <> '" + bean.getId() + "'";
        if (CollectionUtils.isNotEmpty(this.myGeneralDao
                .getBySQL("SELECT 1 FROM exam_site WHERE site_code = ? AND (NAME = ? OR CODE = ?)" + additionalSql,
                        SiteUtil.getSiteCode(), bean.getName(), bean.getCode()))) {
            throw new EntityException("已存在相同的考点名称或考点编号");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                           ");
        sql.append("    	1                                                            ");
        sql.append("    FROM                                                             ");
        sql.append("    	exam_site site                                               ");
        sql.append("    INNER JOIN enum_const active ON active.id = site.flag_active     ");
        sql.append("    WHERE                                                            ");
        sql.append("    	site.site_code = ?                                           ");
        sql.append("    AND active. CODE = '1'                                           ");
        sql.append("    AND " + CommonUtils.madeSqlIn(idList, "site.id"));
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString(), SiteUtil.getSiteCode()))) {
            throw new EntityException("存在有效的考点，不能删除");
        }
    }

}
