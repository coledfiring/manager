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
 * 师资
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OlPeTeacher")
@Table(name = "ol_pe_teacher")
public class OlPeTeacher extends AbstractSiteBean {

    private static final long serialVersionUID = -169571665408221775L;

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
            name = "login_id"
    )
    private String loginId;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "true_name"
    )
    private String trueName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_online_positional_title"
    )
    private EnumConst enumConstByFlagOnlinePositionalTitle;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_online_educational_background"
    )
    private EnumConst enumConstByFlagOnlineEducationalBackground;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_course_teacher"
    )
    private EnumConst enumConstByFlagCourseTeacher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_tutor_teacher"
    )
    private EnumConst enumConstByFlagTutorTeacher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_isclassmaster"
    )
    private EnumConst enumConstByFlagIsclassmaster;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    @Column(
            name = "card_no"
    )
    private String cardNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_gender"
    )
    private EnumConst enumConstByFlagGender;
    @Column(
            name = "work_unit"
    )
    private String workUnit;
    @Column(
            name = "mobile"
    )
    private String mobile;
    @Column(
            name = "work_phone"
    )
    private String workPhone;
    @Column(
            name = "home_phone"
    )
    private String homePhone;
    @Column(
            name = "email"
    )
    private String email;
    @Column(
            name = "address"
    )
    private String address;
    @Column(
            name = "zip_code"
    )
    private String zipCode;
    @Column(
            name = "qq"
    )
    private String qq;
    @Column(
            name = "introduction"
    )
    private String introduction;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_sso_user_id"
    )
    private SsoUser ssoUser;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @Column(
            name = "picture_url"
    )
    private String pictureUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createDate;

}
