package com.whaty.domain.bean;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.HasAttachFile;
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

/**
 * 培训证书
 *
 * @author 索强强
 */
@Data
@Table(name = "training_certificate")
@Entity(name = "TrainingCertificate")
public class TrainingCertificate extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = 3636612822704975940L;
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
            name = "flag_training_type"
    )
    private EnumConst enumConstByFlagTrainingType;
    @Column(
            name = "site_code"
    )
    private String siteCode;

}
