package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 班级
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeClass")
@Table(name = "pe_class")
public class PeClass extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = -2139472276425859370L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_unit_id"
    )
    private PeUnit peUnit;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "fk_training_item_id"
    )
    private TrainingItem trainingItem;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "code"
    )
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_has_certificate"
    )
    private EnumConst enumConstByFlagHasCertificate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_apply_certificate_status"
    )
    private EnumConst enumConstByFlagApplyCertificateStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_settle_account_status"
    )
    private EnumConst enumConstByFlagSettleAccountStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_training_mode"
    )
    private EnumConst enumConstByFlagTrainingMode;
    @Column(
            name = "start_time"
    )
    private Date startTime;
    @Column(
            name = "end_time"
    )
    private Date endTime;
    @Column(
            name = "training_day_number"
    )
    private Integer trainingDayNumber;
    @Column(
            name = "period"
    )
    private Integer period;
    @Column(
            name = "training_person_number"
    )
    private Integer trainingPersonNumber;
    @Column(
            name = "stay_location"
    )
    private String stayLocation;
    @Column(
            name = "dining_location"
    )
    private String diningLocation;
    @Column(
            name = "period_fee"
    )
    private BigDecimal periodFee;
    @Column(
            name = "teacher_fee"
    )
    private BigDecimal teacherFee;
    @Column(
            name = "material_fee"
    )
    private BigDecimal materialFee;
    @Column(
            name = "enroll_fee"
    )
    private BigDecimal enrollFee;
    @Column(
            name = "food_fee"
    )
    private BigDecimal foodFee;
    @Column(
            name = "transport_fee"
    )
    private BigDecimal transportFee;
    @Column(
            name = "teaching_fee"
    )
    private BigDecimal teachingFee;
    @Column(
            name = "room_fee"
    )
    private BigDecimal roomFee;
    @Column(
            name = "site_Fee"
    )
    private BigDecimal siteFee;
    @Column(
            name = "other_fee"
    )
    private BigDecimal otherFee;
    @Column(
            name = "training_total_fee"
    )
    private BigDecimal trainingTotalFee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_class_master_id"
    )
    private PeManager classMaster;
    @Column(
            name = "note"
    )
    private String note;
    @Column(
            name = "score"
    )
    private Integer score;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "score_user"
    )
    private SsoUser scoreUser;
    @Column(
            name = "score_based"
    )
    private String scoreBased;
    @Column(
            name = "score_date"
    )
    private Date scoreDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @Column(
            name = "credit"
    )
    private String credit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_place_type"
    )
    private EnumConst enumConstByFlagPlaceType;
    @Column(
            name = "place_note"
    )
    private String placeNote;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_class_hotel_detail_id"
    )
    private ClassHotelDetail classHotelDetail;
    @Column(
            name = "entrust_unit_linkman"
    )
    private String entrustUnitLinkman;
    @Column(
            name = "entrust_unit_link_phone"
    )
    private String entrustUnitLinkPhone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_school_zone"
    )
    private EnumConst enumConstByFlagSchoolZone;

}
