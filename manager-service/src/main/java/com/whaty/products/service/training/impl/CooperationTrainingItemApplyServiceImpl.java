package com.whaty.products.service.training.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.domain.bean.EntrustedUnit;
import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.training.domain.EntrustedUnitWithLink;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 合作培训项目申报
 *
 * @author weipengsen
 */
@Lazy
@Service("cooperationTrainingItemApplyService")
public class CooperationTrainingItemApplyServiceImpl extends AbstractTrainingItemApplyService {

    @Override
    public void checkBeforeAdd(TrainingItem bean) throws EntityException {
        checkBeforeUpdateOrAdd(bean);
        bean.getEntrustUnitLinkman();
        bean.setEnumConstByFlagTrainingItemType(this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_TRAINING_ITEM_TYPE, "2"));
        super.checkBeforeAdd(bean);
    }

    @Override
    public void checkBeforeUpdate(TrainingItem bean) throws EntityException {
        checkBeforeUpdateOrAdd(bean);
        super.checkBeforeUpdate(bean);
    }

    /**
     * 更新和添加前检查
     *
     * @param bean
     */
    private void checkBeforeUpdateOrAdd(TrainingItem bean) {
        if (StringUtils.isNotBlank(bean.getEntrustUnitLinkman())) {
            String[] entrustUnitLinkman = bean.getEntrustUnitLinkman().split("/");
            bean.setEntrustUnitLinkman(entrustUnitLinkman[0]);
            bean.setEntrustUnitLinkPhone(entrustUnitLinkman[1]);
        }
    }
}
