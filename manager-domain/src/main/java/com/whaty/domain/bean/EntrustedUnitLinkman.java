package com.whaty.domain.bean;

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
 * 委托单位联系人
 *
 * @author 索强强
 */
@Data
@Table(name = "entrusted_unit_linkman")
@Entity(name = "EntrustedUnitLinkman")
@NoArgsConstructor
@AllArgsConstructor
public class EntrustedUnitLinkman extends AbstractSiteBean {

    private static final long serialVersionUID = 7750528749170921342L;
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
            name = "telephone"
    )
    private String telephone;
    @Column(
            name = "mobile_number"
    )
    private String mobileNumber;
    @Column(
            name = "job"
    )
    private String job;
    @Column(
            name = "contacter"
    )
    private String contacter;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_unit_id"
    )
    private EntrustedUnit entrustedUnit;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
