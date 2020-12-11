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
 * 发送消息类型
 *
 * @author weipengsen
 */
@Data
@Entity(name = "SendMessageType")
@Table(name = "send_message_type")
public class SendMessageType extends AbstractBean {

    private static final long serialVersionUID = 4200423698808211953L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_send_message_group_id"
    )
    private SendMessageGroup sendMessageGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_message_type"
    )
    private EnumConst enumConstByFlagMessageType;
    @Column(
            name = "message_code"
    )
    private String messageCode;

}
