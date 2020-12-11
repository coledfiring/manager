package com.whaty.domain.bean.flow;

import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.AbstractSiteBean;
import com.whaty.domain.bean.PeManager;
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
 * 审核流程的审核记录
 *
 * @author weipengsen
 */
@Data
@Entity(name = "CheckFlowRecord")
@Table(name = "check_flow_record")
public class CheckFlowRecord extends AbstractSiteBean {

    private static final long serialVersionUID = -982428075937032978L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_flow_type"
    )
    private EnumConst enumConstByFlagFlowType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "operate_user"
    )
    private PeManager operateUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flag_check_operate"
    )
    private EnumConst enumConstByFlagCheckOperate;
    @Column(
            name = "check_time"
    )
    private Date checkTime;
    @Column(
            name = "item_name"
    )
    private String itemName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_check_flow_id"
    )
    private CheckFlow checkFlow;
    @Column(
            name = "site_code"
    )
    private String siteCode;

    public CheckFlowRecord(EnumConst enumConstByFlagFlowType, PeManager operateUser,
                           EnumConst enumConstByFlagCheckOperate, String itemName, CheckFlow checkFlow) {
        this.enumConstByFlagFlowType = enumConstByFlagFlowType;
        this.operateUser = operateUser;
        this.enumConstByFlagCheckOperate = enumConstByFlagCheckOperate;
        this.checkTime = new Date();
        this.itemName = itemName;
        this.checkFlow = checkFlow;
        this.siteCode = MasterSlaveRoutingDataSource.getDbType();
    }

}
