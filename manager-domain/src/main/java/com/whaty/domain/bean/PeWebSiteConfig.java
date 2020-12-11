package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.PeWebSite;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 站点配置类
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeWebSiteConfig")
@Table(name = "pe_web_site_config")
public class PeWebSiteConfig extends AbstractBean {

    private static final long serialVersionUID = 2550197041610754083L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "config")
    private String config;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_config_type")
    private EnumConst enumConstByFlagConfigType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_web_site_id")
    private PeWebSite peWebSite;

    @Column(name = "is_example")
    private String isExample;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    public PeWebSiteConfig() {
    }

    public PeWebSiteConfig(String config, EnumConst enumConstByFlagConfigType, PeWebSite peWebSite) {
        this.config = config;
        this.enumConstByFlagConfigType = enumConstByFlagConfigType;
        this.peWebSite = peWebSite;
        this.isExample = "0";
        this.createDate = new Date();
    }

}
