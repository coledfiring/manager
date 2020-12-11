package com.whaty.notice.bean;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import com.whaty.domain.bean.SsoUser;
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
import java.util.Objects;

/**
 * 个人中心通知类
 * @author weipengsen
 */
@Entity(
        name = "PeNotice"
)
@Table(
        name = "pe_notice"
)
public class PeNotice extends AbstractSiteBean {

    /**
     * id自增
     */
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "identity"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    /**
     * 通知内容
     */
    @Column(
            name = "CONTENT"
    )
    private String content;
    /**
     * 通知创建时间
     */
    @Column(
            name = "CREATE_TIME"
    )
    private Date createTime;
    /**
     * 操作人ip
     */
    @Column(
            name = "OPERATE_IP"
    )
    private String operateIP;
    /**
     * 请求url
     */
    @Column(
            name = "REQUEST_URL"
    )
    private String requestURL;
    /**
     * 通知范围，单播广播
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_SCOPE_TYPE"
    )
    private EnumConst enumConstByFlagScopeType;
    /**
     * 通知类型，用户操作
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLAG_NOTICE_TYPE"
    )
    private EnumConst enumConstByFlagNoticeType;
    /**
     * 发送者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FK_SEND_USER"
    )
    private SsoUser sendUser;
    @Column(
            name = "site_code"
    )
    private String siteCode;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperateIP() {
        return operateIP;
    }

    public void setOperateIP(String operateIP) {
        this.operateIP = operateIP;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public EnumConst getEnumConstByFlagScopeType() {
        return enumConstByFlagScopeType;
    }

    public void setEnumConstByFlagScopeType(EnumConst enumConstByFlagScopeType) {
        this.enumConstByFlagScopeType = enumConstByFlagScopeType;
    }

    public EnumConst getEnumConstByFlagNoticeType() {
        return enumConstByFlagNoticeType;
    }

    public void setEnumConstByFlagNoticeType(EnumConst enumConstByFlagNoticeType) {
        this.enumConstByFlagNoticeType = enumConstByFlagNoticeType;
    }

    public SsoUser getSendUser() {
        return sendUser;
    }

    public void setSendUser(SsoUser sendUser) {
        this.sendUser = sendUser;
    }

    @Override
    public String getSiteCode() {
        return siteCode;
    }

    @Override
    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeNotice peNotice = (PeNotice) o;
        return Objects.equals(id, peNotice.id) &&
                Objects.equals(content, peNotice.content) &&
                Objects.equals(createTime, peNotice.createTime) &&
                Objects.equals(operateIP, peNotice.operateIP) &&
                Objects.equals(requestURL, peNotice.requestURL) &&
                Objects.equals(enumConstByFlagScopeType, peNotice.enumConstByFlagScopeType) &&
                Objects.equals(enumConstByFlagNoticeType, peNotice.enumConstByFlagNoticeType) &&
                Objects.equals(sendUser, peNotice.sendUser) &&
                Objects.equals(siteCode, peNotice.siteCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, createTime, operateIP, requestURL, enumConstByFlagScopeType,
                enumConstByFlagNoticeType, sendUser, siteCode);
    }

    @Override
    public String toString() {
        return "PeNotice{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", operateIP='" + operateIP + '\'' +
                ", requestURL='" + requestURL + '\'' +
                ", enumConstByFlagScopeType=" + enumConstByFlagScopeType +
                ", enumConstByFlagNoticeType=" + enumConstByFlagNoticeType +
                ", sendUser=" + sendUser +
                ", siteCode='" + siteCode + '\'' +
                '}';
    }
}
