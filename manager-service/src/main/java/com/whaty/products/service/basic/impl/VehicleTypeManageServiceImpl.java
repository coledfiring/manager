package com.whaty.products.service.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 车辆类型管理服务类
 *
 * @author pingzhihao
 */
@Lazy
@Service("vehicleTypeManageService")
public class VehicleTypeManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagVehicleType";
    }
}
