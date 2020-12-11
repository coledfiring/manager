package com.whaty.analyse.framework.type.map;

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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 地区统计查询数据状态
 *
 * @author weipengsen
 */
public class MapSearchDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final MapConfigDO config;

    private final GeneralDao generalDao;

    public MapSearchDataState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.config = (MapConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.config.setData(this.searchData());
        return this.handleNextState(new AnalyseConvertState<>(this.context, this.config, MapConfigVO.class));
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
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

    /**
     * 查找数据
     * @param sql
     * @return
     */
    private List<Map<String, Object>> searchData(String sql) {
        return this.generalDao.getMapBySQL(AnalyseUtils.handleSql(sql,
                this.basicConfig.getAnalyseParam().getSearch()));
    }

}
