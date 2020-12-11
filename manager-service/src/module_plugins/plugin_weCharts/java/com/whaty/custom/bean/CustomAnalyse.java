package com.whaty.custom.bean;

import com.whaty.domain.bean.SsoUser;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.GridBasicConfig;
import com.whaty.wecharts.bean.PeChartDef;
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
import java.util.Objects;

/**
 * 自定义分析图表实体类
 * @author weipengsen
 */
@Entity(name = "CustomAnalyse"
)
@Table(
        name = "custom_analyse"
)
public class CustomAnalyse extends AbstractBean {

    private static final long serialVersionUID = -3126211019113950362L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private String id;
    /**
     * 图表名称
     */
    @Column(
            name = "name"
    )
    private String name;
    /**
     * 自定义统计配置
     */
    @Column(
            name = "custom_analyse_config"
    )
    private String customAnalyseConfig;
    /**
     * 图表Id
     */
    @Column(
            name = "fk_chart_def_id"
    )
    private String peChartDefId;
    /**
     * gridId
     */
    @Column(
            name = "fk_grid_id"
    )
    private String gridBasicConfigId;
    /**
     * 图表
     */
    @Transient
    private PeChartDef peChartDef;
    /**
     * grid
     */
    @Transient
    private GridBasicConfig gridBasicConfig;
    /**
     * 是否有效
     */
    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    /**
     * 创建者
     */
    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_create_user_id"
    )
    private SsoUser createUser;
    /**
     * 可见此统计的角色code拼接
     */
    @Column(
            name = "can_view_role_code"
    )
    private String canViewRoleCode;
    /**
     * 可见级别
     */
    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_analyse_view_level"
    )
    private EnumConst enumConstByFlagAnalyseViewLevel;
    /**
     * 所属自定义图表组
     */
    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_custom_analyse_group_id"
    )
    private CustomAnalyseGroup customAnalyseGroup;

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PeChartDef getPeChartDef() {
        return peChartDef;
    }

    public void setPeChartDef(PeChartDef peChartDef) {
        this.peChartDef = peChartDef;
    }

    public GridBasicConfig getGridBasicConfig() {
        return gridBasicConfig;
    }

    public void setGridBasicConfig(GridBasicConfig gridBasicConfig) {
        this.gridBasicConfig = gridBasicConfig;
    }

    public EnumConst getEnumConstByFlagActive() {
        return enumConstByFlagActive;
    }

    public void setEnumConstByFlagActive(EnumConst enumConstByFlagActive) {
        this.enumConstByFlagActive = enumConstByFlagActive;
    }

    public CustomAnalyseGroup getCustomAnalyseGroup() {
        return customAnalyseGroup;
    }

    public void setCustomAnalyseGroup(CustomAnalyseGroup customAnalyseGroup) {
        this.customAnalyseGroup = customAnalyseGroup;
    }

    public String getPeChartDefId() {
        return peChartDefId;
    }

    public void setPeChartDefId(String peChartDefId) {
        this.peChartDefId = peChartDefId;
    }

    public String getGridBasicConfigId() {
        return gridBasicConfigId;
    }

    public void setGridBasicConfigId(String gridBasicConfigId) {
        this.gridBasicConfigId = gridBasicConfigId;
    }

    public SsoUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(SsoUser createUser) {
        this.createUser = createUser;
    }

    public String getCanViewRoleCode() {
        return canViewRoleCode;
    }

    public void setCanViewRoleCode(String canViewRoleCode) {
        this.canViewRoleCode = canViewRoleCode;
    }

    public EnumConst getEnumConstByFlagAnalyseViewLevel() {
        return enumConstByFlagAnalyseViewLevel;
    }

    public void setEnumConstByFlagAnalyseViewLevel(EnumConst enumConstByFlagAnalyseViewLevel) {
        this.enumConstByFlagAnalyseViewLevel = enumConstByFlagAnalyseViewLevel;
    }

    public String getCustomAnalyseConfig() {
        return customAnalyseConfig;
    }

    public void setCustomAnalyseConfig(String customAnalyseConfig) {
        this.customAnalyseConfig = customAnalyseConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomAnalyse that = (CustomAnalyse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(customAnalyseConfig, that.customAnalyseConfig) &&
                Objects.equals(peChartDefId, that.peChartDefId) &&
                Objects.equals(gridBasicConfigId, that.gridBasicConfigId) &&
                Objects.equals(peChartDef, that.peChartDef) &&
                Objects.equals(gridBasicConfig, that.gridBasicConfig) &&
                Objects.equals(enumConstByFlagActive, that.enumConstByFlagActive) &&
                Objects.equals(createUser, that.createUser) &&
                Objects.equals(canViewRoleCode, that.canViewRoleCode) &&
                Objects.equals(enumConstByFlagAnalyseViewLevel, that.enumConstByFlagAnalyseViewLevel) &&
                Objects.equals(customAnalyseGroup, that.customAnalyseGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, customAnalyseConfig, peChartDefId, gridBasicConfigId, peChartDef,
                gridBasicConfig, enumConstByFlagActive, createUser, canViewRoleCode, enumConstByFlagAnalyseViewLevel,
                customAnalyseGroup);
    }

    @Override
    public String toString() {
        return "CustomAnalyse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", customAnalyseConfig='" + customAnalyseConfig + '\'' +
                ", peChartDefId='" + peChartDefId + '\'' +
                ", gridBasicConfigId='" + gridBasicConfigId + '\'' +
                ", peChartDef=" + peChartDef +
                ", gridBasicConfig=" + gridBasicConfig +
                ", enumConstByFlagActive=" + enumConstByFlagActive +
                ", createUser=" + createUser +
                ", canViewRoleCode='" + canViewRoleCode + '\'' +
                ", enumConstByFlagAnalyseViewLevel=" + enumConstByFlagAnalyseViewLevel +
                ", customAnalyseGroup=" + customAnalyseGroup +
                '}';
    }
}
