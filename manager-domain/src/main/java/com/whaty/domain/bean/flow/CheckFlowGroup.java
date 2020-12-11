package com.whaty.domain.bean.flow;

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
import java.util.Date;

/**
 * 审核流程组
 *
 * @author weipengsen
 */
@Data
@Entity(name = "CheckFlowGroup")
@Table(name = "check_flow_group")
public class CheckFlowGroup extends AbstractSiteBean {

    private static final long serialVersionUID = -4938218074640717705L;
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
            name = "flag_check_status"
    )
    private EnumConst enumConstByFlagCheckStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "fk_copy_person_detail_id"
    )
    private CheckFlowCopyPerson checkFlowCopyPerson;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    @Column(
            name = "note"
    )
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "update_by"
    )
    private SsoUser updateBy;
    @Column(
            name = "update_date"
    )
    private Date updateDate;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_need_check"
    )
    private EnumConst enumConstByFlagNeedCheck;

}
