package com.whaty.core.framework.service.siteManager.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.api.exception.ApiException;
import com.whaty.core.framework.bean.CorePePriRole;
import com.whaty.core.framework.dto.TreeNode;
import com.whaty.core.framework.service.PermissionManageService;
import com.whaty.core.framework.service.siteManager.ManagerControlDataOperateService;
import com.whaty.core.framework.service.siteManager.constant.SiteManagerConst;
import com.whaty.core.framework.util.UUIDUtil;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PePriRole;
import com.whaty.framework.exception.ServiceException;
import com.whaty.util.CommonUtils;
import com.whaty.utils.HibernatePluginsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 站点超管操作control数据服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("managerControlDataOperateService")
public class ManagerControlDataOperateServiceImpl implements ManagerControlDataOperateService {

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Autowired
    private PermissionManageService permissionManageService;

    @Override
    public CorePePriRole savePePriRole(CorePePriRole corePePriRole, String siteCode) {
        List peWebSiteList = this.openGeneralDao.getBySQL("select id from pe_web_site where code='" + siteCode + "'");
        if (CollectionUtils.isEmpty(peWebSiteList)) {
            throw new ServiceException(CommonConstant.PARAM_ERROR);
        }
        String siteId = peWebSiteList.get(0).toString();
        //roleType设为普通管理员且不是站点超管
        String roleTypeId = (String) openGeneralDao.getBySQL("select id from enum_const " +
                "where nameSpace='FlagRoleType' and code='3'").get(0);
        String isSiteSuperAdminId = (String) openGeneralDao.getBySQL("select id from enum_const " +
                "where nameSpace='FlagSiteSuperAdmin' and code='0'").get(0);
        //获取当前最大的角色code
        StringBuilder sql = new StringBuilder();
        sql.append(" select max(role.code + 1) from pe_pri_role role");
        List<Object> maxCodeList = openGeneralDao.getBySQL(sql.toString());
        int maxCode = 1;
        if (CollectionUtils.isNotEmpty(maxCodeList) && maxCodeList.get(0) != null) {
            maxCode = Double.valueOf(String.valueOf(maxCodeList.get(0))).intValue();
        }
        String id = UUIDUtil.getUUID().replace("-", "");
        sql.delete(0, sql.length());
        sql.append("  insert into pe_pri_role (             ");
        sql.append("    id,                                 ");
        sql.append("    name,                               ");
        sql.append("    code,                               ");
        sql.append("    flag_role_type,                     ");
        sql.append("    fk_web_site_id,                     ");
        sql.append("    flag_site_super_admin,              ");
        sql.append("    site_code                           ");
        sql.append("  )                                     ");
        sql.append("  VALUES                                ");
        sql.append("  (                                     ");
        sql.append("    '" + id + "',                       ");
        sql.append("    '" + corePePriRole.getName() + "',  ");
        sql.append("    '" + maxCode + "',                  ");
        sql.append("    '" + roleTypeId + "',               ");
        sql.append("    '" + siteId + "',                   ");
        sql.append("    '" + isSiteSuperAdminId + "',       ");
        sql.append("    '" + siteCode + "'                  ");
        sql.append("  )                                     ");
        openGeneralDao.executeBySQL(sql.toString());
        corePePriRole.setCode(String.valueOf(maxCode));
        corePePriRole.setId(id);
        return corePePriRole;
    }

    @Override
    public void updatePePriRole(PePriRole pePriRole) throws ServiceException {
        String currentType = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        openGeneralDao.executeBySQL("update pe_pri_role set name = '" + pePriRole.getName() + "' where id = '"
                + pePriRole.getId() + "'");
        MasterSlaveRoutingDataSource.setDbType(currentType);
    }

    @Override
    public void deletePePriRoleByIds(List idList) throws EntityException {
        String currentType = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        if (HibernatePluginsUtil.validateReferencingOpenSession(CorePePriRole.class, CommonUtils.join(idList, ",", null))) {
            throw new EntityException(SiteManagerConst.FOREIGN_KEY_ERROR);
        }
        openGeneralDao.executeBySQL("delete from pe_pri_role where " + CommonUtils.madeSqlIn(idList, "id"));
        MasterSlaveRoutingDataSource.setDbType(currentType);
    }

    @Override
    public Object getWebSiteId(String siteCode) {
        String currentType = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List peWebSiteList = this.openGeneralDao.getBySQL("select id from pe_web_site where code='" + siteCode + "'");
        MasterSlaveRoutingDataSource.setDbType(currentType);
        if (CollectionUtils.isNotEmpty(peWebSiteList)) {
            return peWebSiteList.get(0);
        } else {
            throw new ServiceException(CommonConstant.PARAM_ERROR);
        }
    }

    @Override
    public List getSiteMenuTreeWithSubMenu(String siteId) throws ApiException {
        String currentType = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<TreeNode> nodeList = this.permissionManageService.getSiteMenuTreeWithSubMenu(siteId, true, true);
        MasterSlaveRoutingDataSource.setDbType(currentType);
        //筛选出除管理员管理之外的目录
        return nodeList.stream()
                .filter(treeNode -> !"管理员管理".equals(treeNode.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List getRoleMenuIds(String siteId, String roleId) {
        String currentType = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List menuIds = this.permissionManageService.getRoleMenuIds(siteId, roleId);
        MasterSlaveRoutingDataSource.setDbType(currentType);
        return menuIds;
    }

    @Override
    public List getRoleGridMenuIds(String siteId, String roleId) {
        String currentType = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List menuIds = this.permissionManageService.getRoleGridMenuIds(siteId, roleId);
        MasterSlaveRoutingDataSource.setDbType(currentType);
        return menuIds;
    }

    @Override
    public void saveRoleMenuIds(String siteId, String roleId, List<String> menuIdList, List<Integer> gridMenuIdList)
            throws ApiException {
        this.permissionManageService.saveRoleMenuIds(siteId, roleId, menuIdList, gridMenuIdList);
    }
}
