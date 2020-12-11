package com.whaty.analyse.mvc.service.impl;

import com.whaty.analyse.framework.domain.bean.AnalyseBoardCondition;
import com.whaty.analyse.mvc.AnalyseBoardHelper;
import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 统计看板条件管理
 *
 * @author pingzhihao
 */
@Lazy
@Service("analyseBoardConditionManageService")
public class AnalyseBoardConditionManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<AnalyseBoardCondition> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = "analyseBoardHelper")
    private AnalyseBoardHelper helper;

    /**
     * 新增和更改前处理bean
     *
     * @param bean
     */
    private void handleBeanBeforeAddAndUpdate(AnalyseBoardCondition bean) {
        if (StringUtils.isEmpty(bean.getDefaultSql())) {
            bean.setDefaultSql(null);
        }
        if (StringUtils.isEmpty(bean.getSql())) {
            bean.setSql(null);
        }
    }

    @Override
    public void checkBeforeAdd(AnalyseBoardCondition bean, Map<String, Object> params) throws EntityException {
        this.handleBeanBeforeAddAndUpdate(bean);
        super.checkBeforeAdd(bean, params);
    }

    @Override
    public void checkBeforeUpdate(AnalyseBoardCondition bean) throws EntityException {
        this.handleBeanBeforeAddAndUpdate(bean);
        if (null == bean.getEnumConstByFlagBlockConditionType()) {
            throw new EntityException("请重新选择条件类型");
        }
        super.checkBeforeUpdate(bean);
    }

    @Override
    protected void afterAdd(AnalyseBoardCondition bean) throws EntityException {
        this.helper.removeFromCache(bean.getAnalyseBoardConfig().getId());
    }

    @Override
    protected void afterUpdate(AnalyseBoardCondition instance) throws EntityException {
        this.helper.removeFromCache(instance.getAnalyseBoardConfig().getId());
    }

    @Override
    protected void afterDelete(List idList) throws EntityException {
        this.generalDao.<String>getBySQL("select fk_board_id from analyse_board_block where " +
                CommonUtils.madeSqlIn(idList, "fk_block_id")).forEach(this.helper::removeFromCache);
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "analyseBoardConfig.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "analyseBoardConfig.id";
    }
}
