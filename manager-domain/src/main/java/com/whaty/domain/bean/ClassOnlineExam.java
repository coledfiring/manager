package com.whaty.domain.bean;

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

/**
 * 班级在线考试表
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "ClassOnlineExam")
@Table(name = "class_online_exam")
public class ClassOnlineExam extends AbstractSiteBean {

    private static final long serialVersionUID = -4009349051237333630L;

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
            name = "paper_no"
    )
    private String paperNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_is_open"
    )
    private EnumConst enumConstByFlagIsOpen;
    @Column(
            name = "exam_date"
    )
    private Date examDate;
    @Column(
            name = "start_time"
    )
    private String startTime;
    @Column(
            name = "end_time"
    )
    private String endTime;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_user"
    )
    private SsoUser createUser;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
