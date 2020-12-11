package com.whaty.custom.helper;

import com.whaty.custom.constant.AnalyseConditionEnum;
import com.whaty.custom.constant.AnalyseConstant;
import com.whaty.framework.exception.ParameterIllegalException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 自定义统计辅助类
 * @author weipengsen
 */
public class CustomAnalyseHelper {

    /**
     * 生成配置
     * @param chartsType
     * @param charts
     * @param conditions
     * @return
     */
    public Map<String, Object> generateAnalyseConfig(String chartsType, Map<String, Object> charts,
                                        List<Map<String, Object>> conditions) {
        Map<String, Object> config = new HashMap<>(16);
        config.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_SELECTED_CHARTS, chartsType);
        // 横轴维度
        List<String> xGroupProp = new LinkedList<>();
        List<Map<String, Object>> xGroup = (List<Map<String, Object>>) charts.get(AnalyseConstant.PARAM_X_GROUP);
        xGroup.forEach(e -> xGroupProp.add(String.valueOf(e.get(AnalyseConstant.CONDITION_MULTI_OPTION_ID))));
        config.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_X_GROUP, xGroupProp);
        // 统计项
        Map<String, Object> analyses = (Map<String, Object>) charts.get(AnalyseConstant.PARAM_ANALYSES);
        config.put(AnalyseConstant.ANALYSE_CONFIG_PROPERTY_ANALYSES,
                analyses.get(AnalyseConstant.CONDITION_MULTI_OPTION_ID));
        // 条件
        List<Map<String, Object>> conditionsProp = new LinkedList<>();
        config.put(AnalyseConstant.PARAM_CONDITIONS, conditionsProp);
        conditions.forEach(e -> {
            Map<String, Object> condition = new HashMap<>(16);
            conditionsProp.add(condition);
            condition.put(AnalyseConstant.CONDITION_MULTI_OPTION_ID,
                    String.valueOf(e.get(AnalyseConstant.CONDITION_MULTI_OPTION_ID)));
            switch (AnalyseConditionEnum.getByCode((String) e.get(AnalyseConstant.CONDITION_ARG_TYPE))) {
                case multiSelect:
                    List<Object> values = (List<Object>) e.get(AnalyseConstant.CONDITION_VALUE);
                    List<String> selectedOption = new LinkedList<>();
                    values.forEach(v -> selectedOption.add(String.valueOf(v)));
                    condition.put(AnalyseConstant.CONDITION_VALUE, selectedOption);
                    break;
                case input:
                case singleSelect:
                    condition.put(AnalyseConstant.CONDITION_VALUE, String.valueOf(e
                            .get(AnalyseConstant.CONDITION_VALUE)));
                    break;
                default:
                    throw new ParameterIllegalException();
            }
        });
        return config;
    }

}
