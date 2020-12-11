package com.whaty.domain.bean.message;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 站内信模板
 *
 * @author weipengsen
 */
@Data
@Entity(name = "StationMessageTemplate")
@Table(name = "station_message_template")
public class StationMessageTemplate extends AbstractBean {

    private static final long serialVersionUID = -4628591431644006984L;
    @Id
    @GenericGenerator(name = "idGenerator",strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @Column(
            name = "name"
    )
    private String name;
    @ManyToOne
    @JoinColumn(
            name = "fk_message_group_site_id"
    )
    private StationMessageGroupSite stationMessageGroupSite;
    @Column(
            name = "title"
    )
    private String title;
    @Column(
            name = "content"
    )
    private String content;
    @Column(
            name = "foot"
    )
    private String foot;
    @ManyToOne
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;
    @Column(
            name = "useNumber"
    )
    private Long useNumber;

}
