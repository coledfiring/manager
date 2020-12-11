package com.whaty.framework.grid.supergrid.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 用于在超管端增加业务查询的服务类
 * @param <T>
 * @author weipengsen
 */
public class SuperAdminGridServiceImpl<T extends AbstractBean> extends TycjGridServiceAdapter<T> {

    @Override
    public void initGrid(GridConfig gridConfig, Map<String, Object> mapParam) {
        String webSiteCode = (String) mapParam.get(CommonConstant.PARAM_WEB_SITE_CODE);
        MasterSlaveRoutingDataSource.setDbType(StringUtils.isEmpty(webSiteCode) ?
                SiteConstant.SERVICE_SITE_CODE_BASE : webSiteCode);
    }



}
