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
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 班级课程表
 *
 * @author weipengsen
 */
@Data
@Entity(name = "ClassCourseTimetable")
@Table(name = "class_course_timetable")
public class ClassCourseTimetable extends AbstractBean {

    private static final long serialVersionUID = -30847593027078093L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_class_course_id"
    )
    private ClassCourse classCourse;
    @Column(
            name = "training_date"
    )
    private Date trainingDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_course_time_id"
    )
    private CourseTime courseTime;
    @Column(
            name = "note"
    )
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_courseware_id"
    )
    private AttachFile courseware;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_place_id"
    )
    private PePlace pePlace;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "arrange_place_user"
    )
    private SsoUser arrangePlaceUser;
    @Column(
            name = "arrange_place_time"
    )
    private Date arrangePlaceTime;
    @Column(
            name = "teacher_fee"
    )
    private BigDecimal teacherFee;

    @Column(
            name = "start_time"
    )
    private String startTime;
    @Column(
            name = "end_time"
    )
    private String endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_course_type"
    )
    private EnumConst enumConstByFlagCourseType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_place_type"
    )
    private EnumConst enumConstByFlagPlaceType;
    @Transient
    private PeCourse peCourse;
    @Transient
    private PeClass peClass;
    @Transient
    private String placeName;

}
