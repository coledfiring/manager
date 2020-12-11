package com.whaty.domain.bean;

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
 * 合作宾馆
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeHotel")
@Table(name = "pe_hotel")
public class PeHotel extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = -8979283714858040194L;
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
            name = "address"
    )
    private String address;
    @Column(
            name = "linkman"
    )
    private String linkman;
    @Column(
            name = "link_phone"
    )
    private String linkPhone;
    @Column(
            name = "agreement_price"
    )
    private String agreementPrice;
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

}
