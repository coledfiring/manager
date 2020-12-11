package com.whaty.products.service.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 合作单位类型管理服务类
 *
 * @author suoqioangqiangs
 */
@Lazy
@Service("cooperateUnitTypeManageService")
public class CooperateUnitTypeManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagCooperateUnitType";
    }

}