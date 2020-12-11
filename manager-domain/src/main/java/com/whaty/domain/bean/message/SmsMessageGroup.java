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
 * 短信信息组管理
 *
 * @author weipengsen
 */
@Data
@Entity(name = "SmsMessageGroup")
@Table(name = "sms_message_group")
public class SmsMessageGroup extends AbstractBean {

    private static final long serialVersionUID = 4180861639169110742L;
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
            name = "data_sql"
    )
    private String dataSql;
    @Column(
            name = "filter_sql"
    )
    private String filterSql;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;

}
