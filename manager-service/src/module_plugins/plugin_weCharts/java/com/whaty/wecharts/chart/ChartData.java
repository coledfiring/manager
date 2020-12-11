package com.whaty.wecharts.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartData {

    private ChartColumnType type;

    private String name;

    private boolean hasTimeLine;
    //-----------数据缩放 start----------------------
    /**
     * 是否包含数据缩放轴
     */
    public boolean hasDataZoom;

    /**
     * 数据缩放，选择起始比例，默认为0（%），从首个数据起选择
     */
    public Integer dataZoomStart = 0;

    /**
     * 数据缩放，选择结束比例，默认为100（%），到最后一个数据选择结束
     */
    public Integer dataZoomEnd = 70;

    /**
     * 数据缩放锁，默认为false，当设置为true时选择区域不能伸缩，即(end - start)值保持不变，仅能做数据漫游
     */
    public boolean zoomLock = false;
    //-----------数据缩放 end----------------------

    /**
     * 直角坐标系中坐标轴数据数组，数组中每一项代表一条坐标轴，最多同时存在2条横轴或纵轴
     * 2条要么都是横轴，要么都是纵轴
     */
    private List<AxisData> axisData;

    /**
     * 驱动图表生成的数据内容，数组中每一项代表一个系列的数据
     */
    private Map<String, SeriesData> seriesDataMap;
    /**
     * 存放SeriesData顺序
     */
    private List<SeriesData> seriesDatas;

    public ChartData() {
        this.axisData = new ArrayList<>();
        this.seriesDataMap = new HashMap<>();
        this.seriesDatas = new ArrayList<>();
    }

    /**
     * 设置坐标轴数据
     *
     * @return
     */
    public ChartData axisData(List<AxisData> axisData) {
        this.axisData = axisData;
        return this;
    }

    /**
     * 生成图表坐标轴的数据内容，数组中每一项代表一个坐标轴的数据
     *
     * @return
     */
    public List<AxisData> axisData() {
        return this.axisData;
    }

    /**
     * 添加坐标轴数据
     *
     * @param axisData
     * @return
     */
    public ChartData axisData(AxisData... axisData) {
        if (axisData == null || axisData.length == 0) {
            return this;
        }
        this.axisData().addAll(Arrays.asList(axisData));
        return this;
    }

    public ChartData seriesData(String seriesName, SeriesData seriesData) {
        SeriesData s = this.seriesDataMap.get(seriesName);
        if (s == null) {
            this.seriesDatas.add(seriesData);
        }
        this.seriesDataMap.put(seriesName, seriesData);
        return this;
    }

    public SeriesData seriesData(String seriesName) {
        SeriesData seriesData = this.seriesDataMap.get(seriesName);
        if (seriesData == null) {
            seriesData = new SeriesData(seriesName);
            this.seriesDataMap.put(seriesName, seriesData);
            this.seriesDatas.add(seriesData);
        }
        return seriesData;
    }

    public SeriesData seriesData(String seriesId, ChartColumnType chartColumnType, List<Object> data) {
        return this.seriesData(seriesId).data(chartColumnType, data);
    }

    public SeriesData[] seriesData() {
        return this.seriesDatas.toArray(new SeriesData[]{});
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasTimeLine() {
        return hasTimeLine;
    }

    public void setHasTimeLine(boolean hasTimeLine) {
        this.hasTimeLine = hasTimeLine;
    }

    public List<AxisData> getAxisData() {
        return axisData;
    }

    public void setAxisData(List<AxisData> axisData) {
        this.axisData = axisData;
    }

    public Map<String, SeriesData> getSeriesDataMap() {
        return seriesDataMap;
    }

    public boolean isHasDataZoom() {
        return hasDataZoom;
    }

    public void setHasDataZoom(boolean hasDataZoom) {
        this.hasDataZoom = hasDataZoom;
    }

    public Integer getDataZoomStart() {
        return dataZoomStart;
    }

    public void setDataZoomStart(Integer dataZoomStart) {
        this.dataZoomStart = dataZoomStart;
    }

    public Integer getDataZoomEnd() {
        return dataZoomEnd;
    }

    public void setDataZoomEnd(Integer dataZoomEnd) {
        this.dataZoomEnd = dataZoomEnd;
    }

    public boolean isZoomLock() {
        return zoomLock;
    }

    public void setZoomLock(boolean zoomLock) {
        this.zoomLock = zoomLock;
    }

    public ChartColumnType getType() {
        return type;
    }

    public void setType(ChartColumnType type) {
        this.type = type;
    }
}
