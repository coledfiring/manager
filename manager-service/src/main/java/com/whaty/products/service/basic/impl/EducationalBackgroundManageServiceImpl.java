package com.whaty.products.service.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 学历管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("educationalBackgroundManageService")
public class EducationalBackgroundManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagEducationalBackground";
    }

}
