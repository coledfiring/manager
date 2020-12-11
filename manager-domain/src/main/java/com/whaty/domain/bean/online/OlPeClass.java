package com.whaty.domain.bean.online;

import com.whaty.HasAttachFile;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import com.whaty.domain.bean.SsoUser;
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
 * 班级
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OlPeClass")
@Table(name = "ol_pe_class")
public class OlPeClass extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = -2139472276425859370L;
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
            name = "code"
    )
    private String code;
    @Column(
            name = "start_time"
    )
    private Date startTime;
    @Column(
            name = "end_time"
    )
    private Date endTime;
    @Column(
            name = "period"
    )
    private Integer period;
    @Column(
            name = "credit"
    )
    private BigDecimal credit;
    @Column(
            name = "price"
    )
    private BigDecimal price;
    @Column(
            name = "discount_price"
    )
    private BigDecimal discountPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_online_class_status"
    )
    private EnumConst enumConstByFlagOnlineClassStatus;
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @Column(
            name = "picture_url"
    )
    private String pictureUrl;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
