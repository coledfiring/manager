package com.whaty.analyse.framework.type.radar;

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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 雷达图查询数据状态
 *
 * @author weipengsen
 */
public class RadarSearchDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final RadarConfigDO config;

    private final GeneralDao generalDao;

    public RadarSearchDataState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.config = (RadarConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.config.getSeries().forEach(e -> e.setData(this.searchData(e.getSql())));
        return this.handleNextState(new AnalyseConvertState<>(this.context, this.config, RadarConfigVO.class));
    }

    /**
     * 查找数据
     *
     * @return
     */
    private Map<String, ? extends Number> searchData(List<String> sql) {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return sql.stream()
                    .map(e -> this.generalDao.getOneMapBySQL(AnalyseUtils.handleSql(e,
                            this.basicConfig.getAnalyseParam().getSearch())))
                    .filter(MapUtils::isNotEmpty)
                    .flatMap(e -> e.entrySet().stream())
                    .peek(e -> e.setValue(Objects.nonNull(e.getValue()) ? e.getValue() : 0))
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> (Number) e.getValue()));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }
}
