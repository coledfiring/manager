package com.whaty.products.service.enroll.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.ExamBatch;
import com.whaty.framework.asserts.TycjParameterAssert;
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
@Service("examBatchService")
public class ExamBatchServiceImpl extends TycjGridServiceAdapter<ExamBatch> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(ExamBatch bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
        bean.setEnumConstByFlagActive(myGeneralDao.
                getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_ACTIVE, "0"));
        bean.setSiteCode(SiteUtil.getSiteCode());
    }

    @Override
    public void checkBeforeUpdate(ExamBatch bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
    }

    /**
     * 添加或更新前检查
     * @param bean
     */
    private void checkBeforeAddOrUpdate(ExamBatch bean) throws EntityException {
        if (CommonUtils.compareDateDetail(bean.getEndDate(), bean.getStartDate())) {
            throw new EntityException("批次结束时间不能在开始时间之前");
        }
        if (!ValidateUtils.checkCustomValidate(ValidateUtils.NUMBER_REG_STR, bean.getCode())) {
            throw new EntityException("批次编号只能填写数字");
        }
        String additionalSql = bean.getId() == null ? "" : " AND id <> '" + bean.getId() + "'";
        if (CollectionUtils.isNotEmpty(this.myGeneralDao
                .getBySQL("SELECT 1 FROM exam_batch WHERE site_code = ? AND (NAME = ? OR CODE = ?)" + additionalSql,
                        SiteUtil.getSiteCode(), bean.getName(), bean.getCode()))) {
            throw new EntityException("已存在相同的批次名称或批次编号");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                           ");
        sql.append("    	1                                                            ");
        sql.append("    FROM                                                             ");
        sql.append("    	exam_batch bat                                               ");
        sql.append("    INNER JOIN enum_const active ON active.id = bat.flag_active      ");
        sql.append("    WHERE                                                            ");
        sql.append("    	bat.site_code = ?                                            ");
        sql.append("    AND active. CODE = '1'                                           ");
        sql.append("    AND " + CommonUtils.madeSqlIn(idList, "bat.id"));
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString(), SiteUtil.getSiteCode()))) {
            throw new EntityException("存在已经激活的批次，不能删除");
        }
    }

    /**
     * 激活批次
     * @param ids
     */
    public void doActiveExamBatch(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append("    UPDATE exam_batch bat                                                 ");
        sql.append("    INNER JOIN enum_const notActive ON notActive.namespace = 'flagActive' ");
        sql.append("    AND notActive. CODE = '0'                                             ");
        sql.append("    SET bat.flag_active = notActive.id                                    ");
        sql.append("    WHERE                                                                 ");
        sql.append("    	bat.site_code = ?                                                 ");
        myGeneralDao.executeBySQL(sql.toString(), SiteUtil.getSiteCode());
        sql.delete(0, sql.length());
        sql.append("    UPDATE exam_batch bat                                                 ");
        sql.append("    INNER JOIN enum_const active ON active.namespace = 'flagActive'       ");
        sql.append("    AND active. CODE = '1'                                                ");
        sql.append("    SET bat.flag_active = active.id                                       ");
        sql.append("    WHERE                                                                 ");
        sql.append("    	bat.id = ?                                                        ");
        myGeneralDao.executeBySQL(sql.toString(), ids);
    }
}
