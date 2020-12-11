package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.HasAttachFile;
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
 * 课程库
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeCourse")
@Table(name = "pe_course")
public class PeCourse extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = -711051375503950696L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    /**
     * 课程名
     */
    @Column(
            name = "name"
    )
    private String name;

    /**
     * 课程码
     */
    @Column(
            name = "code"
    )
    private String code;

    /**
     * 课程简介
     */
    @Column(
            name = "course_intro"
    )
    private String courseIntro;

    /**
     * 课程图片地址
     */
    @Column(
            name = "course_url"
    )
    private String courseUrl;

    @Column(
            name = "site_code"
    )
    private String siteCode;

    /**
     *培训对象
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_training_target"
    )
    private EnumConst enumConstByFlagTrainingTarget;

    /**
     * 课程类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_course_type"
    )
    private EnumConst enumConstByFlagCourseType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_teacher_id"
    )
    private PeTeacher peTeacher;
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
