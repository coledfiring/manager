package com.whaty.analyse.framework.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
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
 * 统计块的条件
 *
 * @author weipengsen
 */
@Data
@Entity(name = "AnalyseBlockCondition")
@Table(name = "analyse_block_condition")
public class AnalyseBlockCondition extends AbstractCondition {

    private static final long serialVersionUID = 4280762920661684777L;

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
    @JoinColumn(name = "fk_block_id")
    private AnalyseBlockConfig analyseBlockConfig;

    @Column(name = "label")
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_analyse_condition_type")
    private EnumConst enumConstByFlagBlockConditionType;

    @Column(name = "sql")
    private String sql;

    @Column(name = "data_index")
    private String dataIndex;

    @Column(name = "serial")
    private Integer serial;

    @Column(name = "default_sql")
    private String defaultSql;

    @Column(name = "reflect_method")
    private String reflectMethod;

}
