package com.whaty.domain.bean.hbgr.yysj;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * author weipengsen  Date 2020/6/19
 */
@Data
@Entity(name = "PeDeviceType")
@Table(name = "pe_device_type")
public class PeDeviceType extends AbstractBean {

    private static final long serialVersionUID = 6613165191080706418L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @Column(
            name = "name"
    )
    private String name;

    @Column(
            name = "code"
    )
    private String code;
}
