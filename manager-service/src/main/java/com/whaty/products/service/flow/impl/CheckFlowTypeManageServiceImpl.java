package com.whaty.products.service.flow.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.flow.CheckFlowType;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 审核流程类型管理
 *
 * @author weipengsen
 */
@Lazy
@Service("checkFlowTypeManageService")
public class CheckFlowTypeManageServiceImpl extends TycjGridServiceAdapter<CheckFlowType> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(CheckFlowType bean, Map<String, Object> params) throws EntityException {
        if (this.myGeneralDao.checkNotEmpty("select 1 from check_flow_type ty " +
                "inner join check_flow_group g on g.id = ty.fk_flow_group_id " +
                "where g.site_code = ? and ty.flag_flow_type = ?", MasterSlaveRoutingDataSource.getDbType(),
                bean.getEnumConstByFlagFlowType().getId())) {
            throw new EntityException("相同类型的流程已经设置");
        }
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
    }
}
