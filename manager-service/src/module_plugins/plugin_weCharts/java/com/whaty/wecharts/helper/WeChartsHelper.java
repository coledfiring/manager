package com.whaty.wecharts.helper;


import com.alibaba.fastjson.JSON;
import com.whaty.cache.CacheKeys;
import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.wecharts.bean.PeChartColumnDef;
import com.whaty.wecharts.bean.PeChartDef;
import com.whaty.wecharts.chart.ChartConfig;
import com.whaty.wecharts.constant.WeChartsConstants;
import com.whaty.wecharts.exception.WeChartsServiceException;
import com.whaty.wecharts.jsonbean.Option;
import com.whaty.wecharts.jsonbean.code.SeriesType;
import com.whaty.wecharts.service.ChartManagerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * weCharts辅助类
 * @author weipengsen
 */
@Lazy
@Component("weChartsHelper")
@Scope("prototype")
public class WeChartsHelper {

    private String functionCode;

    private String chartName;

    private List<String> allCharts;

    @Resource(name = "chartManagerService")
    private ChartManagerService chartManagerService;

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    private ChartConfig chartConfig = new ChartConfig();

    protected void chartDefine(String functionCode) throws WeChartsServiceException {
        PeChartDef peChartDef = this.getChartDefs(functionCode);
        this.getChartConfig().addChartDef(peChartDef);
    }

    /**
     * 查询图表
     *
     * @param code
     * @return
     * @throws WeChartsServiceException
     */
    public PeChartDef getChartDefs(String code) throws WeChartsServiceException {
        if (StringUtils.isBlank(code)) {
            throw new WeChartsServiceException("显示的图表至少要给出图表名称、图表所属功能之一");
        }
        PeChartDef peChartDef = chartManagerService.queryChartByCode(code);
        List<PeChartColumnDef> chartColumnDefs = chartManagerService.queryColumnByChart(peChartDef.getId());
        peChartDef.setColumnDefList(chartColumnDefs);
        return peChartDef;
    }

    /**
     * 查询图表
     *
     * @param code
     * @return
     */
    public Option queryChart(String code, String params, String isCache) throws NoSuchMethodException,
            WeChartsServiceException, IllegalAccessException, InvocationTargetException {
        String cachePath = String.format(WeChartsConstants.STATISTICS_CHART_CACHE_KEY, SiteUtil.getSiteCode(), code);
        if (StringUtils.isNotBlank(isCache) && isCache.equals(WeChartsConstants.DO_CACHE)) {
            Option optionObj = redisCacheService.getFromCache(cachePath);
            if (optionObj != null) {
                return optionObj;
            }
        }
        Map paramMap;
        if (StringUtils.isNotBlank(params)) {
            params = "{" + params + "}";
            paramMap = JSON.parseObject(params, Map.class);
            this.getChartConfig().setParamMap(paramMap);
        }
        this.chartDefine(code);
        List<Option> charts = this.getChartConfig().getAllCharts();
        if (CollectionUtils.isEmpty(charts)) {
            throw new WeChartsServiceException("不存在此图表");
        }
        Option option = charts.get(0);
        String chartCodeStr = this.redisCacheService.getFromCache(String.format(CacheKeys.SYSTEM_VARIABLES_CACHE_KEY.getKey(),
                WeChartsConstants.SYSTEM_VARIABLES_DATABOARD_CHART_CODE,
                SiteUtil.getSiteCode()));
        if (chartCodeStr.contains(code)) {
            option.title().show(false);
            if (!option.getSeries().get(0).getType().equals(SeriesType.pie)) {
                option.dataZoom().show(false);
            }
        }
        redisCacheService.putToCache(cachePath, option);
        return option;
    }

    public ChartConfig getChartConfig() {
        return chartConfig;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public List<String> getAllCharts() {
        return allCharts;
    }

    public void setAllCharts(List<String> allCharts) {
        this.allCharts = allCharts;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

}
