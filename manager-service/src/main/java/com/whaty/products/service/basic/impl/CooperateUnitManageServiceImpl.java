package com.whaty.products.service.basic.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.CooperateUnit;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 合作单位管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("cooperateUnitManageService")
public class CooperateUnitManageServiceImpl extends TycjGridServiceAdapter<CooperateUnit> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(CooperateUnit bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(CooperateUnit bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    /**
     * 添加或修改前检查
     */
    private void checkBeforeAddOrUpdate(CooperateUnit bean) throws EntityException {
        String additionSql = bean.getId() == null ? "" : " and id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from cooperate_unit where name = ? and site_code = ?"
                + additionSql, bean.getName(), MasterSlaveRoutingDataSource.getDbType())) {
            throw new EntityException("此名称已存在");
        }
    }
}
