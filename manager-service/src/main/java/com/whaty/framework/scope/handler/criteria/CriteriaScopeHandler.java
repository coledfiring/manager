package com.whaty.framework.scope.handler.criteria;

import com.whaty.core.framework.grid.search.scope.criteria.AbstractCriteriaScope;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.framework.scope.constants.ScopeEnum;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 横向权限处理类
 * @author weipengsen
 */
@Service
public class CriteriaScopeHandler extends AbstractCriteriaScope {

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public void setScope(DetachedCriteria criteria, String beanAlias, Criteria subCriteria) {
        if (ScopeEnum.hasBean(beanAlias)) {
            List<String> ids = ScopeHandleUtils.getScopeIdsByBeanAlias(beanAlias,
                    this.userService.getCurrentUser().getId());
            if (CollectionUtils.isNotEmpty(ids)) {
                criteria.add(Restrictions.in(subCriteria != null ? subCriteria.getAlias() + ".id" : "id", ids));
            }

        }
    }

}
