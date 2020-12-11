package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 政策法规管理扩展类
 *
 * @author shangyu
 */
@Data
@Entity(name = "PolicyRegulationExtend")
@Table(name = "policy_regulation_extend")
public class PolicyRegulationExtend extends AbstractBean {

    private static final long serialVersionUID = 116253531525499126L;

    /**
     * 主键ID
     */
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @Column(name = "content")
    private String content;

}
