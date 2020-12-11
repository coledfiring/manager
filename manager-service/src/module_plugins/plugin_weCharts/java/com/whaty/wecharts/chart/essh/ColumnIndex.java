package com.whaty.wecharts.chart.essh;


/**
 * 框架列索引
 * 方便列定位
 *
 * @author 高升
 */
public class ColumnIndex {

    private String dataIndex;

    private int index;

    private ColumnConfig columnConfig;

    public ColumnIndex(String dataIndex, int index) {
        super();
        this.dataIndex = dataIndex;
        this.index = index;
    }

    public ColumnIndex(String dataIndex, int index, ColumnConfig columnConfig) {
        super();
        this.dataIndex = dataIndex;
        this.index = index;
        this.columnConfig = columnConfig;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public int getIndex() {
        return index;
    }

    public ColumnConfig getColumnConfig() {
        return columnConfig;
    }

    public void setColumnConfig(ColumnConfig columnConfig) {
        this.columnConfig = columnConfig;
    }

}
