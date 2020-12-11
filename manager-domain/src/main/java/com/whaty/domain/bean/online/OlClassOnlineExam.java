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
 * 班级在线考试
 *
 * @author weipengsen
 */
@Data
@Entity(name = "OlClassOnlineExam")
@Table(name = "ol_class_online_exam")
public class OlClassOnlineExam extends AbstractBean {

    private static final long serialVersionUID = -3783155585910165352L;

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
    private OlPeClass olPeClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_course_id"
    )
    private OlPeCourse olPeCourse;
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
            name = "start_time"
    )
    private Date startTime;
    @Column(
            name = "end_time"
    )
    private Date endTime;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_user"
    )
    private SsoUser createUser;
}
