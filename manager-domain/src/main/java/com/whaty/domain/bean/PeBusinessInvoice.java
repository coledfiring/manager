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

/**
 * 单位发票管理
 * @author shanshuai
 */
@Data
@Entity(name = "PeBusinessInvoice")
@Table(name = "pe_business_invoice")
public class PeBusinessInvoice extends AbstractSiteBean {
    private static final long serialVersionUID = 6424029360879879940L;

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "identify_number")
    private String identifyNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_business_unit_id")
    private PeBusinessUnit peBusinessUnit;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "note")
    private String note;

    @Column(name = "site_code")
    private String siteCode;

    @Column(name = "invoice_unit")
    private String invoiceUnit;

    @Column(name = "invoice_phone")
    private String invoicePhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_invoice_type")
    private EnumConst enumConstByFlagInvoiceType;

}
