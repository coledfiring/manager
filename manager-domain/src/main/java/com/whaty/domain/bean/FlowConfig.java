package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 流程图配置
 * @author weipengsen
 */
@Data
@Entity(name = "FlowConfig")
@Table(name = "flow_config")
@AllArgsConstructor
public class FlowConfig extends AbstractBean {

    private static final long serialVersionUID = 3155411638809331664L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id",
            unique = true,
            nullable = false
    )
    private String id;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "flow_config"
    )
    private String flowConfig;

    public FlowConfig() {
    }
}
