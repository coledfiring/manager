package com.whaty.domain.bean;

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
 * 培训项目扩展
 *
 * @author weipengsen
 */
@Data
@Entity(name = "TrainingItemExtend")
@Table(name = "training_item_extend")
public class TrainingItemExtend extends AbstractBean {

    private static final long serialVersionUID = 153080425704829985L;
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
            name = "training_content"
    )
    private String trainingContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_item_extend_type"
    )
    private EnumConst enumConstByFlagItemExtendType;
    @Column(
            name = "fk_item_id"
    )
    private String itemId;
    @Column(
            name = "namespace"
    )
    private String namespace;
}
