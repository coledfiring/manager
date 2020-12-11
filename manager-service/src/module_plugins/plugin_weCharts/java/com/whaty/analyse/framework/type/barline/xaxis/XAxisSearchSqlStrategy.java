package com.whaty.analyse.framework.type.barline.xaxis;

import com.whaty.analyse.framework.AnalyseUtils;
import com.whaty.analyse.framework.domain.bean.AnalyseBasicConfig;
import com.whaty.analyse.framework.type.barline.BarAndLineConfigDO;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.config.util.SiteUtil;

/**
 * sql查询策略
 *
 * @author weipengsen
 */
public class XAxisSearchSqlStrategy extends AbstractXAxisSearchStrategy {

    private final BarAndLineConfigDO configDO;

    private final GeneralDao generalDao;

    public XAxisSearchSqlStrategy(AnalyseBasicConfig config) {
        super(config);
        this.configDO = (BarAndLineConfigDO) this.config.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public XItemResult search() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return new XItemResult(this.generalDao.getBySQL(AnalyseUtils.handleSql(this.configDO.getItem().getXItemSql(),
                    this.config.getAnalyseParam().getSearch())));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

}
