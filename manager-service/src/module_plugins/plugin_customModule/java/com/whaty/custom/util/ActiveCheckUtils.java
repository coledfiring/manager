package com.whaty.custom.util;

import com.whaty.constant.SiteConstant;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.utils.StaticBeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 判断节点是否有效的工具类
 * @author weipengsen
 */
public class ActiveCheckUtils {

    /**
     * 检查指定的actionId是否有效
     * @return
     */
    public static boolean checkActionIdActive(String actionId) throws RuntimeException {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT                                                                 ");
            sql.append(" 	pri.id AS id                                                        ");
            sql.append(" FROM                                                                   ");
            sql.append(" 	pe_pri_category pri                                                 ");
            sql.append(" INNER JOIN pe_base_category base ON base.id = pri.fk_base_category_id  ");
            sql.append(" WHERE                                                                  ");
            sql.append(" 	base.fk_grid_id = '").append(actionId).append("'                    ");
            sql.append(" AND pri.fk_web_site_id = '").append(SiteUtil.getSiteId()).append("'    ");
            List<Object> categoryIds = StaticBeanUtils.getControlGeneralDao().getBySQL(sql.toString());
            if (CollectionUtils.isEmpty(categoryIds)) {
                return false;
            }
            String categoryId = String.valueOf(categoryIds.get(0));
            return checkCategoryActive(categoryId);
        } finally {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
        }
    }

    /**
     * 检查节点id是否有效
     * @param categoryId
     * @return
     */
    private static boolean checkCategoryActive(String categoryId) {
        String sql = "SELECT isActive AS active, FK_PARENT_ID AS parent FROM pe_pri_category WHERE ID = '"
                + categoryId + "'";
        List<Map<String, Object>> data = StaticBeanUtils.getControlGeneralDao().getMapList(sql, null);
        if(CollectionUtils.isEmpty(data)) {
            return false;
        }
        Map<String, Object> category = data.get(0);
        boolean active = "1".equals(category.get("active"));
        String parentCategoryId = category.get("parent") == null ? null
                : String.valueOf(category.get("parent"));
        if(active && StringUtils.isNotBlank(parentCategoryId)) {
            return checkCategoryActive(parentCategoryId);
        } else {
            return active;
        }
    }

}
