package com.whaty.domain.bean.evaluate;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
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
 * 综合评价
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OverallEvaluate")
@Table(name = "overall_evaluate")
public class OverallEvaluate extends AbstractSiteBean {

    private static final long serialVersionUID = -9120512690570321829L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    @Column(
            name = "code"
    )
    private String code;
    @Column(
            name = "name"
    )
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_user"
    )
    private SsoUser createUser;
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_isvalid"
    )
    private EnumConst enumConstByFlagIsvalid;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
