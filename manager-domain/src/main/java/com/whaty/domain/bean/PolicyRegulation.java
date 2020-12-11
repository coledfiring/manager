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
 * 政治法规管理
 *
 * @author shangyu
 */
@Data
@Entity(name = "PolicyRegulation")
@Table(name = "policy_regulation")
public class PolicyRegulation extends AbstractSiteBean {

    private static final long serialVersionUID = 8757350752437386435L;

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
     * 文件名
     */
    @Column(name = "name")
    private String name;

    /**
     * 文号
     */
    @Column(name = "code")
    private String code;

    /**
     * 发布者
     */
    @Column(name = "publish_by")
    private String publishBy;

    /**
     * 执行时间
     */
    @Column(name = "execute_time")
    private Date executeTime;

    /**
     * 文件类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_file_type"
    )
    private EnumConst enumConstByFlagFileType;

    /**
     * 是否有效
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;

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
     * 文件内容
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_file_extend_id"
    )
    private PolicyRegulationExtend fileExtend;

    /**
     * 站点编号
     */
    @Column(name = "site_code")
    private String siteCode;

}
