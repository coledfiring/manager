package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 项目评审实体类
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeReview")
@Table(name = "pe_review")
public class PeReview extends AbstractSiteBean {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 项目名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 项目背景
     */
    @Column(name = "background")
    private String background;

    /**
     * 评审专家
     */
    @Column(name = "review_expert")
    private String reviewExpert;


    /**
     * 评审要求
     */
    @Column(name = "review_requirement")
    private String reviewRequirement;
    /**
     * 注意事项
     */

    @Column(name = "note")
    private String note;

    /**
     * 项目状态 常量
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_project_status"
    )
    private EnumConst enumConstByFlagProjectStatus;

    /**
     * 评审意见
     */
    @Column(name = "opinion")
    private String opinion;

    /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_user"
    )
    private SsoUser createUser;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 所属单位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_unit_id"
    )
    private PeUnit unit;

    /**
     * sitecode
     */
    @Column(name = "site_code")
    private String siteCode;

    @Transient
    private List<String> reviewExpertList;

    @Transient
    private String projectStatus;

    @Transient
    private String principal;

    @Transient
    private List<String> principalList;
}
