package com.whaty.domain.bean.online;

import com.whaty.HasAttachFile;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import com.whaty.domain.bean.PeArea;
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
 * 单位
 *
 * @author suoqiangqiang
 */
@Data
@Table(name = "ol_pe_organization")
@Entity(name = "OlPeOrganization")
public class OlPeOrganization extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = 1435493998231052566L;

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
            name = "flag_online_organization_type"
    )
    private EnumConst enumConstByFlagOnlineOrganizationType;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}