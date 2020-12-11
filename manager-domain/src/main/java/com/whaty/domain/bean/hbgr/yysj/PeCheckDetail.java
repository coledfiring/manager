package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.SsoUser;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * author weipengsen  Date 2020/6/20
 */
@Data
@Entity(name = "PeCheckDetail")
@Table(name = "pe_check_detail")
public class PeCheckDetail extends AbstractBean {

    private static final long serialVersionUID = -4748721384993964605L;
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
            name = "check_time"
    )
    private Date checkTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_user_id"
    )
    private SsoUser checkPerson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_check_point"
    )
    private EnumConst enumConstByFlagCheckPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;
}
