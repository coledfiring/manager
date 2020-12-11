package com.whaty.domain.bean.message;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.PeWebSite;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 邮件发送组与站点关联
 */
@Data
@Entity(
        name = "EmailMessageSite"
)
@Table(
        name = "email_message_site"
)
public class EmailMessageSite extends AbstractBean {

    private static final long serialVersionUID = 1792947548378792071L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_web_site_id"
    )
    private PeWebSite peWebSite;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_email_message_group_id"
    )
    private EmailMessageGroup emailMessageGroup;

}
