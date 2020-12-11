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
 * 审核流程详情
 * @author weipengsen
 */
@Data
@Entity(name = "CheckFlowDetail")
@Table(name = "check_flow_detail")
public class CheckFlowDetail extends AbstractBean {

    private static final long serialVersionUID = 8969339156572140948L;
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
            name = "check_detail"
    )
    private String checkDetail;

    public CheckFlowDetail() {
    }

    public CheckFlowDetail(String checkDetail) {
        this.checkDetail = checkDetail;
    }

}
