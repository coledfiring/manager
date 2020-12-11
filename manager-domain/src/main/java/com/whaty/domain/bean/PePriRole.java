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
 * 角色表
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PePriRole")
@Table(name = "pe_pri_role")
public class PePriRole extends AbstractSiteBean {

    private static final long serialVersionUID = -501022145382582020L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "code"
    )
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_ROLE_TYPE"
    )
    private EnumConst enumConstByFlagRoleType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_BAK"
    )
    private EnumConst enumConstByFlagBak;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_SITE_SUPER_ADMIN"
    )
    private EnumConst enumConstByFlagSiteSuperAdmin;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
