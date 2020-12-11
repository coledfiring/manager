package com.whaty.domain.bean.message;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.SsoUser;
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
 * 站内信
 *
 * @author weipengsen
 */
@Data
@Entity(
        name = "StationMessage"
)
@Table(
        name = "station_message"
)
public class StationMessage extends AbstractBean {

    private static final long serialVersionUID = 8416310295298425842L;
    @Id
    @GenericGenerator(name = "idGenerator",strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
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
            name = "fk_sso_user_id"
    )
    private SsoUser ssoUser;
    @ManyToOne
    @JoinColumn(
            name = "flag_is_star"
    )
    private EnumConst enumConstByFlagIsStar;
    @ManyToOne
    @JoinColumn(
            name = "flag_readed"
    )
    private EnumConst enumConstByFlagReaded;
    @Column(
            name = "send_date"
    )
    private String sendDate;
    @ManyToOne
    @JoinColumn(
            name = "fk_send_user_id"
    )
    private SsoUser sendUser;

}
