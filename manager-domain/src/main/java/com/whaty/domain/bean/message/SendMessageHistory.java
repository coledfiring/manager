package com.whaty.domain.bean.message;

import com.whaty.domain.bean.AbstractSiteBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 发送消息历史
 *
 * @author weipengsen
 */
@Data
@Entity(name = "SendMessageHistory")
@Table(name = "send_message_history")
public class SendMessageHistory extends AbstractSiteBean {

    private static final long serialVersionUID = 4492589617338362671L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @Column(
            name = "receive_id"
    )
    private String receiveId;
    @Column(
            name = "group_code"
    )
    private String groupCode;
    @Column(
            name = "group_name"
    )
    private String groupName;
    @Column(
            name = "type_name"
    )
    private String typeName;
    @Column(
            name = "type_code"
    )
    private String typeCode;
    @Column(
            name = "message_content"
    )
    private String messageContent;
    @Column(
            name = "send_time"
    )
    private Date sendTime;
    @Column(
            name = "send_user"
    )
    private String sendUser;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
