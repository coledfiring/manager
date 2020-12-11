package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 易耗品物品
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "PeConsumableItem")
@Table(name = "pe_consumable_item")
public class PeConsumableItem extends AbstractSiteBean {

    private static final long serialVersionUID = -7403733451689850685L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 物品名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否有效
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_is_valid")
    private EnumConst enumConstByFlagIsValid;

    /**
     * 物品单价
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 物品种类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_consumable_type")
    private EnumConst enumConstByFlagConsumableType;

    /**
     * 物品备注
     */
    @Column(name = "note")
    private String note;

    @Column(name = "site_code")
    private String siteCode;
}
