package com.whaty.notice.bean;

import com.whaty.domain.bean.SsoUser;
import com.whaty.core.framework.bean.EnumConst;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 个人信息通知与用户关联类
 * @author weipengsen
 */
@Entity(
        name = "PrUserNotice"
)
@Table(
        name = "pr_user_notice"
)
public class PrUserNotice implements Serializable {

    /**
     * id 自增
     */
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "identity"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private Integer id;
    /**
     * 个人信息通知
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FK_NOTICE_ID"
    )
    private PeNotice peNotice;
    /**
     * 用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FK_SSO_USER_ID"
    )
    private SsoUser ssoUser;
    /**
     * 是否已读
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_READED"
    )
    private EnumConst enumConstByFlagReaded;
    /**
     * 是否星标
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_IS_STAR"
    )
    private EnumConst enumConstByFlagIsStar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PeNotice getPeNotice() {
        return peNotice;
    }

    public void setPeNotice(PeNotice peNotice) {
        this.peNotice = peNotice;
    }

    public SsoUser getSsoUser() {
        return ssoUser;
    }

    public void setSsoUser(SsoUser ssoUser) {
        this.ssoUser = ssoUser;
    }

    public EnumConst getEnumConstByFlagReaded() {
        return enumConstByFlagReaded;
    }

    public void setEnumConstByFlagReaded(EnumConst enumConstByFlagReaded) {
        this.enumConstByFlagReaded = enumConstByFlagReaded;
    }

    public EnumConst getEnumConstByFlagIsStar() {
        return enumConstByFlagIsStar;
    }

    public void setEnumConstByFlagIsStar(EnumConst enumConstByFlagIsStar) {
        this.enumConstByFlagIsStar = enumConstByFlagIsStar;
    }
}
