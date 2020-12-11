package com.whaty.products.service.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 车辆状态型管理服务类
 *
 * @author pingzhihao
 */
@Lazy
@Service("vehicleStatusManageService")
public class VehicleStatusManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagVehicleStatus";
    }
}
