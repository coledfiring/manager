package com.whaty.products.service.training.impl;

import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 合作培训项目审核
 *
 * @author weipengsen
 */
@Lazy
@Service("cooperationTrainingItemCheckService")
public class CooperationTrainingItemCheckServiceImpl extends TycjGridServiceAdapter<TrainingItem> {
}
