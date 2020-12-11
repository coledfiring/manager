package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 单位订单管理
 *
 * @author shanshuai
 */
@Data
@Entity(name = "PeBusinessOrder")
@Table(name = "pe_business_order")
public class PeBusinessOrder extends AbstractSiteBean {

    private static final long serialVersionUID = 1074251406376458581L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_business_unit_id")
    private PeBusinessUnit peBusinessUnit;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_business_pay_way")
    private EnumConst enumConstByFlagBusinessPayWay;

    @Column(name = "audit_opinion")
    private String auditOpinion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_business_fee_status")
    private EnumConst enumConstByFlagBusinessFeeStatus;

    @Column(name = "note")
    private String note;

    @Column(name = "site_code")
    private String siteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    private SsoUser createUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_user_id")
    private SsoUser updateUser;

}
