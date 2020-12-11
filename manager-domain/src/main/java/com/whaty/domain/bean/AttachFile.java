package com.whaty.domain.bean;

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
 * 附件
 *
 * @author weipengsen
 */
@Data
@Entity(name = "AttachFile")
@Table(name = "attach_file")
public class AttachFile extends AbstractSiteBean {

    private static final long serialVersionUID = 4951126440823822637L;
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
    @Column(
            name = "url"
    )
    private String url;
    @Column(
            name = "file_size"
    )
    private String fileSize;
    @Column(
            name = "link_id"
    )
    private String linkId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_attach_type"
    )
    private EnumConst enumConstByFlagAttachType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "create_by"
    )
    private SsoUser createBy;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @Column(
            name = "site_code"
    )
    private String siteCode;
    @Column(
            name = "idocv_uuid"
    )
    private String idocvUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_scene"
    )
    private EnumConst enumConstByFlagScene;
}
