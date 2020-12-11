package com.whaty.products.service.resource.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeHotel;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 合作宾馆资源
 *
 * @author weipengsen
 */
@Lazy
@Service("hotelResourceManageService")
public class HotelResourceManageServiceImpl extends TycjGridServiceAdapter<PeHotel> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PeHotel bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
    }

    @Override
    public void checkBeforeUpdate(PeHotel bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setUpdateBy(UserUtils.getCurrentUser());
        bean.setUpdateDate(new Date());
    }

    private void checkBeforeAddOrUpdate(PeHotel bean) throws EntityException {
        String additionalSql = StringUtils.isBlank(bean.getId()) ? "" : " AND id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_hotel where name = ? and site_code = ?" +
                additionalSql, bean.getName(), MasterSlaveRoutingDataSource.getDbType())) {
            throw new EntityException("此名称已存在");
        }
    }
}
