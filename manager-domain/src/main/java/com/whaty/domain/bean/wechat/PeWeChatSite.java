package com.whaty.domain.bean.wechat;

import com.whaty.domain.bean.AbstractSiteBean;
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
 * 微信站点对象
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeWeChatSite")
@Table(name = "wechat_site")
public class PeWeChatSite extends AbstractSiteBean {

    private static final long serialVersionUID = -5490547905990151321L;
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
     * 站点名称
     */
    @Column(
            name = "name"
    )
    private String name;
    /**
     * 域名
     */
    @Column(
            name = "domain"
    )
    private String domain;
    /**
     * 访问路径
     */
    @Column(
            name = "path"
    )
    private String path;
    /**
     * 公众号账号
     */
    @Column(
            name = "public_account"
    )
    private String publicAccount;
    /**
     * 公众号原始id
     */
    @Column(
            name = "public_id"
    )
    private String publicId;
    /**
     * 提供网页获取用户信息的认证服务号站点信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_union_id"
    )
    private PeWeChatSite unionSite;
    @Column(
            name = "input_date"
    )
    private Date inputDate;
    @Column(
            name = "update_date"
    )
    private Date updateDate;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
