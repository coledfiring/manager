package com.whaty.domain.bean.flow;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 审核流程审核人
 *
 * @author weipengsen
 */
@Data
@Entity(name = "CheckFlowAuditor")
@Table(name = "check_flow_auditor")
public class CheckFlowAuditor extends AbstractBean {

    private static final long serialVersionUID = 2324862538508428710L;
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
            name = "auditor"
    )
    private String auditor;

}
