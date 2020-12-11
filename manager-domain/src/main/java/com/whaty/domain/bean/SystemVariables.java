package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
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

/**
 * 系统常量表
 *
 * @author weipengsen
 */
@Data
@Entity(name = "SystemVariables")
@Table(name = "system_variables")
public class SystemVariables extends AbstractSiteBean {

    private static final long serialVersionUID = -64104742500624522L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_BAK"
    )
    private EnumConst enumConstByFlagBak;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_PLATFORM_SECTION"
    )
    private EnumConst enumConstByFlagPlatformSection;
    /**
     * 名称
     */
    @Column(
            name = "name"
    )
    private String name;
    /**
     * 内容
     */
    @Column(
            name = "value"
    )
    private String value;
    /**
     * 正则
     */
    @Column(
            name = "pattern"
    )
    private String pattern;
    /**
     * 备注
     */
    @Column(
            name = "note"
    )
    private String note;
    /**
     * team
     */
    @Column(
            name = "team"
    )
    private String team;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
