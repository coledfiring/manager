package com.whaty.domain.bean.online;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.SsoUser;
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
 * 教师课程
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OlCourseTeacher")
@Table(name = "ol_course_teacher")
public class OlCourseTeacher extends AbstractBean {

    private static final long serialVersionUID = -6368794561332712683L;
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
    private OlPeCourse olPeCourse;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_teacher_id"
    )
    private OlPeTeacher olPeTeacher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_online_course_teacher_type"
    )
    private EnumConst enumConstByFlagOnlineCourseTeacherType;
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
