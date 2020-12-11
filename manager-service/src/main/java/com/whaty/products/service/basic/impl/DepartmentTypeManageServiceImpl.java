package com.whaty.products.service.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 部门类型管理服务类
 *
 * @author suoqioangqiang
 */
@Lazy
@Service("departmentTypeManageService")
public class DepartmentTypeManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagDepartmentType";
    }

}