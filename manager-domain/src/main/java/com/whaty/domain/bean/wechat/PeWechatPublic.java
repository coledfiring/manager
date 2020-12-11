package com.whaty.domain.bean.wechat;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 微信公众号允许对接域名
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeWechatPublic")
@Table(name = "pe_wechat_public")
public class PeWechatPublic extends AbstractBean {

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_is_valid"
    )
    private EnumConst enumConstByFlagIsValid;

    @Column(
            name = "domain"
    )
    private String domain;

    @Column(
            name = "name"
    )
    private String name;

    @Column(
            name = "note"
    )
    private String note;

    @Column(
            name = "ip"
    )
    private String ip;


}