package com.whaty.custom.bean;

import com.whaty.core.bean.AbstractBean;
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
 * 定制配置与url关联
 *
 * @author weipengsen
 */
@Entity(name = "SystemCustomConfigUrl")
@Table(name = "system_custom_config_url")
public class SystemCustomConfigUrl extends AbstractBean {

    private static final long serialVersionUID = -7030814526505236610L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_system_custom_id"
    )
    private SystemCustomConfig systemCustomConfig;
    @Column(
            name = "url"
    )
    private String url;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SystemCustomConfig getSystemCustomConfig() {
        return systemCustomConfig;
    }

    public void setSystemCustomConfig(SystemCustomConfig systemCustomConfig) {
        this.systemCustomConfig = systemCustomConfig;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemCustomConfigUrl that = (SystemCustomConfigUrl) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(systemCustomConfig, that.systemCustomConfig) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, systemCustomConfig, url);
    }

    @Override
    public String toString() {
        return "SystemCustomConfigUrl{" +
                "id='" + id + '\'' +
                ", systemCustomConfig=" + systemCustomConfig +
                ", url='" + url + '\'' +
                '}';
    }
}
