package com.whaty.core.framework.service.siteManager;

import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.api.exception.ApiException;
import com.whaty.core.framework.bean.CorePePriRole;
import com.whaty.domain.bean.PePriRole;

import java.util.List;

/**
 * 站点超管操作control数据服务类
 * @author suoqiangqiang
 */
public interface ManagerControlDataOperateService {
    /**
     * 保存角色
     * @param corePePriRole
     * @param siteCode
     * @return
     */
    CorePePriRole savePePriRole(CorePePriRole corePePriRole, String siteCode);

    /**
     * 更新角色
     * @param pePriRole
     */
    void updatePePriRole(PePriRole pePriRole);

    /**
     * 批量删除角色
     * @param idList
     * @throws EntityException
     */
    void deletePePriRoleByIds(List idList) throws EntityException;

    /**
     * 获取站点pe_web_site的id
     * @param siteCode
     */
    Object getWebSiteId(String siteCode);

    /**
     * 获取当前站点菜单树
     * @param siteId
     * @throws ApiException
     * @return
     */
    List getSiteMenuTreeWithSubMenu(String siteId) throws ApiException;

    /**
     * 获取当前角色菜单
     * @param siteId
     * @param roleId
     * @return
     */
    List getRoleMenuIds(String siteId, String roleId);

    /**
     * 获取当前角色菜单权限
     * @param siteId
     * @param roleId
     * @return
     */
    List getRoleGridMenuIds(String siteId, String roleId);

    /**
     * 保存当前角色菜单
     * @param siteId
     * @param roleId
     * @param menuIdList
     * @param gridMenuIdList
     * @throws ApiException
     */
    void saveRoleMenuIds(String siteId, String roleId, List<String> menuIdList, List<Integer> gridMenuIdList)
            throws ApiException;
}
