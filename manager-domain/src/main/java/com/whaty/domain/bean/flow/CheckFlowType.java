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
 * 审核流程关联的项目类型
 *
 * @author weipengsen
 */
@Data
@Entity(name = "CheckFlowType")
@Table(name = "check_flow_type")
public class CheckFlowType extends AbstractBean {

    private static final long serialVersionUID = 6966360793412147844L;
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
            name = "flag_flow_type"
    )
    private EnumConst enumConstByFlagFlowType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_flow_group_id"
    )
    private CheckFlowGroup checkFlowGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createDate;

}
