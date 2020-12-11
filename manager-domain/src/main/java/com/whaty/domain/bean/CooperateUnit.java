package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.HasAttachFile;
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
 * 合作单位
 *
 * @author 索强强
 */
@Data
@Table(name = "cooperate_unit")
@Entity(name = "CooperateUnit")
public class CooperateUnit extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = -8974771591737585505L;

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
            name = "area"
    )
    private String area;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_area_id"
    )
    private PeArea peArea;
    @Column(
            name = "linkman"
    )
    private String linkman;
    @Column(
            name = "linkman_position"
    )
    private String linkmanPosition;
    @Column(
            name = "telephone"
    )
    private String telephone;
    @Column(
            name = "address"
    )
    private String address;
    @Column(
            name = "tax_number"
    )
    private String taxNumber;
    @Column(
            name = "cooperate_date"
    )
    private Date cooperateDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_cooperate_unit_type"
    )
    private EnumConst enumConstByFlagCooperateUnitType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_unit_id"
    )
    private PeUnit peUnit;
    @Column(
            name = "division_proportion"
    )
    private String divisionProportion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_province"
    )
    private EnumConst enumConstByFlagProvince;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
