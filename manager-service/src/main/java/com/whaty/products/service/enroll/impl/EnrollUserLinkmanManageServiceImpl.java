package com.whaty.products.service.enroll.impl;

import com.whaty.domain.bean.enroll.EnrollUserLinkman;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 报名系统用户联系人管理service
 *
 * @author pingzhihao
 */
@Lazy
@Service("enrollUserLinkmanManageService")
public class EnrollUserLinkmanManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<EnrollUserLinkman> {

    @Override
    protected String getParentIdSearchParamName() {
        return "enrollUserId";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "enrollUser.id";
    }
}
