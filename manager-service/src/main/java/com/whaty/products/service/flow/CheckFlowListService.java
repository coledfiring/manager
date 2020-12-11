package com.whaty.products.service.flow;

import java.util.Map;

/**
 * 审核列表
 *
 * @author weipengsen
 */
public interface CheckFlowListService {

    /**
     * 获取审核流程
     * @param checkStatus
     * @param needCheck
     * @param flowType
     * @param search
     * @param showType
     * @param page
     * @return
     */
    Map<String, Object> getCheckFlow(String checkStatus, String needCheck, String flowType, String search,
                                     String showType, Map<String, Object> page);

    /**
     * 统计待我审核的项目数量
     * @return
     */
    int countWaitCheck();

}
