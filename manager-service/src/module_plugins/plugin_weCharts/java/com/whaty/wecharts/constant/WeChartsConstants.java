package com.whaty.wecharts.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 常量池
 * @author weipengsen
 */
public interface WeChartsConstants {

    /**
     * 参数，code
     */
    String PARAM_CODE = "code";
    /**
     * 参数，params
     */
    String PARAM_PARAMS = "params";
    /**
     * 参数，isCache
     */
    String IS_CACHE = "isCache";
    /** 统计图表的缓存key **/
    String STATISTICS_CHART_CACHE_KEY = "statistics_chart_cache_key_%s_%s";
    /**
     * 需要读缓存
     */
    String DO_CACHE = "1";
    /**
     * 不需要读缓存
     */
    String NOT_CACHE = "0";
    /**
     * 数据看板显示图表code系统常量
     */
    String SYSTEM_VARIABLES_DATABOARD_CHART_CODE = "dataBoardChartCodeList";
}
