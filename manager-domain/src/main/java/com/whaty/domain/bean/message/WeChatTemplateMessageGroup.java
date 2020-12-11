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
 * 微信模板消息组
 *
 * @author weipengsen
 */
@Data
@Entity(name = "WeChatTemplateMessageGroup")
@Table(name = "wechat_template_message_group")
public class WeChatTemplateMessageGroup extends AbstractBean {

    private static final long serialVersionUID = 6136639810982319281L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "code"
    )
    private String code;
    @Column(
            name = "filter_sql"
    )
    private String filterSql;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    @Column(
            name = "data_sql"
    )
    private String dataSql;

}
