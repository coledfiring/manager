package com.whaty.products.service.analyse.impl;

import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 抽象的二级服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("limitTwoLevelService")
public class LimitTwoLevelServiceImpl extends AbstractTwoLevelListGridServiceImpl {

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map mapParam) {
        Map<String, Object> searchItem = (Map<String, Object>) mapParam.get("search");
        if (MapUtils.isNotEmpty(searchItem)) {
            if (pageParam.getSearchItem() == null) {
                pageParam.setSearchItem(searchItem);
            } else {
                pageParam.getSearchItem().putAll(searchItem);
            }
        }
        return super.list(pageParam, gridConfig, mapParam);
    }

    @Override
    protected String getParentIdSearchParamName() {
        return null;
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return null;
    }
}
