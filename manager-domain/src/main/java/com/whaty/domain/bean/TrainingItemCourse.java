package com.whaty.domain.bean;

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
import java.util.Date;

/**
 * 培训项目与课程关联
 *
 * @author weipengsen
 */
@Data
@Entity(name = "TrainingItemCourse")
@Table(name = "training_item_course")
public class TrainingItemCourse extends AbstractBean {

    private static final long serialVersionUID = -3579912457078775903L;
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
            name = "fk_course_id"
    )
    private PeCourse peCourse;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_training_item_id"
    )
    private TrainingItem trainingItem;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createDate;

}
