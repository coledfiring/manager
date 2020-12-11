package com.whaty.domain.bean.vehicle;

import com.whaty.HasAttachFile;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import com.whaty.domain.bean.PeUnit;
import com.whaty.domain.bean.SsoUser;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * vm-车辆管理-用车申请表
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "VehicleApplication")
@Table(name = "vehicle_application")
public class VehicleApplication extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = 2608724031046380217L;

    public VehicleApplication() {
    }

    public VehicleApplication(String id) {
        this.id = id;
    }

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 申请单位类型（个人，班级，学院，学校等）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_applicant_unit_type")
    private EnumConst enumConstByFlagApplicantUnitType;

    /**
     * 申请单位id 根据类型对应不同的id （班级---班级id，学校---学校sitecode）
     */
    @Column(name = "applicant_unit_id")
    private String applicantUnitId;

    /**
     * 用车事由
     */
    @Column(name = "use_purpose")
    private String usePurpose;

    /**
     * 申请类型（常规使用，临时使用）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_vehicle_apply_type")
    private EnumConst enumConstByFlagVehicleApplyType;

    /**
     * 用车类型
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flag_vehicle_type")
    private EnumConst enumConstByFlagVehicleType;

    /**
     * 使用开始时间
     */
    @Column(name = "use_start_date")
    private Date useStartDate;

    /**
     * 使用结束时间
     */
    @Column(name = "use_end_date")
    private Date useEndDate;

    /**
     * 人数
     */
    @Column(name = "people_num")
    private Integer peopleNum;

    /**
     * 登车地点
     */
    @Column(name = "boarding_location")
    private String boardingLocation;

    /**
     * 登车时间（不包括日期）
     */
    @Column(name = "boarding_time")
    private String boardingTime;

    /**
     * 目的地
     */
    @Column(name = "destination")
    private String destination;

    /**
     * 申请人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_applicant_user_id")
    private SsoUser applicantUser;

    /**
     * 申请单位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_applicant_unit")
    private PeUnit applicantUnit;

    /**
     * 申请人电话
     */
    @Column(name = "applicant_user_tel")
    private String applicantUserTel;

    /**
     * 申请时间
     */
    @Column(name = "applicant_time")
    private Date applicantTime;

    /**
     * 通知方式
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_notification_type")
    private EnumConst enumConstByFlagNotificationType;

    /**
     * 申请状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_vehicle_apply_status")
    private EnumConst enumConstByFlagVehicleApplyStatus;

    /**
     * 结账时间
     */
    @Column(name = "checkout_date")
    private Date checkoutDate;

    /**
     * 建议车队
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_suggested_motorcade_id")
    private Motorcade suggestedMotorcade;

    /**
     * 安排人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_arranger_id")
    private SsoUser arranger;

    /**
     * 安排车队
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_arrange_motorcade_id")
    private Motorcade arrangeMotorcade;

    /**
     * 安排时间
     */
    @Column(name = "arrange_date")
    private Date arrangeDate;

    @Column(name = "site_code")
    private String siteCode;

    /**
     * 申请具体安排对象
     */
    @Transient
    private VehicleArrangement vehicleArrangement;
}
