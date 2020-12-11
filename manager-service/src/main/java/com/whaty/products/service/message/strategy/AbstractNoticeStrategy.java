package com.whaty.products.service.message.strategy;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.framework.httpClient.domain.HttpClientResponseData;
import com.whaty.framework.httpClient.helper.HttpClientHelper;
import com.whaty.products.service.message.constant.MessageConstants;
import com.whaty.util.SQLHandleUtils;
import com.whaty.utils.StaticBeanUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 抽象通知策略
 *
 * @author weipengsen
 */
public interface AbstractNoticeStrategy {

    /**
     * 通知
     * @param params
     * @throws Exception
     */
    void notice(Map<String, Object> params) throws Exception;

    /**
     * 发送请求
     *
     * @param url
     * @param jsonString
     * @return
     * @throws IOException
     */
    default HttpClientResponseData requestHttp(String url, String jsonString) throws IOException {
        //执行http请求
        return new HttpClientHelper().doPostJSON(url, jsonString);
    }

    /**
     * 获取过滤后的id
     * @param params
     * @return
     */
    default List<String> getFilterIds(Map<String, Object> params) {
        String currentDataSource = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        String sql = StaticBeanUtils.getOpenGeneralDao()
                .getOneBySQL("select filter_sql from " + this.getTableName() + " where code = ?",
                        params.get(MessageConstants.PARAM_TEMPLATE_CODE));
        MasterSlaveRoutingDataSource.setDbType(currentDataSource);
        return StaticBeanUtils.getOpenGeneralDao()
                .getBySQL(SQLHandleUtils.replaceSignUseParams(sql, params));
    }

    /**
     * 获取表名
     * @return
     */
    String getTableName();
}
