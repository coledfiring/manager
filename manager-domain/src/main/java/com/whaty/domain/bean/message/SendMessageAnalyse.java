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
 * 发送消息分析
 *
 * @author weipengsen
 */
@Data
@Table(name = "send_message_analyse")
@Entity(name = "SendMessageAnalyse")
public class SendMessageAnalyse extends AbstractSiteBean {

    private static final long serialVersionUID = 5812676871609151322L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @Column(
            name = "group_code"
    )
    private String groupCode;
    @Column(
            name = "group_name"
    )
    private String groupName;
    @Column(
            name = "last_type_name"
    )
    private String lastTypeName;
    @Column(
            name = "last_send_time"
    )
    private Date lastSendTime;
    @Column(
            name = "send_number"
    )
    private Integer sendNumber;
    @Column(
            name = "receive_id"
    )
    private String receiveId;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
