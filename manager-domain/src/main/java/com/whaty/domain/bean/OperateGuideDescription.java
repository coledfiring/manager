package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.PeWebSite;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 操作指导描述
 * @author weipengsen
 */
@Data
@Entity(name = "OperateGuideDescription")
@Table(name = "operate_guide_description")
public class OperateGuideDescription extends AbstractBean {

    private static final long serialVersionUID = -2706612623143570647L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id",
            unique = true,
            nullable = false
    )
    private String id;
    /**
     * 名称
     */
    @Column(
        name = "name"
    )
    private String name;
    /**
     * 图标
     */
    @Column(
            name = "icon"
    )
    private String icon;
    /**
     * 描述
     */
    @Column(
            name = "description"
    )
    private String description;
    /**
     * 对应操作流程图
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_flow_config_id"
    )
    private FlowConfig flowConfig;
    /**
     * 是否有效
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    /**
     * 站点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_web_site_id"
    )
    private PeWebSite peWebSite;
    /**
     * 顺序
     */
    @Column(
            name = "serial_number"
    )
    private Long serialNumber;

}
