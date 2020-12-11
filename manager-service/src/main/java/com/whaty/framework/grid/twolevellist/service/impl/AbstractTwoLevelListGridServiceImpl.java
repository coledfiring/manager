package com.whaty.framework.grid.twolevellist.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.grid.builder.MenuBuilder;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 二级列表查询service，只有前台传递父列表id的情况下才需要继承
 * @param <T>
 * @author weipengsen
 */
public abstract class AbstractTwoLevelListGridServiceImpl<T extends AbstractBean> extends TycjGridServiceAdapter<T> {

    /**
     * 高级搜索符，等于
     */
    private static final String SENIOR_SEARCH_EQUAL = " = ";

    @Override
    public void initGrid(GridConfig gridConfig, Map<String, Object> mapParam) {
        if (MapUtils.isNotEmpty(mapParam) && (mapParam.get("isShowBack") == null ||
                Boolean.valueOf(mapParam.get("isShowBack").toString()))) {
            gridConfig.getGridMenuList().add(0, MenuBuilder.buildBackMenu());
        }
    }

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map<String, Object> mapParam) {
        if (pageParam.getSearchItem() == null) {
            pageParam.setSearchItem(new HashMap<>(16));
        }
        if (mapParam.get(CommonConstant.PARAM_PARENT_ID) != null
                && StringUtils.isNotBlank(this.getParentIdSearchParamName())) {
            pageParam.getSearchItem().put(this.getParentIdSearchParamName(), SENIOR_SEARCH_EQUAL +
                    String.valueOf(mapParam.get(CommonConstant.PARAM_PARENT_ID)));
            pageParam.setSearchItem(ParamsDataModel.convertNameWithPoint(pageParam.getSearchItem()));
        }
        return super.list(pageParam, gridConfig, mapParam);
    }

    @Override
    public void checkBeforeAdd(T bean, Map<String, Object> params) throws EntityException {
        this.addParentIdToBean(bean, params);
        super.checkBeforeAdd(bean, params);
    }

    /**
     * 将parentId放入bean中
     * @param bean
     * @param params
     */
    private void addParentIdToBean(T bean, Map<String, Object> params) {
        // 把一级列表的id传递到bean中
        try {
            if (StringUtils.isNotBlank(this.getParentIdBeanPropertyName())
                    && params.containsKey(CommonConstant.PARAM_PARENT_ID)) {
                CommonUtils.setPropertyToBean(bean, this.getParentIdBeanPropertyName(),
                        String.valueOf(params.get(CommonConstant.PARAM_PARENT_ID)));
            }
        } catch (Exception e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 子类继承返回一级列表中传递的id，在二级列表中对应的搜索条件的名称，两个继承方法在dc查询时相同，在sql查询时不同，
     * 如果不需要集成增删改查则不需要getParentIdBeanPropertyName，返回null即可
     * @return
     */
    protected abstract String getParentIdSearchParamName();

    /**
     * 子类继承返回一级列表中传递的id，在二级列表中对应的bean的属性
     * @return
     */
    protected abstract String getParentIdBeanPropertyName();

}
