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
 * 统计看板与块关联
 *
 * @author weipengsen
 */
@Data
@Entity(name = "AnalyseBoardBlock")
@Table(name = "analyse_board_block")
public class AnalyseBoardBlock extends AbstractBean {

    private static final long serialVersionUID = 5344854459941985816L;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_block_id")
    private AnalyseBlockConfig analyseBlockConfig;

    @Column(name = "serial")
    private Integer serial;

    @Column(name = "col_num")
    private Integer colNum;

}
