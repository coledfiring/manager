package com.whaty.domain.bean.hbgr.warning;

import com.whaty.domain.bean.AbstractSiteBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * author weipengsen  Date 2020/6/18
 */
@Data
@Entity(name = "PeWarningRecord")
@Table(name = "pe_warning_record")
public class PeWarningRecord extends AbstractSiteBean {

    private static final long serialVersionUID = 1561557333330117671L;
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
            name = "site_code"
    )
    private String siteCode;

    @Column(
            name = "name"
    )
    private String name;

    @Column(
            name = "contant"
    )
    private String contant;

    @Column(
            name = "create_date"
    )
    private Date createDate;

    @Column(
            name = "reason"
    )
    private String reason;

}
