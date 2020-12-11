package com.whaty.domain.bean.message;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.PeWebSite;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 站内信组与站点关联
 *
 * @author weipengsen
 */
@Data
@Entity(name = "StationMessageGroupSite")
@Table(name = "station_message_group_site")
public class StationMessageGroupSite extends AbstractBean {

    private static final long serialVersionUID = -5085002969328081244L;
    @Id
    @GenericGenerator(name = "idGenerator",strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @ManyToOne
    @JoinColumn(
            name = "fk_message_group_id"
    )
    private StationMessageGroup stationMessageGroup;
    @ManyToOne
    @JoinColumn(
            name = "fk_web_site_id"
    )
    private PeWebSite peWebSite;
    @ManyToOne
    @JoinColumn(
            name = "flag_active"
    )
    private EnumConst enumConstByFlagActive;

}
