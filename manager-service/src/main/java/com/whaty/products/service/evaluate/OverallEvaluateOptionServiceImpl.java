package com.whaty.products.service.evaluate;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.evaluate.OverallEvaluateOption;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 综合评价选项管理
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("overallEvaluateOptionService")
public class OverallEvaluateOptionServiceImpl extends AbstractTwoLevelListGridServiceImpl<OverallEvaluateOption> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    protected String getParentIdSearchParamName() {
        return "overallEvaluate.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "overallEvaluate.id";
    }

    @Override
    public void checkBeforeAdd(OverallEvaluateOption bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(OverallEvaluateOption bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    /**
     * 检测题目类型与序号  相同的题目类型不可以有重复的序号
     *
     * @param bean
     * @throws EntityException
     */
    private void checkBeforeAddOrUpdate(OverallEvaluateOption bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " and id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from overall_evaluate_option " +
                        "where serial_number = ? and flag_option_type = ? and fk_evaluate_id = ? " + additionalSql,
                bean.getSerialNumber(), bean.getEnumConstByFlagOptionType().getId(),
                bean.getOverallEvaluate().getId())) {
            throw new EntityException("相同类型的题目，序号不可重复");
        }
    }
}
