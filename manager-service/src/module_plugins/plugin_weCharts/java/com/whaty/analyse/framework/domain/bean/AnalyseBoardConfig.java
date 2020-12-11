package com.whaty.analyse.framework.domain.bean;

import com.whaty.analyse.framework.domain.AnalyseBoardConfigVO;
import com.whaty.common.string.StringUtils;
import com.whaty.core.bean.AbstractBean;
import com.whaty.utils.StaticBeanUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 统计看板
 *
 * @author weipengsen
 */
@Data
@Entity(name = "AnalyseBoardConfig")
@Table(name = "analyse_board_config")
public class AnalyseBoardConfig extends AbstractBean {

    private static final long serialVersionUID = -2559275890051277419L;

    @Id
    @GenericGenerator(
            name = "idGenerator",
            strategy = "uuid"
    )
    @GeneratedValue(
            generator = "idGenerator"
    )
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "code")
    private String code;

    @Column(name = "can_export")
    private String canExport;

    @Column(name = "label_sql")
    private String labelSql;

    @Column(name = "create_date")
    private Date createDate;

}
