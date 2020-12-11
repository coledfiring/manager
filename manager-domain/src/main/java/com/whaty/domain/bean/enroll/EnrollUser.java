package com.whaty.domain.bean.enroll;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 报名系统用户表
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "EnrollUser")
@Table(name = "enroll_user")
public class EnrollUser extends AbstractSiteBean {

    private static final long serialVersionUID = 7936492548810853653L;
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    /**
     * 身份证号
     */
    @Column(name = "card_no")
    private String cardNo;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 姓名
     */
    @Column(name = "true_name")
    private String trueName;

    /**
     * 性别
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_gender")
    private EnumConst enumConstByFlagGender;

    /**
     * 出生年月
     */
    @Column(name = "birthday")
    private String birthday;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 站点编号
     */
    @Column(name = "site_code")
    private String siteCode;

    /**
     * 是否初始化
     */
    @Column(name = "is_init")
    private String isInit;
}
