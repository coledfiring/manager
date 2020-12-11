package com.whaty.framework.aop.operatelog.param;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.grid.controller.GridBaseController;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * paramsDataModel的参数提取状态机
 *
 * @author weipengsen
 */
@Lazy
@Component("paramsDataModelParamState")
public class ParamsDataModelParamState implements AbstractParamState<ParamsDataModel> {

    private final static String PARAM_NAME_BEAN = "bean.%s";

    private final static String PARAM_NAME_PAGE = "page.%s";

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> extract(ParamsDataModel param) {
        if (MapUtils.isEmpty(param.getParams())) {
            return null;
        }
        if (param.getParams().containsKey(CommonConstant.PARAM_IDS)) {
            GridBaseController controller = (GridBaseController) SpringUtil.getApplicationContext()
                    .getBean("gridBaseController");
            param.getParams().put(CommonConstant.PARAM_IDS, controller.getIds(param));
        }
        if (param.getBean() != null) {
            param.getParams().putAll(CommonUtils.convertObjectToMap(param.getBean()).entrySet().stream()
                    .collect(Collectors.toMap(e -> String.format(PARAM_NAME_BEAN, e.getKey()), Map.Entry::getValue)));
            if (StringUtils.isNotBlank(param.getBean().getId())) {
                param.getParams().put(CommonConstant.PARAM_IDS, param.getBean().getId());
            }
        }
        if (param.getPage() != null) {
            param.getParams().putAll(CommonUtils.convertObjectToMap(param.getPage()).entrySet().stream()
                    .collect(Collectors.toMap(e -> String.format(PARAM_NAME_PAGE, e.getKey()), Map.Entry::getValue)));
        }
        return param.getParams();
    }

}
