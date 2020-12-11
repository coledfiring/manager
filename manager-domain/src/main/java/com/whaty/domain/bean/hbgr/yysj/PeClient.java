package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * author weipengsen  Date 2020/6/19
 */
@Data
@Entity(name = "PeClient")
@Table(name = "pe_client")
public class PeClient extends AbstractBean {

    private static final long serialVersionUID = -4748721384993964605L;
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
            name = "phone"
    )
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_gender"
    )
    private EnumConst enumConstByFlagGender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;

    @Column(
            name = "legal_person"
    )
    private String legalPerson;

    @Column(
            name = "company_name"
    )
    private String companyName;

    @Column(
            name = "business"
    )
    private String business;

    @Column(
            name = "position"
    )
    private String position;

    @Column(
            name = "company_address"
    )
    private String companyAddress;

    @Column(
            name = "heating_area"
    )
    private String heatingArea;

    @Column(
            name = "home_type"
    )
    private String homeType;

    @Column(
            name = "floor"
    )
    private String floor;

    @Column(
            name = "url"
    )
    private String url;
}

