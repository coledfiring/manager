package com.whaty.domain.bean;


import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用印实体类
 *
 * @author weipengsen
 */
@Data
@Entity(name = "UseSeal")
@Table(name = "use_seal")
public class UseSeal extends AbstractSiteBean {

    private static final long serialVersionUID = -8979283148580401123L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 用印单位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_applicant_unit"
    )
    private PeUnit peUnit;

    /**
     * 申请使用时间
     */
    @Column(
            name = "applicant_use_time"
    )
    private Date applicantUseTime;

    /**
     * 申请人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_applicant_user_id"
    )
    private SsoUser applicantUser;

    /**
     * 申请时间
     */
    @Column(
            name = "applicant_time"
    )
    private Date applicantTime;

    /**
     * 受理状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_accept_status"
    )
    private EnumConst enumConstByFlagAcceptStatus;

    /**
     * 受理人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_accept_user_id"
    )
    private SsoUser acceptUser;

    /**
     * 受理时间
     */
    @Column(
            name = "accept_time"
    )
    private Date acceptTime;

    /**
     * 用印类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_use_seal_type"
    )
    private EnumConst enumConstByFlagUseSealType;

    @Column(
            name = "site_code"
    )
    private String siteCode;

    /**
     * 用印事由
     */
    @Column(
            name = "note"
    )
    private String note;

    /**
     * 审批状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_check_status"
    )
    private EnumConst enumConstByFlagCheckStatus;


}
