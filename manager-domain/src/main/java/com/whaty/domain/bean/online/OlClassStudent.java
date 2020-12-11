package com.whaty.domain.bean.online;

import com.whaty.core.bean.AbstractBean;
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
 * 班级学员关系
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OlClassStudent")
@Table(name = "ol_class_student")
public class OlClassStudent extends AbstractBean {

    private static final long serialVersionUID = 849776604211920606L;

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
            name = "fk_student_id"
    )
    private OlPeStudent olPeStudent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_time"
    )
    private Date createTime;
}
