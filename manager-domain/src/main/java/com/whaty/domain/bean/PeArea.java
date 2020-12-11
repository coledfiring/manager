package com.whaty.domain.bean;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 地区
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeArea")
@Table(name = "pe_area")
public class PeArea extends AbstractSiteBean {

    private static final long serialVersionUID = -6985925363699139052L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "identity"
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
            name = "province"
    )
    private String province;
    @Column(
            name = "city"
    )
    private String city;
    @Column(
            name = "county"
    )
    private String county;
    @Column(
            name = "zip_code"
    )
    private String zipCode;
    @Column(
            name = "area_code"
    )
    private String areaCode;
    @ManyToOne
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @ManyToOne
    @JoinColumn(
            name = "update_by"
    )
    private SsoUser updateBy;
    @Column(
            name = "update_date"
    )
    private Date updateDate;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
