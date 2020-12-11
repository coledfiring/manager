package com.whaty.analyse.framework;

import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.scope.constants.ScopeEnum;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.handler.tree.TreeUtils;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;

import java.util.List;
import java.util.Map;

/**
 * 统计条件工具
 *
 * @author weipengsen
 */
public class AnalyseConditionUtils {

    /**
     * 列举有效单位
     * @return
     */
    public static List<Map<String, Object>> listActiveUnit(Map<String, Object> params) {
        String currentCode = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            List<String> activeUnitIds = ScopeHandleUtils.getScopeIdsByBeanAlias(ScopeEnum.peUnit.getBeanAlias(),
                    UserUtils.getCurrentUserId());
            List<Map<String, Object>> unitList = StaticBeanUtils.getOpenGeneralDao()
                    .getMapBySQL("select id, fk_parent_id as parentId, name as label from pe_unit where site_code = ?",
                            SiteUtil.getSiteCode());
            return TreeUtils.extractActiveNode(unitList, activeUnitIds);
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentCode);
        }
    }

}
