package com.whaty.products.service.basic.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeArea;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 培训地区管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("trainingAreaManageService")
public class TrainingAreaManageServiceImpl extends TycjGridServiceAdapter<PeArea> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PeArea bean) throws EntityException {
        bean.setName(bean.getProvince().trim() + StringUtils.trimBlankAndNull(bean.getCity()) +
                StringUtils.trimBlankAndNull(bean.getCounty()));
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(PeArea bean) throws EntityException {
        bean.setName(bean.getProvince() + (bean.getCity() == null ? "" : bean.getCity()) + bean.getCounty());
        bean.setUpdateBy(UserUtils.getCurrentUser());
        bean.setUpdateDate(new Date());
        this.checkBeforeAddOrUpdate(bean);
    }

    private void checkBeforeAddOrUpdate(PeArea bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " and id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_area where site_code = ? and name = ? "
                + additionalSql, MasterSlaveRoutingDataSource.getDbType(), bean.getName())) {
            throw new EntityException("此地区已存在");
        }
    }

}
