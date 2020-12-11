package com.whaty.products.service.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 适合对象管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("suitableTargetManageService")
public class SuitableTargetManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagSuitableTarget";
    }

}
