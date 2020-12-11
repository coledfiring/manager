package com.whaty.products.service.analyse.impl;

import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.domain.bean.evaluate.OverallEvaluateOption;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 开班评估详情
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("analyseClassEvaluateService")
public class AnalyseClassEvaluateServiceImpl extends AbstractTwoLevelListGridServiceImpl<OverallEvaluateOption> {

    @Override
    public void initGrid(GridConfig gridConfig, Map<String, Object> mapParam) {
        String sql = gridConfig.gridConfigSource().getSql();
        String clId = (String) mapParam.get("parentId");
        gridConfig.gridConfigSource().setSql(sql.replace("%s",clId));
        super.initGrid(gridConfig, mapParam);
    }

    @Override
    protected String getParentIdSearchParamName() {
        return null;
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return null;
    }
}
