package com.whaty.domain.bean.enroll;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 报名系统用户联系人表
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "EnrollUserLinkman")
@Table(name = "enroll_user_linkman")
public class EnrollUserLinkman extends AbstractBean {

    private static final long serialVersionUID = -5593733471880816830L;

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    private String id;

    /**
     * 姓名
     */
    @Column(name = "true_name")
    private String trueName;

    /**
     * 关系
     */
    @Column(name = "relative")
    private String relative;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 序号
     */
    @Column(name = "serial_number")
    private Integer serialNumber;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /***
     * 关联用户
     */
    @ManyToOne
    @JoinColumn(name = "fk_enroll_user_id")
    private EnrollUser enrollUser;
}
