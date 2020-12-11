package com.whaty.custom.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.PeWebSite;
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
 * 定制配置站点关联
 *
 * @author weipengsen
 */
@Entity(name = "SystemSiteCustomConfig")
@Table(name = "system_site_custom_config")
public class SystemSiteCustomConfig extends AbstractBean {

    private static final long serialVersionUID = 5728500135562430669L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_web_site_id"
    )
    private PeWebSite peWebSite;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_system_custom_id"
    )
    private SystemCustomConfig systemCustomConfig;
    @Column(
            name = "value"
    )
    private String value;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PeWebSite getPeWebSite() {
        return peWebSite;
    }

    public void setPeWebSite(PeWebSite peWebSite) {
        this.peWebSite = peWebSite;
    }

    public SystemCustomConfig getSystemCustomConfig() {
        return systemCustomConfig;
    }

    public void setSystemCustomConfig(SystemCustomConfig systemCustomConfig) {
        this.systemCustomConfig = systemCustomConfig;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemSiteCustomConfig that = (SystemSiteCustomConfig) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(peWebSite, that.peWebSite) &&
                Objects.equals(systemCustomConfig, that.systemCustomConfig) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peWebSite, systemCustomConfig, value);
    }

    @Override
    public String toString() {
        return "SystemSiteCustomConfig{" +
                "id='" + id + '\'' +
                ", peWebSite=" + peWebSite +
                ", systemCustomConfig=" + systemCustomConfig +
                ", value='" + value + '\'' +
                '}';
    }
}
