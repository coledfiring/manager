package com.whaty.domain.bean.flow;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
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
 * 审核流程节点
 *
 * @author weipengsen
 */
@Data
@Entity(name = "CheckFlowNode")
@Table(name = "check_flow_node")
public class CheckFlowNode extends AbstractBean {

    private static final long serialVersionUID = 7360547641741649509L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_flow_group_id"
    )
    private CheckFlowGroup checkFlowGroup;
    @Column(
            name = "serial"
    )
    private Integer serial;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_auditor_type"
    )
    private EnumConst enumConstByFlagAuditorType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_check_type"
    )
    private EnumConst enumConstByFlagCheckType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_node_type"
    )
    private EnumConst enumConstByFlagNodeType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "fk_auditor_detail_id"
    )
    private CheckFlowAuditor checkFlowAuditor;
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
}
