package com.whaty.analyse.framework.type.barline;

import com.alibaba.fastjson.JSON;
import com.whaty.analyse.framework.AnalyseUtils;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.state.AbstractState;
import com.whaty.analyse.framework.state.AnalyseStateContext;
import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.util.TycjCollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BarAndLineSearchSeriesState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final GeneralDao generalDao;

    private final BarAndLineConfigDO configDO;

    public BarAndLineSearchSeriesState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.configDO = (BarAndLineConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        if (StringUtils.isNotBlank(this.configDO.getSeries().getSql())) {
            if (this.configDO.getSeries().getItems() == null) {
                this.configDO.getSeries().setItems(new ArrayList<>());
            }
            this.configDO.getSeries().getItems().addAll(JSON.parseArray(JSON.toJSONString(this.searchData()),
                    BarAndLineConfigDO.SeriesDO.SeriesItemDO.class));
        }
        this.basicConfig.getAnalyseParam().getSearch().putAll(TycjCollectionUtils
                .map("series", this.configDO.getSeries(), "sqlAlias", this.configDO.getSeries().listSqlAlias()));
        return this.handleNextState(new BarAndLineSearchDataState(this.basicConfig, this.context));
    }

    /**
     * 查询数据
     *
     * @return
     */
    protected List<Map<String, Object>> searchData() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return this.generalDao.getMapBySQL(AnalyseUtils.handleSql(this.configDO.getSeries().getSql(),
                    this.basicConfig.getAnalyseParam().getSearch()));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

}
