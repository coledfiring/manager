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
 * 教师评价统计
 *
 * @author suoqianqgiang
 */
@Lazy
@Service("analyseTeacherInfoService")
public class AnalyseTeacherInfoServiceImpl extends TycjGridServiceAdapter {

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map mapParam) {
        if (MapUtils.isNotEmpty(pageParam.getSearchItem())) {
            String teachDate = (String) pageParam.getSearchItem().remove("teachDate");
            String whereSql = "1 = 1";
            if (StringUtils.isNotBlank(teachDate)) {
                whereSql += " and " + this.handleToSql(teachDate, "cct.training_date");
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
