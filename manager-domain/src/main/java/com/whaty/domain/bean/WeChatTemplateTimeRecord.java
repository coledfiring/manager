package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
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
 * 微信模板消息定时发送记录
 *
 * @author weipengsen
 */
@Data
@Entity(name = "WeChatTemplateTimeRecord")
@Table(name = "wechat_template_time_record"
)
public class WeChatTemplateTimeRecord extends AbstractBean {

    private static final long serialVersionUID = 9204819931874807322L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "identity"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_wechat_template_time_id"
    )
    private WeChatTemplateTime weChatTemplateTime;
    @Column(
            name = "operate_time"
    )
    private Date operateTime;
    @Column(
            name = "early_days"
    )
    private String earlyDays;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "exception_info"
    )
    private String exceptionInfo;
    @Column(
            name = "is_success"
    )
    private String isSuccess;

    public static WeChatTemplateTimeRecord successRecord(String id, String earlyDays, String name) {
        WeChatTemplateTimeRecord record = new WeChatTemplateTimeRecord();
        record.setEarlyDays(earlyDays);
        record.setIsSuccess("1");
        record.setName(name);
        record.setWeChatTemplateTime(new WeChatTemplateTime(id));
        return record;
    }

    public static WeChatTemplateTimeRecord failureRecord(String id, String earlyDays, String name, Exception e) {
        WeChatTemplateTimeRecord record = new WeChatTemplateTimeRecord();
        record.setEarlyDays(earlyDays);
        record.setIsSuccess("0");
        record.setName(name);
        record.setExceptionInfo(e.getMessage());
        record.setWeChatTemplateTime(new WeChatTemplateTime(id));
        return record;
    }

    public WeChatTemplateTimeRecord() {
        this.operateTime = new Date();
    }

}
