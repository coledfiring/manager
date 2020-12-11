package com.whaty.analyse.framework.type.pie;

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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 饼图数据查找状态机
 *
 * @author weipengsen
 */
public class PieSearchDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final PieConfigDO config;

    private final GeneralDao generalDao;

    public PieSearchDataState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.config = (PieConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        if (this.config.getIsMapMetaData() != null && this.config.getIsMapMetaData()) {
            this.config.setData(this.searchMapData());
            this.config.getItems().forEach(e -> e.setValue(this.config.getData().get(e.getAlias())));
        } else {
            this.config.setItems(this.searchListData().stream()
                    .map(e -> new PieConfigDO.PieItemConfigDO((String) e.get("name"), (Number) e.get("value")))
                    .collect(Collectors.toList()));
        }
        this.config.setInnerContent(this.searchInnerContent());
        return this.handleNextState(new PieConvertState(this.context, this.basicConfig));
    }

    /**
     * 查询多行数据
     * @return
     */
    private List<Map<String, Object>> searchListData() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return this.generalDao.getMapBySQL(AnalyseUtils
                    .handleSql(this.config.getSql(), this.basicConfig.getAnalyseParam().getSearch()));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

    /**
     * 查询单行数据，需要维度转换
     *
     * @return
     */
    private Map<String, ? extends Number> searchMapData() {
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return this.generalDao.getOneMapBySQL(AnalyseUtils
                    .handleSql(this.config.getSql(), this.basicConfig.getAnalyseParam().getSearch()))
                    .entrySet().stream().filter(e -> Objects.nonNull(e.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> (Number) e.getValue()));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

    /**
     * 查询饼图中心内容
     * @return
     */
    private String searchInnerContent() {
        if (StringUtils.isBlank(this.config.getInnerContentSql())) {
            return null;
        }
        String currentSite = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            return String.valueOf(this.generalDao.<Object>getOneBySQL(AnalyseUtils
                    .handleSql(this.config.getInnerContentSql(),
                            this.basicConfig.getAnalyseParam().getSearch())));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

}
