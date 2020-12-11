package com.whaty.domain.bean.message;

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
 * 微信模板消息字段
 *
 * @author weipengsen
 */
@Data
@Entity(name = "WeChatTemplateMessageColumn")
@Table(name = "wechat_template_message_column")
public class WeChatTemplateMessageColumn extends AbstractBean {

    private static final long serialVersionUID = 2560130801936509888L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @Column(
            name = "label"
    )
    private String label;
    @Column(
            name = "sign"
    )
    private String sign;
    @Column(
            name = "is_dynamic"
    )
    private String isDynamic;
    @Column(
            name = "template_text"
    )
    private String templateText;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_template_group_id"
    )
    private WeChatTemplateMessageGroup weChatTemplateMessageGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;

}
