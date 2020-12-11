package com.whaty.products.service.basic.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PrepareItem;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 带班自查事务管理
 *
 * @author weipengsen
 */
@Lazy
@Service("prepareItemManageService")
public class PrepareItemManageServiceImpl extends TycjGridServiceAdapter<PrepareItem> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PrepareItem bean, Map<String, Object> params) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
    }

    @Override
    public void checkBeforeUpdate(PrepareItem bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setUpdateBy(UserUtils.getCurrentUser());
        bean.setUpdateDate(new Date());
    }

    private void checkBeforeAddOrUpdate(PrepareItem bean) throws EntityException {
        String sql = "select 1 from prepare_item where flag_prepare_type = ? and flag_training_type = ? " +
                "and site_code = ? and `name` = ? " + (bean.getId() == null ? "" : "and id <> '" + bean.getId() + "'");
        if (this.myGeneralDao.checkNotEmpty(sql, bean.getEnumConstByFlagTrainingType().getId(),
                bean.getEnumConstByFlagPrepareType().getId(), MasterSlaveRoutingDataSource.getDbType(),
                bean.getName())) {
            throw new EntityException("对应的准备类型、培训类型下已存在相同名称的事务");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        this.myGeneralDao.executeBySQL("delete from teacher_prepare_item where " +
                CommonUtils.madeSqlIn(idList, "fk_prepare_item_id"));
    }
}
