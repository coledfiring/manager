package com.whaty.wecharts.chart.essh;

/**
 * 数据方向
 * 生成图表时表格中的数据组织形式，按照水平或垂直方向当成一组数据
 */
public enum DataDirection {

    //垂直
    vertical("垂直"),
    //水平
    horizontal("水平");

    private String title;

    DataDirection(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
