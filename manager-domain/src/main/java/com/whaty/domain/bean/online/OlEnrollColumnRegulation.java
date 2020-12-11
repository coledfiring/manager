package com.whaty.domain.bean.online;

import com.whaty.annotation.UnionUnique;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
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
 * 报名规则
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OlEnrollColumnRegulation")
@Table(name = "ol_enroll_column_regulation")
@UnionUnique(fieldNames = {"name", "siteCode"}, info = "此名称已存在")
public class OlEnrollColumnRegulation extends AbstractSiteBean {

    private static final long serialVersionUID = -768993848552934061L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "identity"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_active")
    private EnumConst enumConstByFlagActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_is_default")
    private EnumConst enumConstByFlagIsDefault;

    @Column(name = "note")
    private String note;

    @Column(name = "site_code")
    private String siteCode;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private Date updateDate;

}
