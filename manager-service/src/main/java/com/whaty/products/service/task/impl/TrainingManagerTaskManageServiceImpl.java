package com.whaty.products.service.task.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.TrainingManagerTask;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 待办催办任务管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("trainingManagerTaskManageService")
public class TrainingManagerTaskManageServiceImpl extends TycjGridServiceAdapter<TrainingManagerTask> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeAdd(TrainingManagerTask bean) throws EntityException {
        bean.setCreateManager(UserUtils.getCurrentManager());
        bean.setEnumConstByFlagTrainingTaskStatus(this.generalDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_TRAINING_TASK_STATUS, "0"));
        bean.setCreateTime(new Date());
    }
}
