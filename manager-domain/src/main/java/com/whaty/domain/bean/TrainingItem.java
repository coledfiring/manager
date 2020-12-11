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
 * 培训项目
 *
 * @author weipengsen
 */
@Data
@Entity(name = "TrainingItem")
@Table(name = "training_item")
public class TrainingItem extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = 373718006525591008L;
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
    @Column(
            name = "code"
    )
    private String code;
    @Column(
            name = "name"
    )
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_item_status"
    )
    private EnumConst enumConstByFlagItemStatus;
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
            name = "fk_check_user_id"
    )
    private SsoUser checkUser;
    @Column(
            name = "check_date"
    )
    private Date checkDate;
    @Column(
            name = "check_note"
    )
    private String checkNote;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_training_type"
    )
    private EnumConst enumConstByFlagTrainingType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_training_target"
    )
    private EnumConst enumConstByFlagTrainingTarget;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_is_legal"
    )
    private EnumConst enumConstByFlagIsLegal;
    @Column(
            name = "training_address"
    )
    private String trainingAddress;
    @Column(
            name = "enroll_end_time"
    )
    private Date enrollEndTime;
    @Column(
            name = "training_start_time"
    )
    private Date trainingStartTime;
    @Column(
            name = "training_end_time"
    )
    private Date trainingEndTime;
    @Column(
            name = "training_person_number"
    )
    private Integer trainingPersonNumber;
    @Column(
            name = "linkman"
    )
    private String linkman;
    @Column(
            name = "link_phone"
    )
    private String linkPhone;
    @Column(
            name = "training_period"
    )
    private String trainingPeriod;
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
            name = "site_fee"
    )
    private BigDecimal siteFee;
    @Column(
            name = "other_fee"
    )
    private BigDecimal otherFee;
    @Column(
            name = "total_fee"
    )
    private BigDecimal totalFee;
    @Column(
            name = "collect_fee_unit"
    )
    private String collectFeeUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_is_publish"
    )
    private EnumConst enumConstByFlagIsPublish;
    @Column(
            name = "unit_head_name"
    )
    private String unitHeadName;
    @Column(
            name = "put_on_record_time"
    )
    private Date putOnRecordTime;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_training_item_extend_id"
    )
    private TrainingItemExtend trainingItemExtend;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_training_item_type"
    )
    private EnumConst enumConstByFlagTrainingItemType;
    @Column(
            name = "school_divide_rate"
    )
    private BigDecimal schoolDivideRate;
    @Column(
            name = "cooperation_unit"
    )
    private String cooperationUnit;
    @Column(
            name = "cooperation_unit_address"
    )
    private String cooperationUnitAddress;
    @Column(
            name = "cooperation_unit_corporate"
    )
    private String cooperationUnitCorporate;
    @Column(
            name = "cooperation_unit_linkman"
    )
    private String cooperationUnitLinkman;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_cooperate_unit_id"
    )
    private CooperateUnit cooperateUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_entrusted_unit_id"
    )
    private EntrustedUnit entrustedUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_info_source"
    )
    private EnumConst enumConstByFlagInfoSource;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_is_apportion"
    )
    private EnumConst enumConstByFlagIsApportion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_arrange_unit_id"
    )
    private PeUnit arrangeUnit;
    /**
     * 安排人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_arrange_user_id"
    )
    private SsoUser arrangeUser;
    /**
     * 安排日期
     */
    @Column(
            name = "arrange_date"
    )
    private Date arrangeDate;
    /**
     * 分派人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_apportion_user_id"
    )
    private SsoUser apportionUser;
    /**
     * 分派日期
     */
    @Column(
            name = "apportion_date"
    )
    private Date apportionDate;
    /**
     * 项目负责人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_manager_id"
    )
    private PeManager peManager;
    @Column(
            name = "entrust_unit_linkman"
    )
    private String entrustUnitLinkman;
    @Column(
            name = "entrust_unit_link_phone"
    )
    private String entrustUnitLinkPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_enroll_need_check")
    private EnumConst enumConstByFlagEnrollNeedCheck;

}
