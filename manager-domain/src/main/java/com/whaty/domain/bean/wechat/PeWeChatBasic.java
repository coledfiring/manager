package com.whaty.domain.bean.wechat;

import com.whaty.core.bean.AbstractBean;
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
 * 微信基础配置
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeWeChatBasic")
@Table(name = "wechat_basic")
public class PeWeChatBasic extends AbstractBean {

    private static final long serialVersionUID = -4496304442680466869L;
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
            name = "fk_wechat_site_id"
    )
    private PeWeChatSite peWeChatSite;
    @Column(
            name = "appId"
    )
    private String appId;
    @Column(
            name = "appSecret"
    )
    private String appSecret;
    @Column(
            name = "token"
    )
    private String token;
    @Column(
            name = "menu"
    )
    private String menu;
    @Column(
            name = "input_date"
    )
    private Date inputDate;
    @Column(
            name = "update_date"
    )
    private Date updateDate;

}
