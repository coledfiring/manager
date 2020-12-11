package com.whaty.analyse.framework.type.weather;

import com.alibaba.fastjson.JSON;
import com.whaty.analyse.framework.AnalyseUtils;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.state.AbstractState;
import com.whaty.analyse.framework.state.AnalyseConvertState;
import com.whaty.analyse.framework.state.AnalyseStateContext;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.yysj.PeWeather;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.config.util.SiteUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文本块数据查询状态
 *
 * @author 卫鹏森
 */
public class WeatherDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final WeatherConfigDO config;

    private final GeneralDao generalDao;

    public WeatherDataState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.config = (WeatherConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.config.setData(this.searchData());
        return this.handleNextState(new AnalyseConvertState<>(this.context, this.config, WeatherConfigVO.class));
    }

    /**
     * 查询数据
     *
     * @return
     */
    protected List<PeWeather> searchData() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return this.searchListData().stream().map(e-> JSON.parseObject(JSON.toJSONString(e), PeWeather.class))
                    .sorted(Comparator.comparing(PeWeather::getDate))
                    .collect(Collectors.toList());
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

    /**
     * 查询多行数据
     * @return
     */
    private List<Map<String, Object>> searchListData() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return this.generalDao.getMapBySQL(this.config.getSql());
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

}
