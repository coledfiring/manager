package com.whaty.domain.bean.hbgr.energy;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * author weipengsen
 */
@Data
@Entity(name = "PeEnergyDay")
@Table(name = "pe_energy_day")
public class PeEnergyDay extends AbstractBean {

    private static final long serialVersionUID = 5377816798663305780L;

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
            name = "create_time"
    )
    private Date createTime;

    @Column(
            name = "gas"
    )
    private Double gas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;
}
