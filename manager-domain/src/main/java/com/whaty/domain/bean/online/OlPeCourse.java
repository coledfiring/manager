package com.whaty.domain.bean.online;

import com.whaty.HasAttachFile;
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
 * 课程库
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OlPeCourse")
@Table(name = "ol_pe_course")
public class OlPeCourse extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = 3061834565583682781L;

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
     * 课程名
     */
    @Column(
            name = "name"
    )
    private String name;

    /**
     * 课程码
     */
    @Column(
            name = "code"
    )
    private String code;

    /**
     * 课程简介
     */
    @Column(
            name = "course_intro"
    )
    private String courseIntro;

    /**
     * 课程图片地址
     */
    @Column(
            name = "course_url"
    )
    private String courseUrl;

    /**
     * 课时
     */
    @Column(
            name = "period"
    )
    private String period;
    /**
     * 学分
     */
    @Column(
            name = "credit"
    )
    private String credit;
    /**
     * 价格
     */
    @Column(
            name = "price"
    )
    private Double price;
    /**
     * 折扣价格
     */
    @Column(
            name = "discount_price"
    )
    private Double discountPrice;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    /**
     * 课程类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_online_course_type"
    )
    private EnumConst enumConstByFlagOnlineCourseType;
    /**
     * 是否有效
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_isvalid"
    )
    private EnumConst enumConstByFlagIsvalid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_date"
    )
    private Date createDate;

}
