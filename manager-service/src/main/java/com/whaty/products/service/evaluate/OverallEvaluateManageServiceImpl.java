package com.whaty.products.service.evaluate;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.evaluate.OverallEvaluate;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 班级综合评价管理
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("overallEvaluateManageService")
public class OverallEvaluateManageServiceImpl extends TycjGridServiceAdapter<OverallEvaluate> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(OverallEvaluate bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
        super.checkBeforeAdd(bean);
        bean.setCreateDate(new Date());
        bean.setCreateUser(UserUtils.getCurrentUser());
    }

    @Override
    public void checkBeforeUpdate(OverallEvaluate bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
        super.checkBeforeUpdate(bean);
    }

    /**
     * 更新和添加前检查
     *
     * @param bean
     */
    private void checkBeforeAddOrUpdate(OverallEvaluate bean) throws EntityException {
        String additionalSql = StringUtils.isEmpty(bean.getId()) ? "" : "AND id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from overall_evaluate " +
                "where code = '" + bean.getCode() + "' and site_code = ?" + additionalSql, SiteUtil.getSiteCode())) {
            throw new EntityException("编码已存在");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.myGeneralDao.checkNotEmpty("select 1 from overall_evaluate oe " +
                "inner join enum_const ac on ac.id = oe.flag_isvalid where ac.code = '1' and "
                + CommonUtils.madeSqlIn(idList, "oe.id"))) {
            throw new EntityException("存在有效的数据，无法删除");
        }
    }
}
