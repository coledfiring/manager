package com.whaty.wecharts.chart;

import com.whaty.wecharts.jsonbean.code.SeriesType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系列数据
 *
 * @author 高升
 */
public class SeriesData {
    /**
     * 系列名称
     * 表格数据加入系列中的定位依据
     */
    private String name;
    /**
     * 系列类型
     */
    private SeriesType type;
    /**
     * 数据--数据类型为key
     */
    private Map<ChartColumnType, List<Object>> data;

    /**
     * 坐标轴索引
     */
    private Integer axisIndex;

    /**
     * 用于timeline带有时分类
     */
    private String group;

    public SeriesData(String name) {
        this.name = name;
        this.data = new HashMap<>();
    }

    public SeriesData(String name, String dataIndex, Integer axisIndex) {
        this.name = name;
        this.data = new HashMap<>();
        this.axisIndex = axisIndex;
    }

    public SeriesType type() {
        return this.type;
    }

    public SeriesData type(SeriesType seriesType) {
        this.type = seriesType;
        return this;
    }

    public Map<ChartColumnType, List<Object>> data() {
        return data;
    }

    public SeriesData data(ChartColumnType chartColumnType, List<Object> data) {
        this.data.put(chartColumnType, data);
        return this;
    }

    public SeriesData group(String group) {
        this.group = group;
        return this;
    }

    public String group() {
        return this.group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SeriesType getType() {
        return type;
    }

    public void setType(SeriesType type) {
        this.type(type);
    }

    public Map<ChartColumnType, List<Object>> getData() {
        return data;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getAxisIndex() {
        return axisIndex;
    }

    public void setAxisIndex(Integer axisIndex) {
        this.axisIndex = axisIndex;
    }

}
