package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.PeManager;
import com.whaty.domain.bean.SsoUser;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * author weipengsen  Date 2020/7/12
 */
@Data
@Entity(name = "PeCheckRecord")
@Table(name = "pe_check_record")
public class PeCheckRecord extends AbstractBean {

    private static final long serialVersionUID = -6463378810932659533L;
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
            name = "fk_check_manager"
    )
    private PeManager peManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_check_point"
    )
    private PeCheckPoint peCheckPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_check_level"
    )
    private EnumConst enumConstByFlagCheckLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_hygiene_level"
    )
    private EnumConst enumConstByFlagHygieneLevel;

    @Column(
            name = "device_note"
    )
    private String deviceNote;

    @Column(
            name = "problem_note"
    )
    private String problemNote;

    @Column(
            name = "create_time"
    )
    private Date createTime;
}
