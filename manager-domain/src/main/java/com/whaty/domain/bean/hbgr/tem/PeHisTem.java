package com.whaty.domain.bean.hbgr.tem;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @program: com.whaty.domain.bean.hbgr.tem
 * @author: weipengsen
 * @create: 2020-12-15
 **/
@Data
@Entity(name = "PeHisTem")
@Table(name = "pe_his_tem")
public class PeHisTem extends AbstractBean {

    private static final long serialVersionUID = -3147363403682909747L;

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
            name = "DEVICE_ID"
    )
    private String DEVICEID;

    @Column(
            name = "TEMP"
    )
    private String TEMP;

    @Column(
            name = "HUMI"
    )
    private String HUMI;

    @Column(
            name = "UPLOADTIME"
    )
    private Date UPLOADTIME;

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
