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
 * 需求信息
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "RequirementInfo")
@Table(name = "requirement_info")
public class RequirementInfo extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = -8263897842362510582L;

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
            name = "serial"
    )
    private String serial;
    @Column(
            name = "customer"
    )
    private String customer;
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
            name = "link_phone"
    )
    private String linkPhone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_training_target"
    )
    private EnumConst enumConstByFlagTrainingTarget;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_info_source"
    )
    private EnumConst enumConstByFlagInfoSource;
    @Column(
            name = "requirement_info"
    )
    private String requirementInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_create_user_id"
    )
    private PeManager createUser;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @Column(
            name = "update_time"
    )
    private Date updateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_follow_up_user_id"
    )
    private PeManager followUpUser;
    @Column(
            name = "accept_time"
    )
    private Date acceptTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_requirement_status"
    )
    private EnumConst enumConstByFlagRequirementStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_follow_up_status"
    )
    private EnumConst enumConstByFlagFollowUpStatus;
    @Column(
            name = "training_person_number"
    )
    private Integer trainingPersonNumber;
    @Column(
            name = "training_day_number"
    )
    private Integer trainingDayNumber;
    @Column(
            name = "daily_training_fee"
    )
    private BigDecimal dailyTrainingFee;
    @Column(
            name = "daily_room_fee"
    )
    private BigDecimal dailyRoomFee;
    @Column(
            name = "daily_transport_fee"
    )
    private BigDecimal dailyTransportFee;
    @Column(
            name = "daily_food_fee"
    )
    private BigDecimal dailyFoodFee;
    @Column(
            name = "daily_tea_break_fee"
    )
    private BigDecimal dailyTeaBreakFee;
    @Column(
            name = "other_fee"
    )
    private BigDecimal otherFee;
    @Column(
            name = "note"
    )
    private String note;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
