package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.message.WeChatTemplateMessageSite;
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
 * 微信模板消息定时推送
 *
 * @author weipengsen
 */
@Data
@Entity(name = "WeChatTemplateTime")
@Table(name = "wechat_template_time")
public class WeChatTemplateTime extends AbstractBean {

    private static final long serialVersionUID = -4051886535725967925L;
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
            name = "name"
    )
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_wechat_template_site_id"
    )
    private WeChatTemplateMessageSite weChatTemplateMessageSite;
    @Column(
            name = "data_sql"
    )
    private String dataSql;
    @Column(
            name = "early_days"
    )
    private String earlyDays;
    @Column(
            name = "note"
    )
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_is_show"
    )
    private EnumConst enumConstByFlagIsShow;

    public WeChatTemplateTime() {
    }

    public WeChatTemplateTime(String id) {
        this.id = id;
    }

}
