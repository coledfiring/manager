package com.whaty.domain.bean.hbgr.plan;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity(name = "PePlan")
@Table(name = "pe_plan")
public class PePlan extends AbstractBean {

    private static final long serialVersionUID = 2801671695272349044L;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;


    @Column(
            name = "out_tem"
    )
    private String outTem;

    @Column(
            name = "set_tem"
    )
    private String setTem;


    @Column(
            name = "gas"
    )
    private String gas;
}
