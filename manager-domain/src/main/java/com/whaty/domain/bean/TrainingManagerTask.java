package com.whaty.domain.bean;

import com.whaty.HasAttachFile;
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
 * 培训管理人员任务
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "TrainingManagerTask")
@Table(name = "training_manager_task")
public class TrainingManagerTask extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = 6016593233621227549L;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_task_type"
    )
    private EnumConst enumConstByFlagTaskType;
    @Column(
            name = "start_time"
    )
    private Date startTime;
    @Column(
            name = "end_time"
    )
    private Date endTime;
    @Column(
            name = "charge_persons"
    )
    private String chargePersons;
    @Column(
            name = "copy_persons"
    )
    private String copyPersons;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_priority_level"
    )
    private EnumConst enumConstByFlagPriorityLevel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_notify_way"
    )
    private EnumConst enumConstByFlagNotifyWay;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_training_task_status"
    )
    private EnumConst enumConstByFlagTrainingTaskStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_manager"
    )
    private PeManager createManager;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "update_manager"
    )
    private PeManager updateManager;
    @Column(
            name = "note"
    )
    private String note;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
