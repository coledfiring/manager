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
 * 印刷单位实体类
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PrintingUnit")
@Table(name = "printing_unit")
public class PrintingUnit extends AbstractSiteBean {

    private static final long serialVersionUID = -5679283148580401123L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(
            name = "name"
    )
    private String name;

    @Column(
            name = "agreement_time"
    )
    private Date agreementTime;

    @Column(
            name = "agreement_price"
    )
    private String agreementPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_is_valid"
    )
    private EnumConst enumConstByFlagIsValid;

    @Column(
            name = "site_code"
    )
    private String siteCode;

}
