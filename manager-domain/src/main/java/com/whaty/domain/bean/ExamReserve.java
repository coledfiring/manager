package com.whaty.domain.bean;

import com.whaty.constant.SiteConstant;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.framework.reference.ValidatePolicy;
import com.whaty.framework.reference.annotation.ValidateReference;
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
 * 报名批次管理
 *
 * @author shanshuai
 */
@Data
@Entity(name = "ExamReserve")
@Table(name = "exam_reserve")
@ValidateReference(
        siteCodes = SiteConstant.SERVICE_SITE_CODE_GZUCMPX,
        validatePolicy = ValidatePolicy.ONLY_POLICY
)
public class ExamReserve extends AbstractSiteBean {

    private static final long serialVersionUID = 6615331809171844439L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_exam_site_id")
    private ExamSite examSite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_exam_course_id")
    private ExamCourse examCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_exam_batch_id")
    private ExamBatch examBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id")
    private PeStudent peStudent;

    @Column(name = "exam_card_no")
    private String examCardNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_enroll_status")
    private EnumConst enumConstByFlagEnrollStatus;

    @Column(name = "enroll_date")
    private Date enrollDate;

    @Column(name = "check_date")
    private Date checkDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_can_print")
    private EnumConst enumConstByFlagCanPrint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_is_print")
    private EnumConst enumConstByFlagIsPrint;

    @Column(name = "site_code")
    private String siteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_user_id")
    private SsoUser ssoUser;

    @Column(name = "exam_place")
    private String examPlace;

    @Column(name = "exam_seat_number")
    private String examSeatNumber;

    @Column(name = "exam_date")
    private String examDate;

    @Column(name = "note")
    private String note;
}
