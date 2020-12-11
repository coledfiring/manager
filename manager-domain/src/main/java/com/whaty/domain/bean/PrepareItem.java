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
import java.util.Objects;

/**
 * 准备物品
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PrepareItem")
@Table(name = "prepare_item")
public class PrepareItem extends AbstractSiteBean {

    private static final long serialVersionUID = 43561823613147359L;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_prepare_type"
    )
    private EnumConst enumConstByFlagPrepareType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_training_type"
    )
    private EnumConst enumConstByFlagTrainingType;
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "update_date"
    )
    private Date updateDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "update_by"
    )
    private SsoUser updateBy;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @Column(
            name = "order_no"
    )
    private String orderNo;

}
