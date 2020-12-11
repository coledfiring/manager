package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * author weipengsen  Date 2020/7/7
 */
@Data
@Entity(name = "PeCheckPoint")
@Table(name = "pe_check_point")
public class PeCheckPoint extends AbstractBean {

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
            name = "name"
    )
    private String name;

    @Column(
            name = "address"
    )
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;

    @Column(
            name = "code"
    )
    private String code;
}
