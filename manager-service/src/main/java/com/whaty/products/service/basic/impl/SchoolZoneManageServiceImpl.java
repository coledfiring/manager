package com.whaty.products.service.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 校区管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("schoolZoneManageService")
public class SchoolZoneManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagSchoolZone";
    }

}
