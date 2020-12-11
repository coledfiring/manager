package com.whaty.products.service.analyse.impl;

import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.SQLHandleUtils;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * 处理统计业务抽象类
 *
 * @author pingzhihao
 */
public abstract class AbstractAnalyseGridService extends TycjGridServiceAdapter {

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map mapParam) {
        // 处理时间段搜索参数
        String[] timePeriodArr = Optional.ofNullable(pageParam.getSearchItem())
                .filter(MapUtils::isNotEmpty)
                .map(e -> (String) e.remove("timePeriod"))
                .map(this::handleTimePeriod)
                .map(Arrays::asList)
                .orElseGet(ArrayList::new).toArray(new String[0]);
        // 填充sql中ConditionSign占位符
        gridConfig.gridConfigSource().setSql(SQLHandleUtils.replaceConditionSign(
                gridConfig.gridConfigSource().getSql(), timePeriodArr));
        return super.list(pageParam, gridConfig, mapParam);
    }

    /**
     * 处理时间段参数并生成替换的sql片段
     *
     * @param beginTime 起始时间
     * @param endTime 结束时间
     * @return
     */
    protected abstract String getTimeConditionSql(String beginTime, String endTime);

    /**
     * 处理前端返回的时间戳字符串
     *
     * @param timePeriodStr   时间戳字符串
     * @return
     */
    private String handleTimePeriod(String timePeriodStr) {
        String[] array = Optional.of(timePeriodStr.split("and"))
                .filter(e -> e.length == 2)
                .map(this::convertConditionArr)
                .orElseThrow(IllegalArgumentException::new);
        return getTimeConditionSql(array[0], array[1]);
    }

    /**
     * 转换数据格式
     * @param origin
     * @return
     */
    private String[] convertConditionArr(String[] origin) {
        BiFunction<String, String, String> handleFunction = (e, sign) -> e.replace(sign, "").trim();
        return new String[] {handleFunction.apply(origin[0], ">="), handleFunction.apply(origin[1], "<=")};
    }
}
