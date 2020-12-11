package com.whaty.domain.bean.evaluate;

import com.whaty.core.bean.AbstractBean;
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
 * 综合评价选项
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OverallEvaluateOption")
@Table(name = "overall_evaluate_option")
public class OverallEvaluateOption extends AbstractBean {

    private static final long serialVersionUID = 1181440441518536438L;

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
            name = "name"
    )
    private String name;
    @Column(
            name = "serial_number"
    )
    private Integer serialNumber;
    @Column(
            name = "rel_id"
    )
    private String relId;
    @Column(
            name = "note"
    )
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_evaluate_id"
    )
    private OverallEvaluate overallEvaluate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_option_type"
    )
    private EnumConst enumConstByFlagOptionType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_required"
    )
    private EnumConst enumConstByFlagRequired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_question_type"
    )
    private EnumConst enumConstByFlagQuestionType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_isvalid"
    )
    private EnumConst enumConstByFlagIsvalid;
}
