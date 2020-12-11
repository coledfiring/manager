package com.whaty.products.service.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具类
 * @author weipengsen
 */
public class CascadeUtils {

    /**
     * 生成选择项
     * @param label
     * @param key
     * @param type
     * @param isMulti
     * @param required
     * @param isParam
     * @param dataType
     * @param locationItem
     * @param remoteUrl
     * @param cascadeItem
     * @param defaultValue
     * @param remoteParams
     * @return
     */
    public static Map<String, Object> generateItem(String label, String key, String type, boolean isMulti, boolean required,
                                                   boolean isParam, String dataType, List<Map<String, Object>> locationItem,
                                                   String remoteUrl, List<String> cascadeItem, String defaultValue,
                                                   Map<String, Object> remoteParams, String helper) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("label", label);
        map.put("key", key);
        map.put("type", type);
        map.put("isMulti", isMulti);
        map.put("required", required);
        map.put("isParam", isParam);
        map.put("dataType", dataType);
        map.put("locationItems", locationItem);
        map.put("remoteUrl", remoteUrl);
        map.put("cascadeItem", cascadeItem);
        map.put("defaultValue", defaultValue);
        map.put("remoteParams", remoteParams);
        map.put("helper", helper);
        return map;
    }

}
