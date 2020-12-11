package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
 * 委托单位
 *
 * @author 索强强
 */
@Data
@Table(name = "entrusted_unit")
@Entity(name = "EntrustedUnit")
@NoArgsConstructor
@AllArgsConstructor
public class EntrustedUnit extends AbstractSiteBean {

    private static final long serialVersionUID = -7832858508056557325L;
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
    @Column(
            name = "address"
    )
    private String address;
    @Column(
            name = "tax_number"
    )
    private String taxNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_entrusted_unit_type"
    )
    private EnumConst enumConstByFlagEntrustedUnitType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_area_id"
    )
    private PeArea peArea;
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
