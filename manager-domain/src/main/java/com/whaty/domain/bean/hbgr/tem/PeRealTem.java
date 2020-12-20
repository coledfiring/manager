package com.whaty.domain.bean.hbgr.tem;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @program: com.whaty.domain.bean.hbgr.tem
 * @author: weipengsen
 * @create: 2020-12-16
 **/
@Data
@Entity(name = "PeRealTem")
@Table(name = "pe_real_tem")
public class PeRealTem extends AbstractBean {

    private static final long serialVersionUID = -693309184370553674L;


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
            name = "NODEID"
    )
    private String nodeId;

    @Column(
            name = "DEVICE_ID"
    )
    private String deviceId;

    @Column(
            name = "TEMP"
    )
    private String temp;

    @Column(
            name = "HUMI"
    )
    private String humi;

    @Column(
            name = "SIGNALSTRENGTH"
    )
    private String signalStrength;

    @Column(
            name = "UPLOADTIME"
    )
    private Date uploadName;

    @Column(
            name = "BATTERYLEVEL"
    )
    private String batteryLevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;

    @Column(
            name = "address"
    )
    private String address;
}
