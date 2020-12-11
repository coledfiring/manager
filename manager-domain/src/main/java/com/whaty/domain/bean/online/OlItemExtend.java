package com.whaty.domain.bean.online;

import com.whaty.core.bean.AbstractBean;
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

/**
 * ol培训扩展信息
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OlItemExtend")
@Table(name = "ol_item_extend")
public class OlItemExtend extends AbstractBean {

    private static final long serialVersionUID = -1250287521160421784L;
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
            name = "content"
    )
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_online_item_extend_type"
    )
    private EnumConst enumConstByFlagOnlineItemExtendType;
    @Column(
            name = "fk_item_id"
    )
    private String itemId;
    @Column(
            name = "namespace"
    )
    private String namespace;
}
