package com.whaty.domain.bean.vehicle;

import com.whaty.HasAttachFile;
import com.whaty.core.bean.AbstractBean;
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
 * vm-车辆管理-用车安排表
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "VehicleArrangement")
@Table(name = "vehicle_arrangement")
public class VehicleArrangement extends AbstractBean implements HasAttachFile {

    private static final long serialVersionUID = -1008675795644551806L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 车辆申请关联id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_vehicle_application_id")
    private VehicleApplication vehicleApplication;

    /**
     * 车牌号
     */
    @Column(name = "plate_num")
    private String plateNum;

    /**
     * 驾驶员姓名
     */
    @Column(name = "driver_name")
    private String driverName;

    /**
     * 驾驶员电话
     */
    @Column(name = "driver_tel")
    private String driverTel;

    /**
     * 车辆id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_vehicle_id")
    private Vehicle vehicle;

    /**
     * 车费
     */
    @Column(name = "fare")
    private BigDecimal fare;

    /**
     * 使用人
     */
    @Column(name = "end_user")
    private String endUser;

    /**
     * 起始公里数（起点到目的地）
     */
    @Column(name = "start_kilometers")
    private Integer startKilometers;

    /**
     * 返回公里数（目的地到起点）
     */
    @Column(name = "return_kilometers")
    private Integer returnKilometers;

    /**
     * 返回时间
     */
    @Column(name = "return_date")
    private Date returnDate;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

}
