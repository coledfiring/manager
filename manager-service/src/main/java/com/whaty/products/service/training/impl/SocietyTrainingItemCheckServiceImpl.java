package com.whaty.products.service.training.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeClass;
import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 社会培训项目审核
 *
 * @author weipengsen
 */
@Lazy
@Service("societyTrainingItemCheckService")
public class SocietyTrainingItemCheckServiceImpl extends TycjGridServiceAdapter<TrainingItem> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 驳回审核
     *
     * @param ids
     */
    public void doRejectAudit(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        TrainingItem item = this.myGeneralDao.getById(TrainingItem.class, ids);
        TycjParameterAssert.isAllNotNull(item);
        if (!"3".equals(item.getEnumConstByFlagItemStatus().getCode())) {
            throw new ServiceException("审核通过的数据才能驳回审核");
        }
        item.setEnumConstByFlagItemStatus(this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_ITEM_STATUS, "5"));
        this.myGeneralDao.save(item);
    }
}
