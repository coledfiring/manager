package com.whaty.products.service.vehicle.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.vehicle.Motorcade;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 车队管理
 *
 * @author pingzhihao
 */
@Lazy
@Service("motorcadeManageService")
public class MotorcadeManageServiceImpl extends TycjGridServiceAdapter<Motorcade> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeAdd(Motorcade bean) throws EntityException {
        bean.setCreateDate(new Date());
        super.checkBeforeAdd(bean);
    }

}