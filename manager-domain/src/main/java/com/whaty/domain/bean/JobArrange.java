package com.whaty.domain.bean;

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
 * 工作安排管理
 *
 * @author shangyu
 */
@Data
@Entity(name = "JobArrange")
@Table(name = "job_arrange")
public class JobArrange extends AbstractSiteBean {

    /**
     * 主键ID
     */
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    /**
     * 安排时间
     */
    @Column(name = "arrange_time")
    private Date arrangeTime;

    /**
     * 相关人员
     */
    @Column(name = "related_person")
    private String relatedPerson;

    /**
     * 是否公开
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_is_public"
    )
    private EnumConst enumConstByFlagIsPublic;

    /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 创建人单位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_unit_id"
    )
    private PeUnit peUnit;

    /**
     * 工作安排内容
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_arrange_extend_id"
    )
    private JobArrangeExtend arrangeExtend;

    /**
     * 站点ID
     */
    @Column(name = "site_code")
    private String siteCode;

}
