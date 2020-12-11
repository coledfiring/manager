package com.whaty.products.service.analyse.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 按培训项目汇总
 * @author weipengsen
 */
@Lazy
@Service("analyseLimitTimeService")
public class AnalyseLimitTimeServiceImpl extends TycjGridServiceAdapter {

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map mapParam) {
        if (MapUtils.isNotEmpty(pageParam.getSearchItem())) {
            String startTime = (String) pageParam.getSearchItem().remove("startTime");
            String endTime = (String) pageParam.getSearchItem().remove("endTime");
            Map<String, Object> feeStatus = (Map<String, Object>) pageParam.getSearchItem()
                    .remove("combobox_enumConstByFlagSettleAccountStatus");
            String whereSql = "1 = 1";
            if (StringUtils.isNotBlank(startTime)) {
                whereSql += " and " + this.handleToSql(startTime, "c.start_time");
            }
            if (StringUtils.isNotBlank(endTime)) {
                whereSql += " and " + this.handleToSql(endTime, "c.end_time");
            }
            if (MapUtils.isNotEmpty(feeStatus) && StringUtils.isNotBlank((String) feeStatus.get("feeStatus"))) {
                whereSql += " and st.name = '" + feeStatus.get("feeStatus") + "'";
            }
            gridConfig.gridConfigSource().setSql(gridConfig
                    .gridConfigSource().getSql().replace("where", "where " + whereSql + " and "));
        }
        return super.list(pageParam, gridConfig, mapParam);
    }

    private String handleToSql(String origin, String alias) {
        String[] dateSearch = origin.split("and");
        dateSearch[0] = dateSearch[0].replace(">=", "").trim();
        dateSearch[1] = dateSearch[1].replace("<=", "").trim();
        return "date_format(" + alias + ", '%Y-%m-%d') >= '" + dateSearch[0] +
                "' and date_format(" + alias + ", '%Y-%m-%d') <= '" + dateSearch[1] + "'";
    }
}
