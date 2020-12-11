package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * author weipengsen  Date 2020/6/20
 */
@Data
@Entity(name = "PeCheck")
@Table(name = "pe_check")
public class PeCheck extends AbstractBean {

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
            name = "users"
    )
    private String users;

    @Column(
            name = "points"
    )
    private String points;

    @Column(
            name = "days"
    )
    private Integer days;

    @Column(
            name = "note"
    )
    private String note;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_check_type"
    )
    private EnumConst enumConstByFlagCheckType;

    @Column(
            name = "create_time"
    )
    private Date createTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_Isvalid"
    )
    private EnumConst enumConstByFlagIsvalid;

    @Column(
            name = "start_time"
    )
    private Date startTime;

    @Override
    public String toString() {
        return "PeCheck{" +
                "id='" + id + '\'' +
                ", users='" + users + '\'' +
                ", points='" + points + '\'' +
                ", days=" + days +
                ", note='" + note + '\'' +
                ", enumConstByFlagScene=" + enumConstByFlagScene +
                ", enumConstByFlagCheckType=" + enumConstByFlagCheckType +
                ", createTime=" + createTime +
                ", enumConstByFlagIsvalid=" + enumConstByFlagIsvalid +
                ", startTime=" + startTime +
                '}';
    }
}

