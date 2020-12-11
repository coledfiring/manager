package com.whaty.products.service.siteManager;

import com.whaty.core.framework.api.exception.ApiException;
import com.whaty.handler.tree.TreeNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 站点用户权限管理服务类
 * @author suoqiangqiang
 */
public interface SiteManagerPriorityManageService {

    /**
     * 获取所有权限信息
     * @return
     */
    Set<TreeNode> getAllPriorityData();

    /**
     * 保存所有权限信息
     * @param managerId
     * @param unitIds
     */
    void saveAllPriorityData(String managerId, List<String> unitIds);

    /**
     * 检查是否有访问该接口的权限
     * @return
     * @throws ApiException
     */
    boolean checkIsHavePermission() throws ApiException;

    /**
     * 获取已勾选的权限信息
     * @param managerId
     * @return
     */
    Map<String, List> getCheckedPriorityIdList(String managerId);

    /**
     * 获取管理员用户名
     * @param managerId
     * @return
     */
    String getUserName(String managerId);

}
