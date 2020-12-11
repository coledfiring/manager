package com.whaty.domain.bean.flow;

import com.whaty.core.bean.AbstractBean;
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
 * 审核流程抄送人详情
 *
 * @author weipengsen
 */
@Data
@Entity(name = "CheckFlowCopyPerson")
@Table(name = "check_flow_copy_person")
public class CheckFlowCopyPerson extends AbstractBean {

    private static final long serialVersionUID = 2427284651122627403L;
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
            name = "copy_person"
    )
    private String copyPerson;
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
