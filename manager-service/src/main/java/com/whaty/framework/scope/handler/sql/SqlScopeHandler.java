package com.whaty.framework.scope.handler.sql;

import com.whaty.core.framework.grid.search.scope.sql.AbstractSqlScope;
import com.whaty.core.framework.grid.search.scope.sql.Scope;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.framework.scope.constants.ScopeEnum;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 横向权限处理类
 * @author weipengsen
 */
@Service
public class SqlScopeHandler extends AbstractSqlScope {

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public String getScope(Scope scope) {
        if (!ScopeEnum.hasAlias(scope.getTableAlias())) {
            return null;
        }
        List<String> ids = this.getScopeIds(scope.getTableAlias());
        return CollectionUtils.isNotEmpty(ids) ? this.getSqlCondition(scope.getTableColumnAlias(), ids) : "";
    }

    private List<String> getScopeIds(String alias) {
        return ScopeHandleUtils.getScopeIdsByTableAlias(alias, this.userService.getCurrentUser().getId());
    }
}
