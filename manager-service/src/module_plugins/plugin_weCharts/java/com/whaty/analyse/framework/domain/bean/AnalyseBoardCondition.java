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
 * 统计看板条件
 *
 * @author weipengsen
 */
@Data
@Entity(name = "AnalyseBoardCondition")
@Table(name = "analyse_board_condition")
public class AnalyseBoardCondition extends AbstractCondition {

    private static final long serialVersionUID = -8806675617252968178L;

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
    @JoinColumn(name = "fk_board_id")
    private AnalyseBoardConfig analyseBoardConfig;

    @Column(name = "label")
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_analyse_condition_type")
    private EnumConst enumConstByFlagBlockConditionType;

    @Column(name = "[sql]")
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
