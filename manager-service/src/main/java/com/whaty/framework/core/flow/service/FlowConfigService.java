package com.whaty.framework.core.flow.service;

import com.whaty.framework.core.flow.domain.Flow;

import java.util.List;
import java.util.Map;

/**
 * 流程配置服务类接口
 *
 * @author weipengsen
 */
public interface FlowConfigService {

    /**
     * 列举流程配置
     * @return
     */
    List<Map<String, Object>> listFlowConfig();

    /**
     * 获取流程图配置
     * @param id
     * @return
     */
    Flow getFlowConfig(String id);

    /**
     * 获取菜单和按钮
     * @return
     */
    Map<String,Map> listCategoryAndMenu();

    /**
     * 保存流程图
     * @param flow
     * @return
     */
    Flow saveFlowConfig(Flow flow);

    /**
     * 删除流程图
     * @param id
     */
    void deleteFlowConfig(String id);

    /**
     * 获取展示的流程图配置
     * @param flowId
     * @return
     */
    Flow getShowFlowConfig(String flowId);
}
