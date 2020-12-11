package com.whaty.products.service.oltrain.basic.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.products.service.basic.AbstractEnumConstGridService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 机构类型管理服务类
 *
 * @author suoqioangqiang
 */
@Lazy
@Service("olCourseTypeManageService")
public class OLCourseTypeManageServiceImpl extends AbstractEnumConstGridService<EnumConst> {

    @Override
    protected String getNamespace() {
        return "flagOnlineCourseType";
    }

}