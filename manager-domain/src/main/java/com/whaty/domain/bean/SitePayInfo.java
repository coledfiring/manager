package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.PeWebSite;
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
 * 站点支付信息表
 */
@Data
@Entity(name = "SitePayInfo")
@Table(name = "site_pay_info")
public class SitePayInfo extends AbstractBean {

    private static final long serialVersionUID = -2731752998562638941L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "fk_web_site_id"
    )
    private PeWebSite peWebSite;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flag_pay_type"
    )
    private EnumConst enumConstByFlagPayType;
    @Column(
            name = "app_id"
    )
    private String appId;
    @Column(
            name = "alipay_secret_key"
    )
    private String alipaySecretKey;
    @Column(
            name = "alipay_public_key"
    )
    private String alipayPublicKey;
    @Column(
            name = "alipay_gateway"
    )
    private String alipayGateway;
    @Column(
            name = "alipay_return_url"
    )
    private String alipayReturnUrl;
    @Column(
            name = "alipay_notify_url"
    )
    private String alipayNotifyUrl;
    @Column(
            name = "wechat_mch_id"
    )
    private String wechatMchId;
    @Column(
            name = "wechat_pay_url"
    )
    private String wechatPayUrl;
    @Column(
            name = "wechat_close_url"
    )
    private String wechatCloseUrl;
    @Column(
            name = "wechat_api_key"
    )
    private String wechatApiKey;
    @Column(
            name = "wechat_notify_url"
    )
    private String wechatNotifyUrl;

}
