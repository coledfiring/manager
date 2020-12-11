package com.whaty.analyse.mvc.service.impl;

import com.whaty.analyse.framework.dao.AnalyseBlockDao;
import com.whaty.analyse.framework.domain.AnalyseConditionVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBlockConfig;
import com.whaty.analyse.mvc.service.AnalyseBlockService;
import com.whaty.framework.asserts.TycjParameterAssert;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 统计块显示
 *
 * @author weipengsen
 */
@Lazy
@Service("analyseBlockService")
public class AnalyseBlockServiceImpl implements AnalyseBlockService {

    @Resource(name = "analyseBlockDao")
    private AnalyseBlockDao analyseBlockDao;

    @Override
    public AnalyseBlockConfig getBlock(String id) {
        TycjParameterAssert.isAllNotBlank(id);
        AnalyseBlockConfig config = this.analyseBlockDao.getById(id);
        config.handleAnalyseCode();
        config.setConditions(AnalyseConditionVO
                .convert(this.analyseBlockDao.listConditions(config.getId()), null));
        if (config.isShift()) {
            config.setShiftTabs(this.analyseBlockDao.listShiftTabs(config.getAnalyseCodes()));
        }
        return config;
    }
}
