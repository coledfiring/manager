package com.whaty.domain.bean.enroll;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 报名人
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "EnrollPerson")
@Table(name = "enroll_person")
public class EnrollPerson extends AbstractBean {

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

    /***
     * 关联用户
     */
    @ManyToOne
    @JoinColumn(name = "fk_enroll_user_id")
    private EnrollUser enrollUser;

    /**
     * 是否默认
     */
    @Column(name = "is_default")
    private String isDefault;
}
