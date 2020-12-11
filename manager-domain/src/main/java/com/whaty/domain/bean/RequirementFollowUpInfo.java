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
import java.util.Objects;

/**
 * 需求跟进信息
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "RequirementFollowUpInfo")
@Table(name = "requirement_follow_up_info")
public class RequirementFollowUpInfo extends AbstractBean {

    private static final long serialVersionUID = 2471627350581288634L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_requirement_info_id"
    )
    private RequirementInfo requirementInfo;
    @Column(
            name = "follow_up_target"
    )
    private String followUpTarget;
    @Column(
            name = "follow_up_time"
    )
    private Date followUpTime;
    @Column(
            name = "follow_up_content"
    )
    private String followUpContent;
    @Column(
            name = "follow_up_way"
    )
    private String followUpWay;
    @Column(
            name = "follow_up_result"
    )
    private String followUpResult;
    @Column(
            name = "note"
    )
    private String note;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @Column(
            name = "update_time"
    )
    private Date updateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_follow_up_user_id"
    )
    private PeManager peManager;

}
