package com.whaty.custom.bean;

import com.whaty.core.bean.AbstractBean;
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
import java.util.Objects;

/**
 * 自定义组的统计项配置
 * @author weipengsen
 */
@Entity(name = "CustomAnalyseItemConfig"
)
@Table(
        name = "custom_analyse_item_config"
)
public class CustomAnalyseItemConfig extends AbstractBean {

    private static final long serialVersionUID = -7401102525654127362L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private String id;
    /**
     * 统计项名称
     */
    @Column(
            name = "name"
    )
    private String name;
    /**
     * 统计项值
     */
    @Column(
            name = "code"
    )
    private String code;
    /**
     * 项的数据索引
     */
    @Column(
            name = "data_index"
    )
    private String dataIndex;
    /**
     * 自定义统计组
     */
    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_custom_analyse_group_id"
    )
    private CustomAnalyseGroup customAnalyseGroup;

    @Override
    public String getId() {
        return id;
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

    public CustomAnalyseGroup getCustomAnalyseGroup() {
        return customAnalyseGroup;
    }

    public void setCustomAnalyseGroup(CustomAnalyseGroup customAnalyseGroup) {
        this.customAnalyseGroup = customAnalyseGroup;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    @Override
    public String toString() {
        return "CustomAnalyseItemConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", dataIndex='" + dataIndex + '\'' +
                ", customAnalyseGroup=" + customAnalyseGroup +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomAnalyseItemConfig that = (CustomAnalyseItemConfig) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(dataIndex, that.dataIndex) &&
                Objects.equals(customAnalyseGroup, that.customAnalyseGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, dataIndex, customAnalyseGroup);
    }

}
