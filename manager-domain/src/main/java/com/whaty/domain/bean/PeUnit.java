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
 * 单位
 *
 * @author weipengsen
 */
@Data
@Table(name = "pe_unit")
@Entity(name = "PeUnit")
public class PeUnit extends AbstractSiteBean {

    private static final long serialVersionUID = 6862061215640407183L;
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
    @Column(
            name = "head_name"
    )
    private String headName;
    @Column(
            name = "head_mobile"
    )
    private String headMobile;
    @Column(
            name = "head_telephone"
    )
    private String headTelephone;
    @Column(
            name = "head_email"
    )
    private String headEmail;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_unit_type"
    )
    private EnumConst enumConstByFlagUnitType;
    @Column(
            name = "note"
    )
    private String note;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_parent_id"
    )
    private PeUnit parent;

}
