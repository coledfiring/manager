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
import java.util.Date;

/**
 * 培训场地
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PePlace")
@Table(name = "pe_place")
public class PePlace extends AbstractSiteBean {

    private static final long serialVersionUID = -1991351191841369057L;
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
            name = "capacity"
    )
    private String capacity;
    @Column(
            name = "charges"
    )
    private String charges;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_place_unit"
    )
    private PeUnit peUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_school_zone"
    )
    private EnumConst enumConstByFlagSchoolZone;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "update_by"
    )
    private SsoUser updateBy;
    @Column(
            name = "update_date"
    )
    private Date updateDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_is_valid"
    )
    private EnumConst enumConstByFlagIsValid;

}
