package com.whaty.analyse.framework.type.compare;

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
 * 对比统计查找数据状态
 *
 * @author weipengsen
 */
public class CompareSearchDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final CompareConfigDO config;

    private final GeneralDao generalDao;

    public CompareSearchDataState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.config = (CompareConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.config.setData(this.searchData());
        return this.handleNextState(new AnalyseConvertState<>(this.context, this.config, CompareConfigVO.class));
    }

    /**
     * 查询数据
     * @return
     */
    private Map<String, Number> searchData() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return this.config.getSql().stream().map(this::searchData).flatMap(e -> e.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> (Number) e.getValue()));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

    /**
     * 查找数据
     * @param sql
     * @return
     */
    private Map<String, Object> searchData(String sql) {
        return this.generalDao.getOneMapBySQL(AnalyseUtils.handleSql(sql,
                this.basicConfig.getAnalyseParam().getSearch()));
    }
}
