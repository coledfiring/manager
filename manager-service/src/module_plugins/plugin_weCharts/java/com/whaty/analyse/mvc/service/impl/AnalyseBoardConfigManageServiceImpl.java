package com.whaty.analyse.mvc.service.impl;

import com.whaty.analyse.framework.domain.bean.AnalyseBoardConfig;
import com.whaty.analyse.mvc.AnalyseBoardHelper;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 统计看板配置管理
 *
 * @author weipengsen
 */
@Lazy
@Service("analyseBoardConfigManageService")
public class AnalyseBoardConfigManageServiceImpl extends TycjGridServiceAdapter<AnalyseBoardConfig> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = "analyseBoardHelper")
    private AnalyseBoardHelper helper;

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.generalDao.checkNotEmpty("select 1 from analyse_block_config where " +
                CommonUtils.madeSqlIn(idList, "fk_detail_board_id"))) {
            throw new EntityException("存在块引用了的详情看板");
        }
    }

    @Override
    protected void afterUpdate(AnalyseBoardConfig instance) throws EntityException {
        this.helper.removeFromCache(instance.getId());
    }
}
