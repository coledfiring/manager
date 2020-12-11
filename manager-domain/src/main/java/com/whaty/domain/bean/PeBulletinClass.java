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

/**
 * 班级公告
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeBulletinClass")
@Table(name = "PE_BULLETIN_CLASS")
public class PeBulletinClass extends AbstractBean {

    private static final long serialVersionUID = -3367745056136682583L;
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
            name = "FLAG_ISVALID"
    )
    private EnumConst enumConstByFlagIsvalid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_ISTOP"
    )
    private EnumConst enumConstByFlagIstop;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FK_MANAGER_ID"
    )
    private PeManager peManager;
    @Column(
            name = "TITLE"
    )
    private String title;
    @Column(
            name = "PUBLISH_DATE"
    )
    private Date publishDate;
    @Column(
            name = "UPDATE_DATE"
    )
    private Date updateDate;
    @Column(
            name = "NOTE"
    )
    private String note;
    @Column(
            name = "SCOPE_STRING"
    )
    private String scopeString;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FK_CLASS_ID"
    )
    private PeClass peClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FK_TEACHER_ID"
    )
    private PeTeacher peTeacher;

}
