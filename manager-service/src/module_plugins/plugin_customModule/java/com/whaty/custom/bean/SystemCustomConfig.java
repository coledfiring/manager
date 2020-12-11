package com.whaty.custom.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.PeBaseCategory;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 定制配置
 *
 * @author weipengsen
 */
@Entity(name = "SystemCustomConfig")
@Table(name = "system_custom_config")
public class SystemCustomConfig extends AbstractBean {

    private static final long serialVersionUID = 5643179222600763391L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "code"
    )
    private String code;
    @Column(
            name = "type"
    )
    private String type;
    @Column(
            name = "default_value"
    )
    private String defaultValue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_base_category_id"
    )
    private PeBaseCategory peBaseCategory;
    @Column(
            name = "options"
    )
    private String options;
    @Column(
            name = "note"
    )
    private String note;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public PeBaseCategory getPeBaseCategory() {
        return peBaseCategory;
    }

    public void setPeBaseCategory(PeBaseCategory peBaseCategory) {
        this.peBaseCategory = peBaseCategory;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemCustomConfig that = (SystemCustomConfig) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(type, that.type) &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(peBaseCategory, that.peBaseCategory) &&
                Objects.equals(options, that.options) &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, type, defaultValue, peBaseCategory, options, note);
    }

    @Override
    public String toString() {
        return "SystemCustomConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", peBaseCategory=" + peBaseCategory +
                ", options='" + options + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
