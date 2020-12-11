package com.whaty.domain.bean.online;

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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生在线考试成绩表
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OlStudentOnlineExamScore")
@Table(name = "ol_student_online_exam_score")
public class OlStudentOnlineExamScore extends AbstractBean {

    private static final long serialVersionUID = -1615662969777896430L;
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
            name = "fk_class_online_exam_id"
    )
    private OlClassOnlineExam olClassOnlineExam;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_student_id"
    )
    private OlPeStudent olPeStudent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_score_publish"
    )
    private EnumConst enumConstByFlagScorePublish;
    @Column(
            name = "update_time"
    )
    private Date updateTime;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @Column(
            name = "score"
    )
    private BigDecimal score;

}
