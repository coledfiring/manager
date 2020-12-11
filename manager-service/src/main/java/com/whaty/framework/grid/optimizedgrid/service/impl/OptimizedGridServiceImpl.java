package com.whaty.framework.grid.optimizedgrid.service.impl;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.search.GridSearchSql;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.grid.optimizedgrid.searchsql.OptimizedGridSearchSql;

import java.util.Arrays;

/**
 * list优化grid操作类
 * @author weipengsen
 */
public class OptimizedGridServiceImpl<T extends AbstractBean> extends TycjGridServiceAdapter<T> {

    @Override
    public GridSearchSql getGridSearchSql(Page pageParam, GridConfig gridConfig) {
        if (this.isOptimizedList()) {
            return new OptimizedGridSearchSql(this.getSqlScopeManager(),
                    gridConfig, pageParam.getSearchItem(),
                    pageParam.getOrderItem() == null ? null : Arrays.asList(pageParam.getOrderItem()));
        } else {
            return new GridSearchSql(this.getSqlScopeManager(),
                    gridConfig, pageParam.getSearchItem(),
                    pageParam.getOrderItem() == null ? null : Arrays.asList(pageParam.getOrderItem()));
        }
    }

    /**
     * 钩子方法，是否是优化list查询，用于通用service继承后可以动态切换是否使用优化list查询
     * @return
     */
    protected boolean isOptimizedList() {
        return true;
    }

}
