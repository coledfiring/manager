package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.enroll.EnrollPerson;
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
 * @author weipengsen
 */
@Data
@Entity(name = "PeStudent")
@Table(name = "pe_student")
public class PeStudent extends AbstractSiteBean {

    private static final long serialVersionUID = 4365718803616844737L;
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
            name = "flag_folk"
    )
    private EnumConst enumConstByFlagFolk;
    @Column(
            name = "positional_title"
    )
    private String positionalTitle;
    @Column(
            name = "mobile"
    )
    private String mobile;
    @Column(
            name = "work_phone"
    )
    private String workPhone;
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
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_class_id"
    )
    private PeClass peClass;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @Column(
            name = "certificate_number"
    )
    private String certificateNumber;

    /**
     * 原证书编号
     */
    @Column(name = "original_certificate_number")
    private String originalCertificateNumber;

    /**
     * 原证书发证日期
     */
    @Column(name = "original_certificate_release_date")
    private Date originalCertificateReleaseDate;

    /**
     * 原工种类别
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_work_type")
    private EnumConst originalWorkType;

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

    /**
     * 学生报名单位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_business_unit_id")
    private PeBusinessUnit peBusinessUnit;

    /**
     * 是否首次报名
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_first_enroll_status")
    private EnumConst enumConstByFlagFirstEnrollStatus;

    /**
     * 单位订单id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_business_order_id")
    private PeBusinessOrder peBusinessOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_training_item_id")
    private TrainingItem trainingItem;

    @Column(name = "enroll_check_note")
    private String enrollCheckNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_educational_background")
    private EnumConst enumConstByFlagEducationalBackground;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_item_enroll_type")
    private EnumConst enumConstByFlagItemEnrollType;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_enroll_person_id")
    private EnrollPerson enrollPerson;

    @Column(name = "training_id")
    private String trainingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_card_type")
    private EnumConst enumConstByFlagCardType;

}
