package com.whaty.domain.bean.online;

import com.whaty.annotation.Unique;
import com.whaty.core.bean.AbstractBean;
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

/**
 * 报名配置项
 *
 * @author suoqiangqiang
 */
@Data
@Entity(name = "OlEnrollColumn")
@Table(name = "ol_enroll_column")
public class OlEnrollColumn extends AbstractBean {

    private static final long serialVersionUID = 6606780040065751660L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @Unique
    @Column(name = "name")
    private String name;

    @Unique
    @Column(name = "data_index")
    private String dataIndex;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flag_enroll_column_type")
    private EnumConst enumConstByFlagEnrollColumnType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_enroll_column_group")
    private EnumConst enumConstByFlagEnrollColumnGroup;

    @Column(name = "reg_exp")
    private String regExp;

    @Column(name = "exp_error_info")
    private String expErrorInfo;

    @Column(name = "custom_sql")
    private String customSql;

    @Column(name = "note")
    private String note;

    @Column(name = "example_picture")
    private String examplePicture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_is_attach_file")
    private EnumConst enumConstByFlagIsAttachFile;

    @Column(name = "file_num")
    private Integer fileNum;

}
