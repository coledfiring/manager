package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * author weipengsen  Date 2020/7/15
 */
@Data
@Entity(name = "PeClientRecord")
@Table(name = "pe_client_record")
public class PeClientRecord extends AbstractBean {

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
            name = "mobile"
    )
    private String mobile;

    @Column(
            name = "address"
    )
    private String address;

    @Column(
            name = "problem"
    )
    private String problem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_Isvalid"
    )
    private EnumConst enumConstByFlagIsvalid;
}
