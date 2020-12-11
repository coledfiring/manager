package com.whaty.domain.bean.evaluate;

import com.whaty.core.bean.AbstractBean;
import com.whaty.domain.bean.SsoUser;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 督导计划
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "SupervisionPlan")
@Table(name = "supervision_plan")
public class SupervisionPlan extends AbstractBean {

    private static final long serialVersionUID = -7706344724192097698L;

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
            name = "fk_sso_user_id"
    )
    private SsoUser ssoUser;
    @Column(
            name = "item_id"
    )
    private String item_id;
    @Column(
            name = "namespace"
    )
    private String namespace;
    @Column(
            name = "is_finish"
    )
    private String isFinish;
    @Column(
            name = "is_delete"
    )
    private String isDelete;
    @Column(
            name = "create_time"
    )
    private Date createTime;

}
