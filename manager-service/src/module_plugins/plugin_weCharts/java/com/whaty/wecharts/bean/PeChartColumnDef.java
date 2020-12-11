package com.whaty.wecharts.bean;


import com.whaty.core.bean.AbstractBean;
import com.whaty.wecharts.chart.ChartColumnType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

/**
 * 框架列表纵向图表列定义
 * @author yinxu
 */
@Entity(
        name = "PeChartColumnDef"
)
@Table(
        name = "pe_chart_column_def"
)
public class PeChartColumnDef extends AbstractBean {

    private static final long serialVersionUID = -1488182286685758843L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_chart_def_id"
    )
    private PeChartDef peChartDef;
    /**
     * 栏位索引essh表格中列(行)的索引
     */
    @Column(
            name = "column_index"
    )
    private int columnIndex;
    @Column(
            name = "column_name"
    )
    private String columnName;
    /**
     * 栏位类型
     */
    @Column(name = "type")
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {
                    @Parameter(name = "enumClass", value = "com.whaty.wecharts.chart.ChartColumnType"),
                    @Parameter(name = "type", value = "12")
            }
    )
    private ChartColumnType type;
    /**
     * 栏位对应的图表系列名称
     * 可以为数字、字符串
     * 用于区分属于同一系列的栏位
     * 可为空
     */
    @Column(
            name = "series_name"
    )
    private String seriesName;
    /**
     * 图形组
     * 用于带timeline的图表中图形归类
     * 可为空
     */
    @Column(
            name = "group_name"
    )
    private String groupName;
    /**
     * 坐标轴索引
     */
    @Column(
            name = "axis_index"
    )
    private Integer axisIndex;
    @Column(
            name = "input_date"
    )
    private Date inputDate;

    public PeChartColumnDef() {
    }

    public PeChartColumnDef(int index, PeChartDef chartDef, ChartColumnType type) {
        this(index, chartDef, type, "default", null, 0);
    }

    public PeChartColumnDef(int index, PeChartDef chartDef, ChartColumnType type, String seriesId) {
        this(index, chartDef, type, seriesId, null, 0);
    }

    public PeChartColumnDef(int index, PeChartDef chartDef, ChartColumnType type, String seriesId, String group) {
        this(index, chartDef, type, seriesId, group, 0);
    }

    public PeChartColumnDef(int index, PeChartDef chartDef, ChartColumnType type, String seriesId, int axisIndex) {
        this(index, chartDef, type, seriesId, null, axisIndex);
    }

    public PeChartColumnDef(int index, PeChartDef chartDef, ChartColumnType type, String seriesName, String group, int axisIndex) {
        this.columnIndex = index;
        this.peChartDef = chartDef;
        this.type = type;
        this.seriesName = seriesName;
        this.groupName = group;
        this.axisIndex = axisIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PeChartDef getPeChartDef() {
        return peChartDef;
    }

    public void setPeChartDef(PeChartDef chartDef) {
        this.peChartDef = chartDef;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int index) {
        this.columnIndex = index;
    }

    public ChartColumnType getType() {
        return type;
    }

    public void setType(ChartColumnType type) {
        this.type = type;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String group) {
        this.groupName = group;
    }

    public Integer getAxisIndex() {
        return axisIndex;
    }

    public void setAxisIndex(Integer axisIndex) {
        this.axisIndex = axisIndex;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeChartColumnDef that = (PeChartColumnDef) o;
        return columnIndex == that.columnIndex &&
                Objects.equals(id, that.id) &&
                Objects.equals(peChartDef, that.peChartDef) &&
                Objects.equals(columnName, that.columnName) &&
                type == that.type &&
                Objects.equals(seriesName, that.seriesName) &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(axisIndex, that.axisIndex) &&
                Objects.equals(inputDate, that.inputDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peChartDef, columnIndex, columnName, type, seriesName, groupName, axisIndex, inputDate);
    }

    @Override
    public String toString() {
        return "PeChartColumnDef{" +
                "id='" + id + '\'' +
                ", columnIndex=" + columnIndex +
                ", columnName='" + columnName + '\'' +
                ", type=" + type +
                ", seriesName='" + seriesName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", axisIndex=" + axisIndex +
                ", inputDate=" + inputDate +
                '}';
    }
}
