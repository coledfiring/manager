package com.whaty.domain.bean.hbgr.plan;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity(name = "PePlanDetail")
@Table(name = "pe_plan_detail")
public class PePlanDetail extends AbstractBean {

    private static final long serialVersionUID = 337585380084503445L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "fk_plan_id"
    )
    private PePlan pePlan;

    @Column(
            name = "start_time"
    )
    private String startTime;

    @Column(
            name = "end_time"
    )
    private String endTime;

    @Column(
            name = "boiler"
    )
    private String boiler;

    @Column(
            name = "w1_pump"
    )
    private String w1Pump;

    @Column(
            name = "w2_pump"
    )
    private String w2Pump;
}
