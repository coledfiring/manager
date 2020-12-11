package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 易耗品管理实体类
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "PeConsumable")
@Table(name = "pe_consumable")
public class PeConsumable extends AbstractSiteBean {

    private static final long serialVersionUID = -4543241855257945827L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 领用班级或其他项目id
     */
    @Column(name = "item_id")
    private String itemId;

    /**
     * 领用用途
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_use_purpose")
    private EnumConst enumConstByFlagUsePurpose;

    @Column(name = "site_code")
    private String siteCode;

    /**
     * 申请人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_applicant_user_id")
    private SsoUser applicantUser;

    /**
     * 申请人电话
     */
    @Column(name = "applicant_user_tel")
    private String applicantUserTel;

    /**
     * 申请时间
     */
    @Column(name = "applicant_time")
    private Date applicantTime;

    /**
     * 使用状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_use_status")
    private EnumConst enumConstByFlagUseStatus;

    /**
     * 验收人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_check_user_id")
    private SsoUser checkUser;

    /**
     * 验收状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_check_status")
    private EnumConst enumConstByFlagCheckStatus;

    /**
     * 验收时间
     */
    @Column(name = "check_time")
    private Date checkTime;

    /**
     * 验收金额
     */
    @Column(name = "check_price")
    private BigDecimal checkPrice;

    /**
     * 申请单位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_unit_id")
    private PeUnit peUnit;

    /**
     * 备注
     */
    @Column(name = "note")
    private String note;

    @Transient
    private Map<String, Integer> peConsumableItems;

}
