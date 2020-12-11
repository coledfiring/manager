package com.whaty.analyse.framework.type.block;

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
 * 文本块数据查询状态
 *
 * @author weipengsen
 */
public class BlockSearchDataState extends AbstractState {

    private final AnalyseBasicConfig basicConfig;

    private final BlockConfigDO config;

    private final GeneralDao generalDao;

    public BlockSearchDataState(AnalyseBasicConfig basicConfig, AnalyseStateContext context) {
        super(context);
        this.basicConfig = basicConfig;
        this.config = (BlockConfigDO) basicConfig.getIConfigDO();
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public AbstractConfigVO handle() throws Exception {
        this.config.setData(this.searchData());
        return this.handleNextState(new AnalyseConvertState<>(this.context, this.config, BlockConfigVO.class));
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
            return this.config.getSql().stream()
                    .map(e -> this.generalDao.getOneMapBySQL(AnalyseUtils.handleSql(e,
                        this.basicConfig.getAnalyseParam().getSearch())))
                    .flatMap(e -> e.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            e -> Objects.nonNull(e.getValue()) ? e.getValue() : "-"));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSite);
        }
    }

}
