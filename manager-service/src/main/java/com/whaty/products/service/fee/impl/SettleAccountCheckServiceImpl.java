package com.whaty.products.service.fee.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeClass;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 结算审核
 *
 * @author weipengsen
 */
@Lazy
@Service("settleAccountCheckService")
public class SettleAccountCheckServiceImpl extends TycjGridServiceAdapter<PeClass> {
    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 驳回审核
     *
     * @param ids
     */
    public void doRejectAudit(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        PeClass clazz = this.myGeneralDao.getById(PeClass.class, ids);
        TycjParameterAssert.isAllNotNull(clazz);
        if (!"2".equals(clazz.getEnumConstByFlagSettleAccountStatus().getCode())) {
            throw new ServiceException("审核通过的数据才能驳回审核");
        }
        clazz.setEnumConstByFlagSettleAccountStatus(this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_SETTLE_ACCOUNT_STATUS, "4"));
        this.myGeneralDao.save(clazz);
    }
}
