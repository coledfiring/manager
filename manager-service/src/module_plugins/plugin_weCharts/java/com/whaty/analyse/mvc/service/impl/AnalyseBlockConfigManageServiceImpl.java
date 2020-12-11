package com.whaty.analyse.mvc.service.impl;

import com.whaty.analyse.framework.domain.bean.AnalyseBlockConfig;
import com.whaty.analyse.mvc.AnalyseBoardHelper;
import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 统计块配置管理
 *
 * @author weipengsen
 */
@Lazy
@Service("analyseBlockConfigManageService")
public class AnalyseBlockConfigManageServiceImpl extends TycjGridServiceAdapter<AnalyseBlockConfig> {

    @Resource(name = "analyseBoardHelper")
    private AnalyseBoardHelper helper;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeAdd(AnalyseBlockConfig bean, Map<String, Object> params) throws EntityException {
        bean.setCreateDate(new Date());
        bean.setDetailBoardId(StringUtils.isNotBlank(bean.getDetailBoardId()) ? bean.getDetailBoardId() : null);
    }

    @Override
    public void checkBeforeUpdate(AnalyseBlockConfig bean) throws EntityException {
        bean.setDetailBoardId(StringUtils.isNotBlank(bean.getDetailBoardId()) ? bean.getDetailBoardId() : null);
    }

    @Override
    protected void afterUpdate(AnalyseBlockConfig instance) throws EntityException {
        this.generalDao.<String>getBySQL("select fk_board_id from analyse_board_block where fk_block_id = ?",
                        instance.getId()).forEach(this.helper::removeFromCache);
    }
}
