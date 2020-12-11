package com.whaty.domain.bean;

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
import java.util.Date;

/**
 * 附件操作记录
 *
 * @author weipengsen
 */
@Data
@Entity(name = "AttachFileOperateRecord")
@Table(name = "attach_file_operate_record")
public class AttachFileOperateRecord extends AbstractBean {

    private static final long serialVersionUID = 1416575940904717492L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "namespace"
    )
    private String namespace;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_file_operate_type"
    )
    private EnumConst enumConstByFlagFileOperateType;
    @Column(
            name = "operate_time"
    )
    private Date operateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "operate_user"
    )
    private SsoUser operateUser;

}
