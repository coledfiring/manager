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
 * 班级课程关联
 *
 * @author weipengsen
 */
@Data
@Entity(name = "ClassCourse")
@Table(name = "class_course")
public class ClassCourse extends AbstractBean {

    private static final long serialVersionUID = -839245331285224814L;
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
            name = "fk_class_id"
    )
    private PeClass peClass;
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
