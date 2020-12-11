package com.whaty.domain.bean.vehicle;

import com.whaty.HasAttachFile;
import com.whaty.domain.bean.AbstractSiteBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * vm-车辆管理-车队表
 *
 * @author pingzhihao
 */
@Data
@Entity(name = "Motorcade")
@Table(name = "motorcade")
public class Motorcade extends AbstractSiteBean implements HasAttachFile {

    private static final long serialVersionUID = -4423540007571092242L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 车队名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 负责人姓名
     */
    @Column(name = "responsible_person_name")
    private String responsiblePersonName;

    /**
     * 负责人电话
     */
    @Column(name = "responsible_person_tel")
    private String responsiblePersonTel;

    /**
     * 联系人姓名
     */
    @Column(name = "linkman_name")
    private String linkmanName;

    /**
     * 联系人电话
     */
    @Column(name = "linkman_tel")
    private String linkmanTel;

    /**
     * 备注
     */
    @Column(name = "notes")
    private String notes;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;


    @Column(name = "site_code")
    private String siteCode;

}
