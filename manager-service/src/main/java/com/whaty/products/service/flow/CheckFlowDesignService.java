package com.whaty.products.service.flow;

import com.whaty.products.service.flow.domain.CheckFlowPage;
import com.whaty.products.service.flow.domain.config.CheckFlowConfigNode;
import com.whaty.products.service.flow.domain.config.CheckFlowConfig;

import java.util.Map;

/**
 * 审核流程设计
 *
 * @author weipengsen
 */
public interface CheckFlowDesignService {

    /**
     * 获取组信息
     * @param groupId
     * @return
     */
    CheckFlowConfig getCheckFlowGroup(String groupId);

    /**
     * 列举所有的角色
     * @param page
     * @return
     */
    Map<String, Object> listRoles(CheckFlowPage page);

    /**
     * 列举所有管理员
     * @param page
     * @return
     */
    Map<String, Object> listManagers(CheckFlowPage page);

    /**
     * 保存审核人节点
     * @param node
     */
    void saveNode(CheckFlowConfigNode node);

    /**
     * 添加第一个节点
     * @param groupId
     */
    void addFirstNode(String groupId);

    /**
     * 添加节点
     * @param groupId
     * @param previousId
     */
    void addNodeAfter(String groupId, String previousId);

    /**
     * 删除节点
     * @param groupId
     * @param nodeId
     */
    void deleteNode(String groupId, String nodeId);

    /**
     * 保存抄送人
     * @param config
     */
    void saveCopyPerson(CheckFlowConfig config);

    /**
     * 是否有效
     * @param groupId
     * @return
     */
    Boolean isActive(String groupId);
}
