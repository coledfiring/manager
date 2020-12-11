package com.whaty.domain.bean.message;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import com.whaty.domain.bean.SsoUser;
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
import java.util.Date;

/**
 * 发送消息时间配置
 *
 * @author weipengsen
 */
@Data
@Entity(name = "SendMessageTimeConfig")
@Table(name = "send_message_time_config")
public class SendMessageTimeConfig extends AbstractSiteBean {

    private static final long serialVersionUID = 1252584160332488626L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @Column(
            name = "group_name"
    )
    private String groupName;
    @Column(
            name = "message_type"
    )
    private String messageType;
    @Column(
            name = "message_type_code"
    )
    private String messageTypeCode;
    @Column(
            name = "time"
    )
    private Date time;
    @Column(
            name = "schedule_job_id"
    )
    private String scheduleJobId;
    @Column(
            name = "receive_number"
    )
    private Integer receiveNumber;
    @Column(
            name = "data"
    )
    private String data;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_send_status"
    )
    private EnumConst enumConstByFlagSendStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "update_by"
    )
    private SsoUser updateBy;
    @Column(
            name = "update_date"
    )
    private Date updateDate;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
