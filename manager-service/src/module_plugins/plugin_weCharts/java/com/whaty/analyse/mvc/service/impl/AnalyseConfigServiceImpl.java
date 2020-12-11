package com.whaty.analyse.mvc.service.impl;

import com.whaty.analyse.framework.AnalyseConfigManager;
import com.whaty.analyse.framework.AnalyseUtils;
import com.whaty.analyse.framework.domain.AbstractConfigVO;
import com.whaty.analyse.framework.domain.AnalyseParam;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 统计配置
 *
 * @author weipengsen
 */
@Lazy
@Service("analyseConfigService")
public class AnalyseConfigServiceImpl extends TycjGridServiceAdapter {

    /**
     * 获取统计数据
     * @param param
     * @return
     */
    public AbstractConfigVO getAnalyseData(AnalyseParam param) {
        try {
            TycjParameterAssert.validatePass(param);
            return new AnalyseConfigManager(param).getAnalyseData();
        } catch (Exception e) {
            throw new UncheckException(e);
        }
    }

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map mapParam) {
        Map<String, Object> search = (Map<String, Object>) mapParam.get("search");
        if (MapUtils.isNotEmpty(search)) {
            gridConfig.gridConfigSource().setSql(AnalyseUtils
                    .handleSql(gridConfig.gridConfigSource().getSql(), search));
        }
        return super.list(pageParam, gridConfig, mapParam);
    }
}
