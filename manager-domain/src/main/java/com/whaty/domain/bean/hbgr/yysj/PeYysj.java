package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * author weipengsen  Date 2020/6/17
 */
@Data
@Entity(name = "PeYysj")
@Table(name = "pe_yysj")
public class PeYysj  extends AbstractSiteBean {

    private static final long serialVersionUID = 3108939907691969405L;
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
            name = "yysj_date"
    )
    private Date yysjDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;

    @Column(
            name = "site_code"
    )
    private String siteCode;
}
