package com.whaty.wecharts.chart;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.util.JsonUtil;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.utils.UserUtils;
import com.whaty.wecharts.bean.PeChartColumnDef;
import com.whaty.wecharts.bean.PeChartDef;
import com.whaty.wecharts.chart.essh.ColumnConfig;
import com.whaty.wecharts.chart.essh.ColumnIndex;
import com.whaty.wecharts.chart.essh.DataDirection;
import com.whaty.wecharts.exception.WeChartsServiceException;
import com.whaty.wecharts.jsonbean.Option;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.JavaScriptUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChartConfig {

    /**
     * 纵向图表的图表定义
     */
    private Map<String, PeChartDef> chartDefMap;

    /**
     * 图表(数据)
     */
    private Map<String, ChartData> chartMap;

    private Map<String, Object> paramMap;

    private GeneralDao generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.GENERAL_DAO_BEAN_NAME);

    public ChartConfig() {
        this.chartDefMap = new HashMap<>();
        this.chartMap = new HashMap<>();
        this.paramMap = new HashMap<>();
    }

    public PeChartDef addChartDef(PeChartDef peChartDef) throws WeChartsServiceException {
        if (StringUtils.isBlank(peChartDef.getChart())) {
            throw new WeChartsServiceException("图表名称为空");
        }
        return this.chartDefMap.put(peChartDef.getChart(), peChartDef);
    }

    /**
     * 普通纵向数据列表生成带时间轴的图表
     *
     * @param chart
     * @param hasTimeline
     * @return
     */
    public PeChartDef addChartDef(String chart, boolean hasTimeline) {
        return this.addChartDef(chart, DataDirection.vertical, hasTimeline, false, 0, 70, false, null, "", null, null);
    }

    /**
     * 普通纵向数据列表生成带时间轴、数据缩放区域的图表
     *
     * @param chart
     * @param hasDataZoom
     * @param dataZoomStart
     * @param dataZoomEnd
     * @param zoomLock
     * @return
     */
    public PeChartDef addChartDef(String chart, boolean hasTimeline, boolean hasDataZoom, Integer dataZoomStart,
                                  Integer dataZoomEnd, boolean zoomLock) {
        return this.addChartDef(chart, DataDirection.vertical, hasTimeline, hasDataZoom, dataZoomStart, dataZoomEnd, zoomLock, null, "", null, null);
    }

    /**
     * 普通横向数据列表生成带时间轴的图表
     *
     * @param chart
     * @param hasTimeline
     * @param type
     * @param dataIndexColumn
     * @param valueColumns
     * @return
     */
    public PeChartDef addChartDef(String chart, boolean hasTimeline, ChartColumnType type, String dataIndexColumn, Integer[] valueColumns) {
        return this.addChartDef(chart, DataDirection.horizontal, hasTimeline, false, 0, 70, false, type, dataIndexColumn, valueColumns, null);
    }

    /**
     * 列表生成图表定义之完全体
     *
     * @param chart
     * @param dataDirection
     * @param hasTimeline
     * @param hasDataZoom
     * @param dataZoomStart
     * @param dataZoomEnd
     * @param zoomLock
     * @param type
     * @param dataIndexColumn
     * @param valueColumns
     * @param columnDefList
     * @return
     */
    public PeChartDef addChartDef(String chart, DataDirection dataDirection,
                                  boolean hasTimeline, boolean hasDataZoom, Integer dataZoomStart,
                                  Integer dataZoomEnd, boolean zoomLock, ChartColumnType type,
                                  String dataIndexColumn, Integer[] valueColumns,
                                  List<PeChartColumnDef> columnDefList) {
        return this.chartDefMap.put(chart,
                new PeChartDef(chart, dataDirection, hasTimeline, hasDataZoom, dataZoomStart,
                        dataZoomEnd, zoomLock, type, dataIndexColumn, valueColumns, columnDefList));
    }

    /**
     * 添加纵向图表列定义
     *
     * @param index
     * @param chart
     * @param type
     * @param seriesName
     */
    public void addColumnDef(int index, String chart, ChartColumnType type, String seriesName) {
        this.addColumnDef(index, chart, type, seriesName, null, 0);
    }

    public void addColumnDef(int index, String chart, ChartColumnType type, String seriesName, String group) {
        this.addColumnDef(index, chart, type, seriesName, group, 0);
    }

    public void addColumnDef(int index, String chart, ChartColumnType type, String seriesName, int axisIndex) {
        this.addColumnDef(index, chart, type, seriesName, null, axisIndex);
    }

    public void addColumnDef(int index, String chart, ChartColumnType type, String seriesName, String group, int axisIndex) {
        PeChartDef chartDef = this.chartDefMap(chart);
        PeChartColumnDef columnDef = new PeChartColumnDef(index, chartDef, type, seriesName, group, axisIndex);
        chartDef.columnDefList(columnDef);
    }

    public List<Option> getAllCharts() throws InvocationTargetException, NoSuchMethodException,
            IllegalAccessException, WeChartsServiceException {
        List<Option> list = new ArrayList<>();
        Iterator iterator = this.chartDefMap.keySet().iterator();
        while (iterator.hasNext()) {
            String chartName = (String) iterator.next();
            list.add(this.getChart(chartName));
        }
        return list;
    }

    /**
     * 获取图表
     *
     * @param chart
     * @return
     * @throws WeChartsServiceException
     */
    public Option getChart(String chart) throws WeChartsServiceException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        PeChartDef chartDef = this.chartDefMap.get(chart);
        if (chartDef == null) {
            throw new WeChartsServiceException("没有定义此图表");
        }
        return this.getChart(chartDef);
    }

    /**
     * 获取图表
     *
     * @param chartDef
     * @return
     * @throws WeChartsServiceException
     */
    public Option getChart(PeChartDef chartDef) throws WeChartsServiceException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ChartData chartData;
        //纵向图表
        if (DataDirection.vertical.equals(chartDef.getDataDirection())) {
            chartData = this.analyseVerticalDataChart(chartDef);
        } else {
            chartData = this.analyseRowList(chartDef);
        }

        EchartsUtil echartsUtil = new EchartsUtil(chartData);
        Option option = echartsUtil.chartDataToEcharts();
        return option;
    }

    //生成纵向图表 ------------- start -----------------------------

    /**
     * 纵向数据图表数据准备初始化
     *
     * @param chartDef
     * @return
     * @throws WeChartsServiceException
     */
    protected ChartData initVerticalDataChartBasic(PeChartDef chartDef) throws WeChartsServiceException {
        ChartData chartData = this.chartMap(chartDef);

        Map<String, ColumnConfig> columnConfigs = buildMap(chartDef);

        for (PeChartColumnDef columnDef : chartDef.getColumnDefList()) {
            String cIndex = columnDef.getColumnName();
            String seriesName = columnDef.getSeriesName();
            ColumnConfig columnConfig = columnConfigs.get(cIndex);

            //数据列类型为gridName时，此数据列为坐标轴数据内容；否则为系列中的数据
            if (!columnDef.getType().equals(ChartColumnType.gridName)) {
                SeriesData seriesData = chartData.seriesData(seriesName);
                if (seriesData == null) {
                    seriesData = new SeriesData(seriesName, columnConfig.getName(), columnDef.getAxisIndex());
                    chartData.seriesData(seriesName, seriesData);
                }

                if (seriesData.getGroup() != null && !seriesData.getGroup().equals(columnDef.getGroupName())) {
                    throw new WeChartsServiceException("同一系列的数据的分组必须相同");
                }

                if (seriesData.getType() != null && !seriesData.getType().equals(columnDef.getType().seriesType())) {
                    throw new WeChartsServiceException("同一系列的数据的系列类型必须相同");
                }

                seriesData.group(columnDef.getGroupName());
                seriesData.type(columnDef.getType().seriesType());
            }

        }
        return chartData;
    }

    /**
     * 纵向数据图表数据准备
     *
     * @throws Exception
     */
    protected ChartData analyseVerticalDataChart(PeChartDef chartDef) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, WeChartsServiceException {
        ChartData chart = this.initVerticalDataChartBasic(chartDef);
        Map<String, ColumnConfig> columnConfigs = buildMap(chartDef);
        Map<String, Object> params = this.getParamMap();
        String sql = this.operateSql(ScopeHandleUtils
                .handleScopeSignOfSql(chartDef.getChartSql(), UserUtils.getCurrentUserId()), params);
        List dataList = this.generalDao.getBySQLWithParameters(sql, params);
        if (CollectionUtils.isEmpty(dataList)) {
            throw new WeChartsServiceException("没有数据，无法生成图表");
        }

        if (CollectionUtils.isEmpty(chartDef.getColumnDefList())) {
            throw new WeChartsServiceException("没有定义图表，请实现initColumnDefine()方法");
        }

        for (PeChartColumnDef columnDef : chartDef.getColumnDefList()) {
            List<Object> values = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                Object bean = dataList.get(i);
                int index = columnDef.getColumnIndex();
                String col = columnDef.getColumnName();
                ColumnConfig columnConfig = columnConfigs.get(col);
                String dataIndex = columnConfig.getDataIndex();
                Object cellValue = getColumnValue(bean, new ColumnIndex(dataIndex, index, columnConfig));
                values.add(cellValue);
            }

            //数据列类型为gridName时，此数据列为坐标轴数据内容；否则为系列中的数据
            if (!columnDef.getType().equals(ChartColumnType.gridName)) {
                chart.seriesData(columnDef.getSeriesName(), columnDef.getType(), values);
            } else {
                chart.axisData(new AxisData(values, columnDef.getAxisIndex()));
            }

        }
        return chart;
    }

    //生成纵向图表 ------------- end -----------------------------

    /**
     * 生成显示列数据
     * @param chartDef
     * @return
     */
    private final Map buildMap(PeChartDef chartDef){
        Map<String, ColumnConfig> columnConfigs = new HashMap<>();
        for (PeChartColumnDef columnDef : chartDef.getColumnDefList()) {
            columnConfigs.put(columnDef.getColumnName(), new ColumnConfig(columnDef.getSeriesName(),
                    columnDef.getColumnName(), columnDef.getColumnIndex()));
        }
        return columnConfigs;
    }

    //生成横向图表 ------------- start -----------------------------

    /**
     * 解析数据list，处理成chart list
     *
     * @return
     * @throws
     */
    protected ChartData analyseRowList(PeChartDef chartDef) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, WeChartsServiceException {
        ChartData chart = this.chartMap(chartDef);
        Map<String, ColumnConfig> columnConfigs = buildMap(chartDef);
        Map<String, Object> params = this.getParamMap();
        String sql = this.operateSql(chartDef.getChartSql(), params);
        List dataList = this.generalDao.getBySQLWithParameters(sql, params);
        if (CollectionUtils.isEmpty(dataList) || MapUtils.isEmpty(columnConfigs)) {
            throw new WeChartsServiceException("没有数据，无法生成图表");
        }
        if (chartDef == null) {
            throw new WeChartsServiceException("没有定义图表，请实现initColumnDefine()方法");
        }

        List dataKeys = this.getDataKeys(chartDef, columnConfigs);
        //栏位类型为直角坐标系时，表格标题栏位坐标轴数据
        if (chartDef.getType().isGrid()) {
            chart.axisData(new AxisData(dataKeys, 0));
        }

        for (Object bean : dataList) {
            ColumnConfig columnConfig = columnConfigs.get(chartDef.getDataIndexColumn());
            String seriesName = this.getColumnValue(bean, new ColumnIndex(columnConfig.getDataIndex(),
                    columnConfig.getIndex(), columnConfig)).toString();

            List<Object> list = new ArrayList<>();

            String[] valueCol = chartDef.getValueColumnsStr().split(",");
            for(String key : valueCol){
                columnConfig = columnConfigs.get(key);
                Object cellValue = this.getColumnValue(bean, new ColumnIndex(columnConfig.getDataIndex(),
                        columnConfig.getIndex(), columnConfig));
                list.add(cellValue);
            }

            chart.seriesData(seriesName, chartDef.getType(), list).type(chartDef.getType().seriesType());

            //栏位类型为pieValue时，设置pieName
            if (ChartColumnType.pieValue.equals(chartDef.getType())) {
                chart.seriesData(seriesName, ChartColumnType.pieName, dataKeys);
            }
        }
        return chart;
    }

    /**
     * 处理数据列
     * @param chartDef
     * @param columnConfigs
     * @return
     */
    private List<String> getDataKeys(PeChartDef chartDef, Map<String, ColumnConfig> columnConfigs) {
        List<String> list = new ArrayList<>();

        String[] valueCol = chartDef.getValueColumnsStr().split(",");
        for(String key : valueCol){
            ColumnConfig columnConfig = columnConfigs.get(key);
            list.add(columnConfig.getName());
        }
        return list;
    }

    //生成横向图表 ------------- end -----------------------------

    public String getColumnValue(Object bean, ColumnIndex columnIndex) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        if (!bean.getClass().isArray()) {
            Method method;
            String subBeanName;
            Object value;
            String dataIndex = columnIndex.getDataIndex();
            /*
             * 一层一层递进 dataIndex 到最内层，获取最内层的 bean 及属性名
             */
            while (dataIndex.contains(".")) {
                subBeanName = dataIndex.substring(0, dataIndex.indexOf("."));
                subBeanName = subBeanName.substring(0, 1).toUpperCase() + subBeanName.substring(1);
                dataIndex = dataIndex.substring(dataIndex.indexOf(".") + 1);
                method = bean.getClass().getMethod("get" + subBeanName, new Class[]{null});
                bean = method.invoke(bean, new Object[]{null});
            }

            /*
             * 已获取最内层的 bean，及属性名，调用 get 方法，得到真实属性
             */
            subBeanName = dataIndex;
            if (bean instanceof HashMap) {
                /*
                 * 如果是HashMap(SQL查询方法经JsonUtil转换返回)，直接获取名为subBean的键值
                 */
                Class[] parameterTypes = new Class[1];
                parameterTypes[0] = Object.class;
                method = bean.getClass().getMethod("get", parameterTypes);
                value = method.invoke(bean, subBeanName);
            } else {
                /*
                 * 否则为bean对象，获取subBean子成员
                 */
                subBeanName = subBeanName.substring(0, 1).toUpperCase() + subBeanName.substring(1);
                method = bean.getClass().getMethod("get" + subBeanName, new Class[]{null});
                value = method.invoke(bean, new Object[]{null});
            }

            if (value != null) {
                if (value instanceof Date || value instanceof Timestamp || value instanceof java.sql.Date) {
                    SimpleDateFormat sf;
                    if (JsonUtil.getDateformat() == null) {
                        sf = new SimpleDateFormat("yyyy-MM-dd");
                    } else {
                        sf = new SimpleDateFormat(JsonUtil.getDateformat());
                    }
                    value = JavaScriptUtils.javaScriptEscape(sf.format(value));
                }
                return value.toString();
            }
        } else {
            Object[] beanArray = (Object[]) bean;
            String valueString = "";
            if (beanArray[columnIndex.getIndex()] instanceof String) {
                valueString = (String) beanArray[columnIndex.getIndex()];

            } else if (beanArray[columnIndex.getIndex()] instanceof Number) {
                valueString = beanArray[columnIndex.getIndex()].toString();
            } else if (beanArray[columnIndex.getIndex()] instanceof Date
                    || beanArray[columnIndex.getIndex()] instanceof Timestamp
                    || beanArray[columnIndex.getIndex()] instanceof java.sql.Date) {
                SimpleDateFormat sf;
                if (JsonUtil.getDateformat() == null) {
                    sf = new SimpleDateFormat("yyyy-MM-dd");
                } else {
                    sf = new SimpleDateFormat(JsonUtil.getDateformat());
                }
                valueString = sf.format(beanArray[columnIndex.getIndex()]);
            }
            ColumnConfig columnConfig = columnIndex.getColumnConfig();
            if (columnConfig.getComboList() != null) {
                valueString = columnConfig.getComboMap().get(valueString) == null ? valueString
                        : columnConfig.getComboMap().get(valueString).toString();
            }
            return valueString;
        }
        return null;
    }

    /**
     * 根据图表名称获取图表定义
     * 当图表定义不存在时自动创建该图表定义
     *
     * @param chart
     * @return
     */
    public PeChartDef chartDefMap(String chart) {
        PeChartDef chartDef = this.chartDefMap.get(chart);
        if (chartDef == null) {
            chartDef = new PeChartDef();
            chartDef.setChart(chart);
            this.chartDefMap.put(chart, chartDef);
        }
        return chartDef;
    }

    public ChartData chartMap(PeChartDef chartDef) {
        ChartData chartData = chartMap.get(chartDef.getChart());
        if (chartData == null) {
            chartData = new ChartData();

            chartData.setName(chartDef.getChart());
            chartData.setHasTimeLine(chartDef.isHasTimeline());
            chartData.setHasDataZoom(chartDef.isHasDataZoom());
            chartData.setDataZoomStart(chartDef.getDataZoomStart());
            chartData.setDataZoomEnd(chartDef.getDataZoomEnd());
            chartData.setZoomLock(chartDef.isZoomLock());
            chartData.setType(chartDef.getType());

            this.chartMap.put(chartDef.getChart(), chartData);
        }
        return chartData;
    }

    /**
     * 處理sql查詢
     * @param sql
     * @return
     */
    private String operateSql(String sql, Map<String, Object> params){
        String pattern = "\\[([\\w\\W][^\\[\\]]+)\\]";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(sql);
        System.out.println("m.groupCount() = " + m.groupCount());
        List<String> group = new ArrayList<String>();

        while (m.find()){
            boolean flag = true;
            System.out.println("m.group() = " + m.group());
            String g = m.group();

            for(String key : params.keySet()){
                if(g.contains(key)){
                    flag = false;
                }
            }

            if(flag){
                sql = sql.replace(g, "");
            } else {
                String tmp = g.replace('[',' ').replace(']', ' ');
                sql = sql.replace(g, tmp);
            }
        }
        return sql;
    }

    public Map<String, ChartData> getColumnChartMap() {
        return chartMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }
}
