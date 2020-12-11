package com.whaty.products.service.basic.impl;

import com.whaty.core.commons.exception.EntityException;
import com.whaty.domain.bean.EntrustedUnitLinkman;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 委托单位联系人列表服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("entrustedUnitLinkmanManageService")
public class EntrustedUnitLinkmanManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<EntrustedUnitLinkman> {
    @Override
    protected String getOrderColumnIndex() {
        return "name";
    }

    @Override
    public void afterAdd(EntrustedUnitLinkman bean) throws EntityException {
        super.afterAdd(bean);
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "entrustedUnit.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "entrustedUnit.id";
    }
}
