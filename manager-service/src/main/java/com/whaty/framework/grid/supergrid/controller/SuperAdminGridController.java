package com.whaty.framework.grid.supergrid.controller;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import org.apache.commons.lang.StringUtils;

/**
 * 用于在超管端增加业务查询的controller
 * @param <T>
 * @author weipengsen
 */
public class SuperAdminGridController<T extends AbstractBean> extends AbstractChangeDataSourceGridController<T> {

    /**
     * 根据传参切换数据源
     * @param paramsData
     */
    @Override
    protected void changeDataSource(ParamsDataModel<T> paramsData) {
        //根据配置切换数据源
        String webSiteCode = paramsData.getStringParameter(CommonConstant.PARAM_WEB_SITE_CODE);
        MasterSlaveRoutingDataSource.setDbType(StringUtils.isEmpty(webSiteCode) ?
                SiteConstant.SERVICE_SITE_CODE_BASE : webSiteCode);
    }

}
