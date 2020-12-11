package com.whaty.domain.bean;

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
 * 培训项目申请
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "TrainingItemApply")
@Table(name = "training_item_apply")
public class TrainingItemApply extends AbstractSiteBean {

    private static final long serialVersionUID = -204516335229879818L;

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
    @JoinColumn(
            name = "fk_training_item_id"
    )
    private TrainingItem trainingItem;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_unit_id"
    )
    private PeUnit peUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_apply_user_id"
    )
    private SsoUser applyBy;
    @Column(
            name = "apply_date"
    )
    private Date applyDate;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
