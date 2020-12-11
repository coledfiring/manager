package com.whaty.products.service.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 委托单位类型管理服务类
 *
 * @author suoqioangqiang
 */
@Lazy
@Service("entrustedUnitTypeManageService")
public class EntrustedUnitTypeManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagEntrustedUnitType";
    }

}