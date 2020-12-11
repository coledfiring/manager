package com.whaty.products.service.training.domain;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.EntrustedUnit;
import com.whaty.domain.bean.EntrustedUnitLinkman;
import com.whaty.domain.bean.PeArea;
import lombok.Data;

/**
 * 带联系人的委托单位
 *
 * @author weipengsen
 */
@Data
public class EntrustedUnitWithLink extends AbstractBean {

    private static final long serialVersionUID = 4646137692694569476L;

    private String id;

    private String name;

    private String area;

    private String address;

    private String taxNumber;

    private EnumConst enumConstByFlagEntrustedUnitType;

    private PeArea peArea;

    private EnumConst enumConstByFlagProvince;



    private String linkman;

    private String telephone;

    private String mobileNumber;

    private String job;

    private String contacter;

    /**
     * 生成委托单位
     * @return
     */
    public EntrustedUnit generateEntrustedUnit() {
        return new EntrustedUnit(null, this.name, this.area, this.address, this.taxNumber,
                this.enumConstByFlagEntrustedUnitType, this.peArea, this.enumConstByFlagProvince,
                MasterSlaveRoutingDataSource.getDbType());
    }

    /**
     * 生成委托单位联系人
     * @return
     */
    public EntrustedUnitLinkman generateEntrustedUnitLinkman(EntrustedUnit unit) {
        return new EntrustedUnitLinkman(null, this.linkman, this.telephone, this.mobileNumber,
                this.job, this.contacter, unit, MasterSlaveRoutingDataSource.getDbType());
    }

}
