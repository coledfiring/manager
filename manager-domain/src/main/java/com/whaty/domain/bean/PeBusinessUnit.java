package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 单位管理
 *
 * @author shanshuai
 */
@Data
@Entity(name = "PeBusinessUnit")
@Table(name = "pe_business_unit")
public class PeBusinessUnit extends AbstractSiteBean {

    private static final long serialVersionUID = 1037426806708341759L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "name")
    private String name;

    @Fetch(FetchMode.JOIN)
    @OneToOne
    @JoinColumn(name = "fk_sso_user_id")
    private SsoUser ssoUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_unit_properties")
    private EnumConst enumConstByFlagUnitProperties;

    @Column(name = "unit_phone")
    private String unitPhone;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "unit_address")
    private String unitAddress;

    @Column(name = "card_no")
    private String cardNo;

    @Column(name = "note")
    private String note;

    @Column(name = "site_code")
    private String siteCode;

}
