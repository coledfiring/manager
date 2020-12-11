package com.whaty.wecharts.bean;

import com.whaty.domain.bean.AbstractSiteBean;
import com.whaty.wecharts.chart.ChartColumnType;
import com.whaty.wecharts.chart.essh.DataDirection;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 框架图表定义
 *
 * @author 高升
 */
@Entity(
        name = "PeChartDef"
)
@Table(
        name = "pe_chart_def"
)
public class PeChartDef extends AbstractSiteBean {

    private static final long serialVersionUID = 6976943711406689178L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    /**
     * 图表名称
     */
    @Column(
            name = "chart"
    )
    private String chart;
    //-----------图表所属功能 start----------------------
    /**
     * 图表名称
     */
    @Column(
            name = "code"
    )
    private String code;
    /**
     * 数据方向
     * 纵向、横向
     */
    @Column(name = "data_direction")
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {
                    @Parameter(name = "enumClass", value = "com.whaty.wecharts.chart.essh.DataDirection"),
                    @Parameter(name = "type", value = "12")
            }
    )
    private DataDirection dataDirection;
    /**
     * 是否有时间轴
     */
    @Column(
            name = "has_time_line"
    )
    private boolean hasTimeline;
    //-----------图表所属功能 end----------------------

    //-----------数据缩放 start----------------------
    /**
     * 是否包含数据缩放轴
     */
    @Column(
            name = "has_data_zoom"
    )
    private boolean hasDataZoom;
    /**
     * 数据缩放，选择起始比例，默认为0（%），从首个数据起选择
     */
    @Column(
            name = "data_zoom_start"
    )
    private Integer dataZoomStart = 0;
    /**
     * 数据缩放，选择结束比例，默认为100（%），到最后一个数据选择结束
     */
    @Column(
            name = "data_zoom_end"
    )
    private Integer dataZoomEnd = 70;
    /**
     * 数据缩放锁，默认为false，当设置为true时选择区域不能伸缩，即(end - start)值保持不变，仅能做数据漫游
     */
    @Column(
            name = "zoom_lock"
    )
    private boolean zoomLock = false;
    //-----------数据缩放 end----------------------

    //定义横向图表使用 --------------------- start -------------------
    /**
     * 图表展示时的数据类型
     * 仅列表为横向图表时使用
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
     * 充当图例的数据列
     * 仅列表为横向图表时使用
     */
    @Column(
            name = "data_index_column"
    )
    private String dataIndexColumn;
    @Column(
            name = "value_columns_str"
    )
    private String valueColumnsStr;
    /**
     * 充当图表数据的数据列
     * 仅列表为横向图表时使用
     */
    @Transient
    private Integer[] valueColumns;

    //定义横向图表使用 --------------------- end -------------------
    /**
     * 图表的列定义
     */
    @Transient
    private List<PeChartColumnDef> columnDefList;
    @Column(
            name = "input_date"
    )
    private Date inputDate;
    @Column(
            name = "chart_sql"
    )
    private String chartSql;
    /**
     * 是否需要缓存
     */
    @Column(
            name = "is_cache"
    )
    private String isCache;
    @Column(
            name = "site_code"
    )
    private String siteCode;

    public PeChartDef() {
        this.hasTimeline = false;
        this.hasDataZoom = false;
        this.dataDirection = DataDirection.vertical;
        this.columnDefList = new ArrayList<>();
    }

    public PeChartDef(String id) {
        this.id = id;
    }

    public PeChartDef(String chart, DataDirection dataDirection,
                      boolean hasTimeline, boolean hasDataZoom, Integer dataZoomStart,
                      Integer dataZoomEnd, boolean zoomLock, ChartColumnType type,
                      String dataIndexColumn, Integer[] valueColumns,
                      List<PeChartColumnDef> columnDefList) {
        this();
        this.chart = chart;
        this.dataDirection = dataDirection;
        this.hasTimeline = hasTimeline;
        this.hasDataZoom = hasDataZoom;
        this.dataZoomStart = dataZoomStart;
        this.dataZoomEnd = dataZoomEnd;
        this.zoomLock = zoomLock;
        this.type = type;
        this.dataIndexColumn = dataIndexColumn;
        this.valueColumns = valueColumns;
        if (columnDefList != null) {
            this.columnDefList = columnDefList;
        }
    }

    public PeChartDef columnDefList(PeChartColumnDef... columnDefs) {
        if (columnDefs == null || columnDefs.length == 0) {
            return this;
        }
        this.columnDefList.addAll(Arrays.asList(columnDefs));
        return this;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public DataDirection getDataDirection() {
        return dataDirection;
    }

    public void setDataDirection(DataDirection dataDirection) {
        this.dataDirection = dataDirection;
    }

    public boolean isHasTimeline() {
        return hasTimeline;
    }

    public void setHasTimeline(boolean hasTimeline) {
        this.hasTimeline = hasTimeline;
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

    public String getDataIndexColumn() {
        return dataIndexColumn;
    }

    public void setDataIndexColumn(String dataIndexColumn) {
        this.dataIndexColumn = dataIndexColumn;
    }

    public Integer[] getValueColumns() {
        if (this.valueColumns == null && StringUtils.isNotBlank(this.valueColumnsStr)) {
            String[] columns = this.valueColumnsStr.split(",");
            this.valueColumns = new Integer[columns.length];
            for (int i = 0; i < columns.length; i++) {
                this.valueColumns[i] = Integer.parseInt(columns[i]);
            }
        }
        return valueColumns;
    }

    public void setValueColumns(Integer[] valueColumns) {
        this.valueColumns = valueColumns;
    }

    public List<PeChartColumnDef> getColumnDefList() {
        return columnDefList;
    }

    public void setColumnDefList(List<PeChartColumnDef> columnDefList) {
        this.columnDefList = columnDefList;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValueColumnsStr() {
        return valueColumnsStr;
    }

    public void setValueColumnsStr(String valueColumnsStr) {
        this.valueColumnsStr = valueColumnsStr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public String getChartSql() {
        return chartSql;
    }

    public void setChartSql(String chartSql) {
        this.chartSql = chartSql;
    }

    public String getIsCache() {
        return isCache;
    }

    public void setIsCache(String isCache) {
        this.isCache = isCache;
    }

    @Override
    public String getSiteCode() {
        return siteCode;
    }

    @Override
    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeChartDef that = (PeChartDef) o;
        return hasTimeline == that.hasTimeline &&
                hasDataZoom == that.hasDataZoom &&
                zoomLock == that.zoomLock &&
                Objects.equals(id, that.id) &&
                Objects.equals(chart, that.chart) &&
                Objects.equals(code, that.code) &&
                dataDirection == that.dataDirection &&
                Objects.equals(dataZoomStart, that.dataZoomStart) &&
                Objects.equals(dataZoomEnd, that.dataZoomEnd) &&
                type == that.type &&
                Objects.equals(dataIndexColumn, that.dataIndexColumn) &&
                Objects.equals(valueColumnsStr, that.valueColumnsStr) &&
                Arrays.equals(valueColumns, that.valueColumns) &&
                Objects.equals(columnDefList, that.columnDefList) &&
                Objects.equals(inputDate, that.inputDate) &&
                Objects.equals(chartSql, that.chartSql) &&
                Objects.equals(isCache, that.isCache) &&
                Objects.equals(siteCode, that.siteCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chart, code, dataDirection, hasTimeline, hasDataZoom, dataZoomStart, dataZoomEnd,
                zoomLock, type, dataIndexColumn, valueColumnsStr, valueColumns, columnDefList, inputDate,
                chartSql, isCache, siteCode);
    }

    @Override
    public String toString() {
        return "PeChartDef{" +
                "id='" + id + '\'' +
                ", chart='" + chart + '\'' +
                ", code='" + code + '\'' +
                ", dataDirection=" + dataDirection +
                ", hasTimeline=" + hasTimeline +
                ", hasDataZoom=" + hasDataZoom +
                ", dataZoomStart=" + dataZoomStart +
                ", dataZoomEnd=" + dataZoomEnd +
                ", zoomLock=" + zoomLock +
                ", type=" + type +
                ", dataIndexColumn='" + dataIndexColumn + '\'' +
                ", valueColumnsStr='" + valueColumnsStr + '\'' +
                ", valueColumns=" + Arrays.toString(valueColumns) +
                ", columnDefList=" + columnDefList +
                ", inputDate=" + inputDate +
                ", chartSql='" + chartSql + '\'' +
                ", isCache='" + isCache + '\'' +
                ", siteCode='" + siteCode + '\'' +
                '}';
    }
}
