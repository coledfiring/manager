package com.whaty.domain.bean;

import com.whaty.constant.SiteConstant;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.framework.reference.ValidatePolicy;
import com.whaty.framework.reference.annotation.ValidateReference;
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
 * 报名批次管理
 *
 * @author shanshuai
 */
@Data
@Entity(name = "ExamBatch")
@Table(name = "exam_batch")
@ValidateReference(
        siteCodes = SiteConstant.SERVICE_SITE_CODE_GZUCMPX,
        validatePolicy = ValidatePolicy.ONLY_POLICY
)
public class ExamBatch extends AbstractSiteBean {

    private static final long serialVersionUID = -78613740844972600L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_active")
    private EnumConst enumConstByFlagActive;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "site_code")
    private String siteCode;

    @Column(name = "note")
    private String note;

    @Column(name = "exam_name")
    private String examName;

}
