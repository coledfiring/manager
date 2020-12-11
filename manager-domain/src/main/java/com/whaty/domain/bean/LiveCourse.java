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
import java.util.Date;
import java.util.Objects;

/**
 * 直播课
 *
 * @author weipengsen
 */
@Data
@Entity(name = "LiveCourse")
@Table(name = "live_course")
public class LiveCourse extends AbstractBean {

    private static final long serialVersionUID = -8209125071765244610L;
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
            name = "fk_class_id"
    )
    private PeClass peClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_course_id"
    )
    private PeCourse peCourse;
    @Column(
            name = "exam_paper_code"
    )
    private String examPaperCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    @Column(
            name = "start_time"
    )
    private Date startTime;
    @Column(
            name = "end_time"
    )
    private Date endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_teacher_id"
    )
    private PeTeacher peTeacher;
    @Column(
            name = "live_url"
    )
    private String liveUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "update_by"
    )
    private SsoUser updateBy;
    @Column(
            name = "update_date"
    )
    private Date updateDate;

}
