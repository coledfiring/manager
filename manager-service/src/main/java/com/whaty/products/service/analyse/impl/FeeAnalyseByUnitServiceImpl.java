package com.whaty.products.service.analyse.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.grid.bean.GridConfig;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 按培训单位汇总
 *
 * @author weipengsen
 */
@Lazy
@Service("feeAnalyseByUnitService")
public class FeeAnalyseByUnitServiceImpl extends AnalyseLimitTimeServiceImpl {

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map mapParam) {
        if (MapUtils.isNotEmpty(pageParam.getSearchItem())) {
            Map<String, Object> trainingType = (Map<String, Object>) pageParam.getSearchItem()
                    .remove("combobox_enumConstByFlagTrainingType");
            String whereSql = "1 = 1";
            if (MapUtils.isNotEmpty(trainingType)
                    && StringUtils.isNotBlank((String) trainingType.get("trainingType"))) {
                whereSql += " and tt. NAME = '" + trainingType.get("trainingType") + "'";
            }
            gridConfig.gridConfigSource().setSql(gridConfig
                    .gridConfigSource().getSql().replace("where", "where " + whereSql + " and "));
        }
        return super.list(pageParam, gridConfig, mapParam);
    }

}
