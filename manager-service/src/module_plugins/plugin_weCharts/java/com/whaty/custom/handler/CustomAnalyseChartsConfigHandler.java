package com.whaty.custom.handler;

import com.whaty.custom.bean.CustomAnalyseGroup;
import com.whaty.custom.constant.AnalyseConstant;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.sqlflow.SqlFlowHandler;
import com.whaty.wecharts.bean.PeChartColumnDef;
import com.whaty.wecharts.bean.PeChartDef;
import com.whaty.wecharts.chart.ChartColumnType;
import com.whaty.wecharts.chart.ChartConfig;
import com.whaty.wecharts.chart.essh.DataDirection;
import com.whaty.wecharts.exception.WeChartsServiceException;
import com.whaty.wecharts.jsonbean.Option;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 自定义统计统计图配置处理类
 * @author weipengsen
 */
public class CustomAnalyseChartsConfigHandler {
    /**
     * 自定义统计组
     */
    private CustomAnalyseGroup group;
    /**
     * 名称
     */
    private String name;
    /**
     * 统计图表类型
     */
    private String chartsType;
    /**
     * 统计图表配置
     */
    private Map<String, Object> charts;
    /**
     * 条件配置
     */
    private List<Map<String, Object>> conditions;

    private GeneralDao generalDao;
    /**
     * 动态列
     */
    List<Map<String, Object>> columns;

    /**
     * 根据配置生成charts对象的构造器
     * @param name
     * @param group
     * @param chartsType
     * @param charts
     * @param conditions
     */
    public CustomAnalyseChartsConfigHandler(String name, CustomAnalyseGroup group, String chartsType,
                                            Map<String, Object> charts, List<Map<String, Object>> conditions) {
        this.name = name;
        this.group = group;
        this.chartsType = chartsType;
        this.charts = charts;
        this.conditions = conditions;
    }

    /**
     * 将前台配置转化成统计图表配置
     * @return
     */
    public Option handle() throws WeChartsServiceException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        PeChartDef chart = this.handleConfigToChartConfig();
        ChartConfig chartConfig = new ChartConfig();
        chartConfig.addChartDef(chart);
        return chartConfig.getAllCharts().get(0);
    }

    /**
     * 将配置转化为统计配置
     * @return
     */
    public PeChartDef handleConfigToChartConfig() throws InvocationTargetException, IllegalAccessException {
        PeChartDef peChartDef = new PeChartDef();
        // 初始化统计图表
        peChartDef.setChart(this.name);
        peChartDef.setCode(UUID.randomUUID().toString().replace("-", ""));
        String dataDirection = ChartColumnType.valueOf(this.chartsType) != ChartColumnType.pieValue
                ? "horizontal" : "vertical";
        peChartDef.setDataDirection(DataDirection.valueOf(dataDirection));
        peChartDef.setHasTimeline(false);
        peChartDef.setHasDataZoom(true);
        peChartDef.setDataZoomStart(0);
        peChartDef.setDataZoomEnd(70);
        peChartDef.setZoomLock(false);
        peChartDef.setType(ChartColumnType.valueOf(this.chartsType));
        peChartDef.setInputDate(new Date());
        // 生成动态的维度列
        Map<String, Object> params = new HashMap<>(4);
        params.put(AnalyseConstant.PARAM_CHARTS, this.charts);
        params.put(AnalyseConstant.PARAM_CONDITIONS, this.conditions);
        if (ChartColumnType.valueOf(this.chartsType) != ChartColumnType.pieValue) {
            columns = this.getGeneralDao().getMapBySQL(this.generateColumnSql(params));
            if (CollectionUtils.isEmpty(columns)) {
                throw new ServiceException("没有可以统计的数据");
            }
            StringBuilder valueColumns = new StringBuilder();
            columns.forEach(e -> valueColumns.append(e.get(AnalyseConstant.ARG_CODE)).append(","));
            valueColumns.delete(valueColumns.length() - 1, valueColumns.length());
            peChartDef.setValueColumnsStr(valueColumns.toString());
            // 生成统计项列
            String dataIndex = (String) ((Map<String, Object>) this.charts.get(AnalyseConstant.PARAM_ANALYSES))
                    .get(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_DATA_INDEX);
            String code = dataIndex.substring(dataIndex.indexOf(".") + 1);
            peChartDef.setDataIndexColumn(code);
        } else {
            columns = new ArrayList<>();
            peChartDef.setDataIndexColumn("-1");
        }
        // 生成动态查询sql
        params.put(AnalyseConstant.PARAM_COLUMNS, columns);
        String sql = this.group.getChartsSql();
        SqlFlowHandler handler = new SqlFlowHandler(sql, params);
        handler.handle();
        peChartDef.setChartSql(handler.getSql());
        // 生成列
        peChartDef.setColumnDefList(this.generateColumn(peChartDef));
        return peChartDef;
    }

    /**
     * 生成动态列sql
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private String generateColumnSql(Map<String, Object> params)
            throws InvocationTargetException, IllegalAccessException {
        String columnSql = this.group.getColumnSql();
        SqlFlowHandler columnHandler = new SqlFlowHandler(columnSql, params);
        columnHandler.handle();
        return columnHandler.getSql();
    }

    /**
     * 生成统计列
     * @return
     */
    private List<PeChartColumnDef> generateColumn(PeChartDef peChartDef) {
        List<PeChartColumnDef> columnList = new ArrayList<>();
        int index = 0;
        // 生成统计项列
        Map<String, Object> analyses = (Map<String, Object>) this.charts.get(AnalyseConstant.PARAM_ANALYSES);
        String dataIndex = (String) analyses.get(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_DATA_INDEX);
        String code = dataIndex.substring(dataIndex.indexOf(".") + 1);
        if (ChartColumnType.valueOf(this.chartsType) != ChartColumnType.pieValue) {
            columnList.add(this.generateColumn((String) analyses.get(AnalyseConstant.PARAM_NAME),
                    code, "gridName", index++, peChartDef));
        } else {
            columnList.add(this.generateColumn((String) analyses.get(AnalyseConstant.PARAM_NAME),
                    code, "pieName", index++, peChartDef));
            columnList.add(this.generateColumn((String) analyses.get(AnalyseConstant.PARAM_NAME),
                    "number", "pieValue", index++, peChartDef));
        }
        // 生成统计维度列
        for (Map<String, Object> column : this.columns) {
            columnList.add(this.generateColumn((String) column.get(AnalyseConstant.PARAM_NAME),
                    (String) column.get(AnalyseConstant.ARG_CODE), "gridName", index ++, peChartDef));
        }
        return columnList;
    }

    /**
     * 生成统计列
     * @param name
     * @param code
     * @param serialNumber
     * @param peChartDef
     * @param columnType
     * @return
     */
    private PeChartColumnDef generateColumn(String name, String code, String columnType, int serialNumber, PeChartDef peChartDef) {
        PeChartColumnDef column = new PeChartColumnDef();
        column.setPeChartDef(peChartDef);
        column.setColumnIndex(serialNumber);
        column.setType(ChartColumnType.valueOf(columnType));
        column.setAxisIndex(0);
        column.setSeriesName(name);
        column.setColumnName(code);
        column.setInputDate(new Date());
        return column;
    }

    public GeneralDao getGeneralDao() {
        if (this.generalDao == null) {
            this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.GENERAL_DAO_BEAN_NAME);
        }
        return generalDao;
    }

    public CustomAnalyseGroup getGroup() {
        return group;
    }

    public void setGroup(CustomAnalyseGroup group) {
        this.group = group;
    }

    public String getChartsType() {
        return chartsType;
    }

    public void setChartsType(String chartsType) {
        this.chartsType = chartsType;
    }

    public Map<String, Object> getCharts() {
        return charts;
    }

    public void setCharts(Map<String, Object> charts) {
        this.charts = charts;
    }

    public List<Map<String, Object>> getConditions() {
        return conditions;
    }

    public void setConditions(List<Map<String, Object>> conditions) {
        this.conditions = conditions;
    }
}
