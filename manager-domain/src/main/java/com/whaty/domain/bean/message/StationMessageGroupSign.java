package com.whaty.domain.bean.message;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 站内信组占位符
 *
 * @author weipengsen
 */
@Data
@Entity(name = "StationMessageGroupSign")
@Table(name = "station_message_group_sign")
public class StationMessageGroupSign extends AbstractBean {

    private static final long serialVersionUID = 1958878612682065295L;
    @Id
    @GenericGenerator(name = "idGenerator",strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @ManyToOne
    @JoinColumn(
            name = "fk_message_group_id"
    )
    private StationMessageGroup stationMessageGroup;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "sign"
    )
    private String sign;
    @ManyToOne
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    @Column(
            name = "is_show"
    )
    private String isShow;
    @Column(
            name = "example"
    )
    private String example;

}
