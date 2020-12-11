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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 单位订单发票
 *
 * @author shanshuai
 */
@Data
@Entity(name = "PeBusinessOrderInvoice")
@Table(name = "pe_business_order_invoice")
public class PeBusinessOrderInvoice extends AbstractSiteBean {

    private static final long serialVersionUID = -604989540031139583L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_business_order_id")
    private PeBusinessOrder peBusinessOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_invoice_type")
    private EnumConst enumConstByFlagInvoiceType;

    @Column(name = "name")
    private String name;

    @Column(name = "identify_number")
    private String identifyNumber;

    @Column(name = "business_unit_address")
    private String businessUnitAddress;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "invoice_unit")
    private String invoiceUnit;

    @Column(name = "invoice_num")
    private Integer invoiceNum;

    @Column(name = "invoice_single_amount")
    private BigDecimal invoiceSingleAmount;

    @Column(name = "invoice_note")
    private String invoiceNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_receice_status")
    private EnumConst enumConstByFlagReceiceStatus;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "recipient_phone")
    private String recipientPhone;

    @Column(name = "recipient_address")
    private String recipientAddress;

    @Column(name = "invoice_phone")
    private String invoicePhone;

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

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}
