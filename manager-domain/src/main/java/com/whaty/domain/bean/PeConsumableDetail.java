package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 易耗品管理实体类
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "PeConsumableDetail")
@Table(name = "pe_consumable_detail")
public class PeConsumableDetail extends AbstractBean {

    private static final long serialVersionUID = 1911812173194655121L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 易耗品申请id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_consumable_id")
    private PeConsumable peConsumable;

    /**
     * 物品种类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_consumable_item_id")
    private PeConsumableItem peConsumableItem;

    /**
     * 使用数量
     */
    @Column(name = "use_number")
    private int useNumber;


}
