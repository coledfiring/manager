package com.whaty.domain.bean.message;

import com.whaty.core.bean.AbstractBean;
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
 * 微信模板消息站点关联
 *
 * @author weipengsen
 */
@Data
@Entity(name = "WeChatTemplateMessageSite")
@Table(name = "wechat_template_message_site")
public class WeChatTemplateMessageSite extends AbstractBean {

    private static final long serialVersionUID = 4431868877710638132L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_template_group_id"
    )
    private WeChatTemplateMessageGroup weChatTemplateMessageGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_web_site_id"
    )
    private PeWebSite peWebSite;
    @Column(
            name = "template_id"
    )
    private String templateId;

}
