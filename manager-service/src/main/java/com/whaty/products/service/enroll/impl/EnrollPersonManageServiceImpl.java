package com.whaty.products.service.enroll.impl;

import com.whaty.domain.bean.enroll.EnrollPerson;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 报名系统用户联系人管理service
 *
 * @author pingzhihao
 */
@Lazy
@Service("enrollPersonManageService")
public class EnrollPersonManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<EnrollPerson> {

    @Override
    protected String getParentIdSearchParamName() {
        return null;
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return null;
    }
}
