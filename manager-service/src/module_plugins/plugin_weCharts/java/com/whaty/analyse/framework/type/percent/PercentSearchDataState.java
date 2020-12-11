package com.whaty.analyse.framework.type.percent;

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

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 进度条统计数据查询状态
 *
 * @author weipengsen
 */
public class PercentSearchDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final PercentConfigDO config;

    private final GeneralDao generalDao;

    public PercentSearchDataState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.config = (PercentConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.config.setData(this.searchData());
        return this.handleNextState(new AnalyseConvertState<>(this.context, this.config, PercentConfigVO.class));
    }

    /**
     * 查询数据
     *
     * @return
     */
    private Map<String, Number> searchData() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return this.config.getSql().stream().map(this::searchData)
                    .flatMap(e -> e.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

    /**
     * 查询sql的数据
     *
     * @param sql
     * @return
     */
    private Map<String, Number> searchData(String sql) {
        return this.generalDao.getOneMapBySQL(AnalyseUtils
                .handleSql(sql, this.basicConfig.getAnalyseParam().getSearch()))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() != null ? (Number) e.getValue() : 0));
    }
}
