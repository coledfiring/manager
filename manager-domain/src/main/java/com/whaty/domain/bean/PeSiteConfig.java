package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 工作室配置
 * @author weipengsen
 */
@Data
@Entity(name = "PeSiteConfig")
@Table(name = "pe_site_config")
public class PeSiteConfig extends AbstractBean {

    private static final long serialVersionUID = -7612438194008137746L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "identity"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    /**
     * 配置项编码
     */
    @Column(
            name = "CODE"
    )
    private String code;
    /**
     * 配置项名称
     */
    @Column(
            name = "name"
    )
    private String name;
    /**
     * 配置项备注
     */
    @Column(
            name = "note"
    )
    private String note;
    /**
     * json配置
     */
    @Column(
            name = "json_config"
    )
    private String jsonConfig;
    /**
     * 站点编号
     */
    @Column(
            name = "site_code"
    )
    private String siteCode;
    /**
     * 客服js链接
     */
    @Column(
            name = "service_script_url"
    )
    private String serviceScriptUrl;

}
