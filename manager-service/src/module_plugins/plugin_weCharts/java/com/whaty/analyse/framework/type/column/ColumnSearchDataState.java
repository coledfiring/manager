package com.whaty.analyse.framework.type.column;

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
import org.apache.commons.collections.MapUtils;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 查询数据状态机
 *
 * @author weipengsen
 */
public class ColumnSearchDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final ColumnConfigDO configDO;

    private final GeneralDao generalDao;

    public ColumnSearchDataState(AnalyseBasicConfig config, AnalyseStateContext context) {
        super(context);
        this.basicConfig = config;
        this.configDO = (ColumnConfigDO) config.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.configDO.setData(this.searchData());
        return this.handleNextState(new AnalyseConvertState<>(this.context, this.configDO, ColumnConfigVO.class));
    }

    /**
     * 查询数据
     *
     * @return
     */
    protected Map<String, Object> searchData() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return this.configDO.getSql().stream()
                    .map(e -> this.generalDao.getOneMapBySQL(AnalyseUtils.handleSql(e,
                            this.basicConfig.getAnalyseParam().getSearch())))
                    .filter(MapUtils::isNotEmpty)
                    .flatMap(e -> e.entrySet().stream())
                    .peek(e -> e.setValue(Objects.nonNull(e.getValue()) ? e.getValue() : "-"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

}
