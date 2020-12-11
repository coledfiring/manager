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
 * 审核流程
 *
 * @author weipengsen
 */
@Data
@Entity(name = "CheckFlow")
@Table(name = "check_flow")
public class CheckFlow extends AbstractSiteBean {

    private static final long serialVersionUID = -5027885437746325275L;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_flow_type"
    )
    private EnumConst enumConstByFlagFlowType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_check_status"
    )
    private EnumConst enumConstByFlagCheckStatus;
    @Column(
            name = "fk_item_id"
    )
    private String itemId;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @Column(
            name = "current_node"
    )
    private String currentNode;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "fk_check_detail_id"
    )
    private CheckFlowDetail checkFlowDetail;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_flow_group_id"
    )
    private CheckFlowGroup checkFlowGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    @Column(
            name = "auditors"
    )
    private String auditors;
    @Column(
            name = "copy_persons"
    )
    private String copyPersons;
    @Column(
            name = "complete_date"
    )
    private Date completeDate;
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
            name = "flag_need_check"
    )
    private EnumConst enumConstByFlagNeedCheck;
    @Column(
            name = "scope_id"
    )
    private String scopeId;

}
