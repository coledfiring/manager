package com.whaty.products.service.evaluate;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
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
 * 教学督导评价管理  OverallEvaluate 表code标识为trainingSupervision
 *
 * @author pingzhihao
 */
@Lazy
@Service("supervisionEvaluateManageService")
public class SupervisionEvaluateManageServiceImpl extends TycjGridServiceAdapter<OverallEvaluate> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 教学督导专属标识code码
     */
    private static final String OVERALL_EVALUATE_CODE = "trainingSupervision";

    @Override
    public void checkBeforeAdd(OverallEvaluate bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        super.checkBeforeAdd(bean);
        bean.setCreateDate(new Date());
        bean.setCreateUser(UserUtils.getCurrentUser());
    }

    @Override
    public void checkBeforeUpdate(OverallEvaluate bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        super.checkBeforeUpdate(bean);
    }

    /**
     * 更新和添加前检查
     *
     * @param bean
     */
    private void checkBeforeAddOrUpdate(OverallEvaluate bean) throws EntityException {
        boolean hasId = StringUtils.isEmpty(bean.getId());
        bean.setCode(OVERALL_EVALUATE_CODE);
        // 判断是否为有效
        EnumConst enumConstByFlagIsvalid = bean.getEnumConstByFlagIsvalid();
        if (enumConstByFlagIsvalid == null || StringUtils.isBlank(enumConstByFlagIsvalid.getId())) {
            if (!hasId) {
                //添加时设置为无效状态
                bean.setEnumConstByFlagIsvalid(myGeneralDao.getEnumConstByNamespaceCode(
                        EnumConstConstants.ENUM_CONST_NAMESPACE_FLAG_IS_VALID, "0"));
            }
            return;
        }
        String additionalSql = hasId ? "" : "AND oe.id <> '" + bean.getId() + "'";
        if ("2".equals(enumConstByFlagIsvalid.getId()) &&
                this.myGeneralDao.checkNotEmpty("select 1 from overall_evaluate oe" +
                                " INNER JOIN enum_const ec ON ec.id = oe.flag_isvalid AND ec.code = '1' " +
                                " where oe.code = 'trainingSupervision' and oe.site_code = ? " + additionalSql,
                        SiteUtil.getSiteCode())) {
            throw new EntityException("已存在有效的评价");
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
