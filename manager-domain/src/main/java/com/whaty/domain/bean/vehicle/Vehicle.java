package com.whaty.domain.bean.vehicle;

import com.whaty.HasAttachFile;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
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
 * vm-车辆管理-车辆表
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "Vehicle")
@Table(name = "vehicle")
public class Vehicle extends AbstractBean implements HasAttachFile {

    private static final long serialVersionUID = 5705614753827283185L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 所属车队
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorcade_id")
    private Motorcade motorcade;

    /**
     * 车牌号
     */
    @Column(name = "plate_num")
    private String plateNum;

    /**
     * 车型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_vehicle_type")
    private EnumConst enumConstByFlagVehicleType;

    /**
     * 车辆状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_vehicle_status")
    private EnumConst enumConstByFlagVehicleStatus;

    /**
     * 容量（人）
     */
    @Column(name = "capacity")
    private Integer capacity;

    /**
     * 是否删除（'0'未删除，'1'已删除）
     */
    @Column(name = "is_delete")
    private String isDelete;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

}
