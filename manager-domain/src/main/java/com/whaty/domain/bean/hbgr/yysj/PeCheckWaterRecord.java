package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.PeManager;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * author weipengsen  Date 2020/7/13
 */
@Data
@Entity(name = "PeCheckWaterRecord")
@Table(name = "pe_check_water_record")
public class PeCheckWaterRecord extends AbstractBean {

    private static final long serialVersionUID = -3631007681670118958L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_check_manager"
    )
    private PeManager peManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_check_point"
    )
    private PeCheckPoint peCheckPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_isValid_1"
    )
    private EnumConst enumConstByFlagIsvalid1;

    @Column(
            name = "hard_1"
    )
    private String hard1;

    @Column(
            name = "PH_1"
    )
    private String ph1;

    @Column(
            name = "CI_1"
    )
    private String ci1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_isValid_2"
    )
    private EnumConst enumConstByFlagIsvalid2;

    @Column(
            name = "hard_2"
    )
    private String hard2;

    @Column(
            name = "PH_2"
    )
    private String ph2;

    @Column(
            name = "CI_2"
    )
    private String ci2;

    @Column(
            name = "note"
    )
    private String note;

    @Column(
            name = "create_time"
    )
    private Date createTime;
}
