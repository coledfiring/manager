package com.whaty.custom.bean;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Objects;

/**
 * 自定义分析图表组实体类
 * @author weipengsen
 */
@Entity(name = "CustomAnalyseGroup"
)
@Table(
        name = "custom_analyse_group"
)
public class CustomAnalyseGroup extends AbstractSiteBean {

    private static final long serialVersionUID = 1473437764670272597L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private String id;
    /**
     * 组名称
     */
    @Column(
            name = "name"
    )
    private String name;
    /**
     * 编号
     */
    @Column(
            name = "code"
    )
    private String code;
    /**
     * grid配置sql
     */
    @Column(
            name = "grid_sql"
    )
    private String gridSql;
    /**
     * charts配置sql
     */
    @Column(
            name = "charts_sql"
    )
    private String chartsSql;
    /**
     * 查询维度的sql
     */
    @Column(
            name = "column_sql"
    )
    private String columnSql;
    /**
     * 是否有效
     */
    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    @Column(
            name = "siteCode"
    )
    protected String siteCode;
    /**
     * 所有自定义图表
     */
    @Transient
    private List<CustomAnalyse> customAnalysesList;
    /**
     * 维度配置
     */
    @Transient
    private List<CustomAnalyseXGroupConfig> customAnalyseXGroupConfigList;
    /**
     * 统计项配置
     */
    @Transient
    private List<CustomAnalyseItemConfig> customAnalyseItemConfigList;
    /**
     * 条件配置
     */
    @Transient
    private List<CustomAnalyseConditionConfig> customAnalyseConditionConfigList;

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getSiteCode() {
        return siteCode;
    }

    @Override
    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EnumConst getEnumConstByFlagActive() {
        return enumConstByFlagActive;
    }

    public void setEnumConstByFlagActive(EnumConst enumConstByFlagActive) {
        this.enumConstByFlagActive = enumConstByFlagActive;
    }

    public String getGridSql() {
        return gridSql;
    }

    public void setGridSql(String gridSql) {
        this.gridSql = gridSql;
    }

    public String getChartsSql() {
        return chartsSql;
    }

    public void setChartsSql(String chartsSql) {
        this.chartsSql = chartsSql;
    }

    public String getColumnSql() {
        return columnSql;
    }

    public void setColumnSql(String columnSql) {
        this.columnSql = columnSql;
    }

    public List<CustomAnalyse> getCustomAnalysesList() {
        return customAnalysesList;
    }

    public void setCustomAnalysesList(List<CustomAnalyse> customAnalysesList) {
        this.customAnalysesList = customAnalysesList;
    }

    public List<CustomAnalyseXGroupConfig> getCustomAnalyseXGroupConfigList() {
        return customAnalyseXGroupConfigList;
    }

    public void setCustomAnalyseXGroupConfigList(List<CustomAnalyseXGroupConfig> customAnalyseXGroupConfigList) {
        this.customAnalyseXGroupConfigList = customAnalyseXGroupConfigList;
    }

    public List<CustomAnalyseItemConfig> getCustomAnalyseItemConfigList() {
        return customAnalyseItemConfigList;
    }

    public void setCustomAnalyseItemConfigList(List<CustomAnalyseItemConfig> customAnalyseItemConfigList) {
        this.customAnalyseItemConfigList = customAnalyseItemConfigList;
    }

    public List<CustomAnalyseConditionConfig> getCustomAnalyseConditionConfigList() {
        return customAnalyseConditionConfigList;
    }

    public void setCustomAnalyseConditionConfigList(List<CustomAnalyseConditionConfig> customAnalyseConditionConfigList) {
        this.customAnalyseConditionConfigList = customAnalyseConditionConfigList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomAnalyseGroup that = (CustomAnalyseGroup) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(gridSql, that.gridSql) &&
                Objects.equals(chartsSql, that.chartsSql) &&
                Objects.equals(columnSql, that.columnSql) &&
                Objects.equals(enumConstByFlagActive, that.enumConstByFlagActive) &&
                Objects.equals(customAnalysesList, that.customAnalysesList) &&
                Objects.equals(customAnalyseXGroupConfigList, that.customAnalyseXGroupConfigList) &&
                Objects.equals(customAnalyseItemConfigList, that.customAnalyseItemConfigList) &&
                Objects.equals(customAnalyseConditionConfigList, that.customAnalyseConditionConfigList) &&
                Objects.equals(siteCode, that.siteCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, gridSql, chartsSql, columnSql, enumConstByFlagActive,
                customAnalysesList, customAnalyseXGroupConfigList, customAnalyseItemConfigList,
                customAnalyseConditionConfigList, siteCode);
    }

    @Override
    public String toString() {
        return "CustomAnalyseGroup{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", gridSql='" + gridSql + '\'' +
                ", chartsSql='" + chartsSql + '\'' +
                ", columnSql='" + columnSql + '\'' +
                ", enumConstByFlagActive=" + enumConstByFlagActive +
                ", customAnalysesList=" + customAnalysesList +
                ", customAnalyseXGroupConfigList=" + customAnalyseXGroupConfigList +
                ", customAnalyseItemConfigList=" + customAnalyseItemConfigList +
                ", customAnalyseConditionConfigList=" + customAnalyseConditionConfigList +
                ", siteCode='" + siteCode + '\'' +
                '}';
    }
}
