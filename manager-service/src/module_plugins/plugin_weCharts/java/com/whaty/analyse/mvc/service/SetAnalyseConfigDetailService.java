package com.whaty.analyse.mvc.service;

import java.util.Map;

/**
 * 设置统计配置详情
 *
 * @author weipengsen
 */
public interface SetAnalyseConfigDetailService {

    /**
     * 通过id获取配置
     * @param id
     * @return
     */
    Map<String, Object> getConfigById(String id);

    /**
     * 保存配置
     * @param id
     * @param typeCode
     * @param config
     */
    void saveConfig(String id, String typeCode, String config);
}
