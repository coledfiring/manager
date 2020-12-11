package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * author weipengsen  Date 2020/6/19
 */
@Data
@Entity(name = "PeDevice")
@Table(name = "pe_device")
public class PeDevice extends AbstractBean {

    private static final long serialVersionUID = 1022775040421405742L;
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
            name = "name"
    )
    private String name;

    @Column(
            name = "code"
    )
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_device_type"
    )
    private PeDeviceType peDeviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;

    @Column(
            name = "power"
    )
    private String power;

    @Column(
            name = "factory"
    )
    private String factory;

    @Column(
            name = "provide"
    )
    private String provide;

    @Column(
            name = "note"
    )
    private String note;

}

