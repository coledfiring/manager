package com.whaty.products.service.training.impl;

import com.whaty.domain.bean.TrainingItemApply;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 申请单位查看服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("trainingItemApplyUnitService")
public class TrainingItemApplyUnitServiceImpl extends AbstractTwoLevelListGridServiceImpl<TrainingItemApply> {
    @Override
    protected String getOrderColumnIndex() {
        return "peUnit";
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "trainingItem.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "trainingItem.id";
    }
}
