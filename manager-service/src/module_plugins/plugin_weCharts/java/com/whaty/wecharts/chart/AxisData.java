package com.whaty.wecharts.chart;

import java.util.List;

public class AxisData {
    /**
     * 坐标轴数据
     */
    private List<Object> data;
    /**
     * 坐标轴序号--定义表格时指定
     */
    private int index;

    public AxisData(List<Object> data, int index) {
        super();
        this.data = data;
        this.index = index;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
