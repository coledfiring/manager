package com.whaty.analyse.framework.type.funnel;

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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 漏斗图查询数据状态机
 *
 * @author weipengsen
 */
public class FunnelSearchDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final FunnelConfigDO config;

    private final GeneralDao generalDao;

    public FunnelSearchDataState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.config = (FunnelConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.config.setData(this.searchData());
        return this.handleNextState(new AnalyseConvertState<>(this.context,
                (FunnelConfigDO) this.basicConfig.getIConfigDO(), FunnelConfigVO.class));
    }

    /**
     * 查找数据
     *
     * @return
     */
    private Map<String, ? extends Number> searchData() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return this.generalDao.getOneMapBySQL(AnalyseUtils
                    .handleSql(this.config.getSql(), this.basicConfig.getAnalyseParam().getSearch()))
                    .entrySet().stream().peek(e -> e.setValue(Objects.nonNull(e.getValue()) ? e.getValue() : 0))
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> (Number) e.getValue()));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

}
