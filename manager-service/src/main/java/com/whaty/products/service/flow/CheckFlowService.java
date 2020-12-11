package com.whaty.products.service.flow;

import com.whaty.products.service.flow.domain.CustomCheckFlowParams;
import com.whaty.products.service.flow.domain.config.CheckFlowConfig;

import java.util.Map;

/**
 * 审核流程操作
 *
 * @author weipengsen
 */
public interface CheckFlowService {

    /**
     * 发起审核流程
     * @param itemId
     * @param type
     * @throws Exception
     */
    void doApply(String itemId, String type) throws Exception;

    /**
     * 获取审核流程配置
     * @param id
     * @param type
     * @return
     */
    Map<String, Object> getCheckFlowConfig(String id, String type);

    /**
     * 根据fk_item_id获取审批流程
     *
     * @param fkItemId
     * @param type
     * @return
     */
    Map<String, Object> getCheckFlowConfigByItemId(String fkItemId, String type);

    /**
     * 审核通过
     * @param id
     * @param currentNode
     * @param note
     * @throws Exception
     */
    void doPassCheck(String id, String currentNode, String note) throws Exception;

    /**
     * 驳回
     * @param id
     * @param currentNode
     * @param note
     * @throws Exception
     */
    void doNoPassCheck(String id, String currentNode, String note) throws Exception;

    /**
     * 撤回申请
     * @param id
     */
    void doCancelCheck(String id);

    /**
     * 获取基于自定义的审批配置
     *
     * @param type
     * @return
     */
    CheckFlowConfig getCheckFlowConfigWithCustom(String type);

    /**
     * 基于自定义审批的发起审批
     *
     * @param params
     * @throws Exception
     */
    void doApplyWithCustom(CustomCheckFlowParams params) throws Exception;

    /**
     * 是否可申请
     * @param itemId
     * @param type
     */
    void canApply(String itemId, String type);

}
