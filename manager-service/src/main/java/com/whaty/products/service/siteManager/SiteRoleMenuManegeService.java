package com.whaty.products.service.siteManager;

import com.whaty.core.framework.api.exception.ApiException;

import java.util.List;

/**
 * 站点业务库角色菜单管理服务类
 * @author suoqiangqiang
 */
public interface SiteRoleMenuManegeService {

    /**
     * 获取站点所有角色
     * @param siteId
     */
    List getAllRole(String siteId) throws ApiException;

    /**
     * 检查是否有访问该接口的权限
     */
    boolean checkIsHavePermission() throws ApiException;
}
