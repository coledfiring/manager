package com.whaty.analyse.mvc.service.impl;

import com.whaty.analyse.framework.domain.bean.AnalyseBoardBlock;
import com.whaty.analyse.mvc.AnalyseBoardHelper;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 统计看板关联统计块管理
 *
 * @author weipengsen
 */
@Lazy
@Service("boardBlockConfigManageService")
public class BoardBlockConfigManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<AnalyseBoardBlock> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = "analyseBoardHelper")
    private AnalyseBoardHelper helper;

    @Override
    protected void afterAdd(AnalyseBoardBlock bean) throws EntityException {
        this.helper.removeFromCache(bean.getAnalyseBoardConfig().getId());
    }

    @Override
    protected void afterUpdate(AnalyseBoardBlock instance) throws EntityException {
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
