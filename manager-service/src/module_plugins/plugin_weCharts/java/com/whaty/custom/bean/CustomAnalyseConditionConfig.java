package com.whaty.custom.bean;

import com.whaty.custom.constant.AnalyseConditionEnum;
import com.whaty.core.bean.AbstractBean;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

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
 * 自定义统计条件配置
 * @author weipengsen
 */
@Entity(name = "CustomAnalyseConditionConfig"
)
@Table(
        name = "custom_analyse_condition_config"
)
public class CustomAnalyseConditionConfig extends AbstractBean {

    private static final long serialVersionUID = -654788887663772973L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private String id;
    /**
     * 名称
     */
    @Column(
            name = "name"
    )
    private String name;
    /**
     * 编号
     */
    @Column(
            name = "key"
    )
    private String key;
    /**
     * 条件类型
     */
    @Column(name = "type")
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {
                    @Parameter(name = "enumClass", value = "com.whaty.custom.constant.AnalyseConditionEnum"),
                    @Parameter(name = "type", value = "12")
            }
    )
    private AnalyseConditionEnum type;
    /**
     * 查询选项的sql，如果是文本类型则不需要
     */
    @Column(
            name = "option_sql"
    )
    private String optionSql;
    /**
     * 帮助文案
     */
    @Column(
            name = "helper"
    )
    private String helper;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public AnalyseConditionEnum getType() {
        return type;
    }

    public void setType(AnalyseConditionEnum type) {
        this.type = type;
    }

    public String getOptionSql() {
        return optionSql;
    }

    public void setOptionSql(String optionSql) {
        this.optionSql = optionSql;
    }

    public String getHelper() {
        return helper;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }

    public CustomAnalyseGroup getCustomAnalyseGroup() {
        return customAnalyseGroup;
    }

    public void setCustomAnalyseGroup(CustomAnalyseGroup customAnalyseGroup) {
        this.customAnalyseGroup = customAnalyseGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomAnalyseConditionConfig that = (CustomAnalyseConditionConfig) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(key, that.key) &&
                type == that.type &&
                Objects.equals(optionSql, that.optionSql) &&
                Objects.equals(helper, that.helper) &&
                Objects.equals(customAnalyseGroup, that.customAnalyseGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, key, type, optionSql, helper, customAnalyseGroup);
    }

    @Override
    public String toString() {
        return "CustomAnalyseConditionConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", type=" + type +
                ", optionSql='" + optionSql + '\'' +
                ", helper='" + helper + '\'' +
                ", customAnalyseGroup=" + customAnalyseGroup +
                '}';
    }
}
