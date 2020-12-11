package com.whaty.domain.bean;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 接待管理
 *
 * @author weipengsen
 */
@Data
@Entity(name = "PeReceived")
@Table(name = "pe_received")
public class PeReceived extends AbstractSiteBean {

    private static final long serialVersionUID = -546912532608076680L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 登记人员
     */
    @Column(
            name = "regist_user"
    )
    private String registUser;

    /**
     * 站点码
     */
    @Column(
            name = "site_code"
    )
    private String siteCode;

    /**
     * 登记时间
     */
    @Column(
            name = "regist_time"
    )
    private Date registTime;

    /**
     * 接待事由
     */
    @Column(
            name = "received_by"
    )
    private String receivedBy;

    /**
     * 接待时间
     */
    @Column(
            name = "received_time"
    )
    private String receivedTime;

    /**
     * 接待用户
     */
    @Column(
            name = "received_user"
    )
    private String receivedUser;

    /**
     * 参加人员
     */
    @Column(
            name = "participate_user"
    )
    private String participateUser;

    /**
     * 人数
     */
    @Column(
            name = "people_number"
    )
    private String peopleNumber;

    /**
     * 接待要求
     */
    @Column(
            name = "received_require"
    )
    private String receivedRequire;

    /**
     * 备注
     */
    @Column(
            name = "note"
    )
    private String note;

    /**
     * 单位 
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_unit_id"
    )
    private PeUnit peUnit;

    /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private SsoUser createBy;

    @Transient
    private List<String> receivedUserList;
}
