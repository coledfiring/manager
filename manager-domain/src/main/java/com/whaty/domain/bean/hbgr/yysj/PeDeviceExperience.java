package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * author weipengsen  Date 2020/6/20
 */
@Data
@Entity(name = "PeDeviceExperience")
@Table(name = "pe_device_experience")
public class PeDeviceExperience extends AbstractBean {

    private static final long serialVersionUID = -2107743421521828931L;
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
            name = "experience"
    )
    private String experience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_device_id"
    )
    private PeDevice peDevice;

    @Column(
            name = "reason"
    )
    private String reason;

    @Column(
            name = "create_time"
    )
    private Date createTime;
}
