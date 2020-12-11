package com.whaty.analyse.framework.domain.bean;

import com.whaty.core.bean.AbstractBean;
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

/**
 * 统计配置详情
 *
 * @author weipengsen
 */
@Data
@Entity(name = "AnalyseConfigDetail")
@Table(name = "analyse_config_detail")
public class AnalyseConfigDetail extends AbstractBean {

    private static final long serialVersionUID = 7737249148708959618L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @Column(name = "config")
    private String config;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_basic_config_id")
    private AnalyseBasicConfig analyseBasicConfig;

}
