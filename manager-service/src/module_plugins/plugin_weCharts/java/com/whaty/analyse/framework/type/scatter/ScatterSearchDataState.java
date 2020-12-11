package com.whaty.analyse.framework.type.scatter;

import com.whaty.analyse.framework.AnalyseUtils;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.state.AbstractState;
import com.whaty.analyse.framework.state.AnalyseConvertState;
import com.whaty.analyse.framework.state.AnalyseStateContext;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.config.util.SiteUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 散点图查询数据状态
 *
 * @author weipengsen
 */
public class ScatterSearchDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final ScatterConfigDO config;

    private final GeneralDao generalDao;

    public ScatterSearchDataState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.config = (ScatterConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.config.setData(this.searchData());
        if (this.config.getData().stream().anyMatch(this::validate)) {
            throw new IllegalArgumentException("find the length of some data is no valid");
        }
        return this.handleNextState(new AnalyseConvertState<>(this.context, this.config, ScatterConfigVO.class));
    }

    /**
     * 检查数据是否合法
     * @param data
     * @return
     */
    private boolean validate(Map<String, Object> data) {
        return !data.containsKey("x") || !data.containsKey("y");
    }

    /**
     * 查询数据
     * @return
     */
    private List<Map<String, Object>> searchData() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return this.config.getSql().stream().map(this::searchData)
                    .flatMap(List::stream).collect(Collectors.toList());
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

    /**
     * 查询sql数据
     * @param sql
     * @return
     */
    private List<Map<String, Object>> searchData(String sql) {
        return this.generalDao.getMapBySQL(AnalyseUtils
                .handleSql(sql, this.basicConfig.getAnalyseParam().getSearch()));
    }

}
