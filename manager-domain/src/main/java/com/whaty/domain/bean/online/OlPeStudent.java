package com.whaty.domain.bean.online;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import com.whaty.domain.bean.SsoUser;
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
 * 学生
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OlPeStudent")
@Table(name = "ol_pe_student")
public class OlPeStudent extends AbstractSiteBean {

    private static final long serialVersionUID = 2299542535222998960L;
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
            name = "true_name"
    )
    private String trueName;
    @Column(
            name = "login_id"
    )
    private String loginId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_gender"
    )
    private EnumConst enumConstByFlagGender;
    @Column(
            name = "work_unit"
    )
    private String workUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_online_positional_title"
    )
    private EnumConst enumConstByFlagOnlinePositionalTitle;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_online_professional_title_series"
    )
    private EnumConst enumConstByFlagOnlineProfessionalTitleSeries;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_online_professional_title_level"
    )
    private EnumConst enumConstByFlagOnlineProfessionalTitleLevel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_isvalid"
    )
    private EnumConst enumConstByFlagIsvalid;
    @Column(
            name = "mobile"
    )
    private String mobile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_sso_user_id"
    )
    private SsoUser ssoUser;
    @Column(
            name = "email"
    )
    private String email;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_organization_id"
    )
    private OlPeOrganization olPeOrganization;
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @Column(
            name = "picture_url"
    )
    private String pictureUrl;
    @Column(
            name = "order_no"
    )
    private Integer orderNo;
    @Column(
            name = "card_no"
    )
    private String cardNo;
    @Column(
            name = "degree"
    )
    private String degree;
    @Column(
            name = "educational_background"
    )
    private String educationalBackground;
    @Column(
            name = "register_date"
    )
    private Date registerDate;
    @Column(
            name = "publish_date"
    )
    private Date publishDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_class_id"
    )
    private OlPeClass olPeClass;
}
