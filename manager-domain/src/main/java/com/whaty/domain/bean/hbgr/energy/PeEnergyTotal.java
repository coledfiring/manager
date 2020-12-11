package com.whaty.domain.bean.hbgr.energy;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 *  author weipengsen
 */
@Data
@Entity(name = "PeEnergyTotal")
@Table(name = "pe_energy_total")
public class PeEnergyTotal extends AbstractBean {

    private static final long serialVersionUID = -8105169699690866096L;

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
            name = "heat"
    )
    private Double heat;

    @Column(
            name = "dn_heat"
    )
    private Double dnHeat;

    @Column(
            name = "kt_heat"
    )
    private Double ktHeat;

    @Column(
            name = "water"
    )
    private Double water;

    @Column(
            name = "electricity"
    )
    private Double electricity;

    @Column(
            name = "gas"
    )
    private Double gas;

    @Column(
            name = "heat_price"
    )
    private Double heatPrice;

    @Column(
            name = "water_price"
    )
    private Double waterPrice;

    @Column(
            name = "electricity_price"
    )
    private Double electricityPrice;

    @Column(
            name = "gas_price"
    )
    private Double gasPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;

    @Column(
            name = "create_time"
    )
    private Date createTime;

    @Column(
            name = "building_area"
    )
    private String buildingArea;

    @Column(
            name = "heating_area"
    )
    private String heatingArea;
}
