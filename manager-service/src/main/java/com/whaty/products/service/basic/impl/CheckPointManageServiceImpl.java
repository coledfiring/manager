package com.whaty.products.service.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * author weipengsen  Date 2020/7/3
 */
@Lazy
@Service("checkPointManageService")
public class CheckPointManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagcheckPoint";
    }
}
