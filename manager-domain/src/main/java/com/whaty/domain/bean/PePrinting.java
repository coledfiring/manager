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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 印刷管理实体类
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PePrinting")
@Table(name = "pe_printing")
public class PePrinting extends AbstractSiteBean {

    private static final long serialVersionUID = -8979283112380401123L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_class_id"
    )
    private PeClass peClass;

    @Column(
            name = "printing_name"
    )
    private String printingName;

    @Column(
            name = "site_code"
    )
    private String siteCode;

    @Column(
            name = "printing_content"
    )
    private String printingContent;

    @Column(
            name = "paper"
    )
    private String paper;

    @Column(
            name = "printing_number"
    )
    private String printingNumber;

    @Column(
            name = "printing_require"
    )
    private String printingRequire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_printing_unit_id"
    )
    private PrintingUnit printingUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_unit_id"
    )
    private PeUnit peUnit;

    @Column(
            name = "note"
    )
    private String note;

    @Column(
            name = "price"
    )
    private BigDecimal price;

    @Column(
            name = "applicant_time"
    )
    private Date applicantTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_applicant_user_id"
    )
    private SsoUser applicantUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_accept_user_id"
    )
    private SsoUser acceptUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_check_user_id"
    )
    private SsoUser checkUser;

    @Column(
            name = "accept_time"
    )
    private Date accpetTime;

    @Column(
            name = "check_time"
    )
    private Date checkTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_accept_status"
    )
    private EnumConst enumConstByFlagAcceptStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_check_status"
    )
    private EnumConst enumConstByFlagCheckStatus;

}
