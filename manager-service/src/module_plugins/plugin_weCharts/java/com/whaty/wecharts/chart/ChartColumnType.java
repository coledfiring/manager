package com.whaty.wecharts.chart;

import com.whaty.wecharts.jsonbean.code.SeriesType;

/**
 * 数据栏位类型
 *
 * @author 高升
 */
public enum ChartColumnType {
    //直角系类目轴
    gridName(null, false),
    //折线图数据
    lineValue(SeriesType.line, true),
    //堆叠条形图
    stackValue(SeriesType.stack, true),
    //柱状图数据
    barValue(SeriesType.bar, true),
    //饼图数据名称
    pieName(SeriesType.pie, false),
    //饼图数据
    pieValue(SeriesType.pie, false);

    /**
     * 栏位对应的图表系列
     */
    private SeriesType seriesType;

    /**
     * 栏位是否为直角坐标系数据
     */
    private boolean isGrid;

    private ChartColumnType(SeriesType seriesType, boolean isGrid) {
        this.seriesType = seriesType;
        this.isGrid = isGrid;
    }

    public SeriesType seriesType() {
        return this.seriesType;
    }

    public boolean isGrid() {
        return this.isGrid;
    }
}

