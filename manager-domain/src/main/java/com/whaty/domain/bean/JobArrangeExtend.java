package com.whaty.domain.bean;

import com.whaty.core.bean.AbstractBean;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 工作安排扩展类
 *
 * @author shangyu
 *
 */
@Data
@Entity(name = "JobArrangeExtend")
@Table(name = "job_arrange_extend")
public class JobArrangeExtend extends AbstractBean {

    private static final long serialVersionUID = 9200123266489954878L;
    /**
     * 主键ID
     */
    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @Column(name = "content")
    private String content;

}
