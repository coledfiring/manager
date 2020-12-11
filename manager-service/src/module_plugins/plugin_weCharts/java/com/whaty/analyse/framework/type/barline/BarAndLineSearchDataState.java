package com.whaty.analyse.framework.type.barline;

import com.whaty.analyse.framework.AnalyseUtils;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.state.AbstractState;
import com.whaty.analyse.framework.state.AnalyseStateContext;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.config.util.SiteUtil;

import java.util.List;
import java.util.Map;

/**
 * 柱状和折线图查询状态机
 *
 * @author weipengsen
 */
public class BarAndLineSearchDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final GeneralDao generalDao;

    private final BarAndLineConfigDO configDO;

    public BarAndLineSearchDataState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.configDO = (BarAndLineConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.configDO.setData(this.searchData());
        return this.handleNextState(new BarAndLineConvertState(this.context, this.basicConfig));
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
            return this.generalDao.getMapBySQL(AnalyseUtils.handleSql(this.configDO.getSql(),
                            this.basicConfig.getAnalyseParam().getSearch()));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

}
