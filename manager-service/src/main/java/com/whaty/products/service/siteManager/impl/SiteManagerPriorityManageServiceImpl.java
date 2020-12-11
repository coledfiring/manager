package com.whaty.products.service.siteManager.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.exception.ApiException;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.function.Functions;
import com.whaty.handler.tree.TreeNode;
import com.whaty.handler.tree.TreeUtils;
import com.whaty.products.service.siteManager.SiteManagerPriorityManageService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 站点用户权限管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("siteManagerPriorityManageService")
public class SiteManagerPriorityManageServiceImpl implements SiteManagerPriorityManageService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public Set<TreeNode> getAllPriorityData() {
        List<Map<String, Object>> siteList = myGeneralDao
                .getMapBySQL("select id, fk_parent_id as parentId, name as label from pe_unit where site_code = ?",
                        MasterSlaveRoutingDataSource.getDbType());
        return TreeUtils.buildTree(siteList);
    }

    @Override
    public void saveAllPriorityData(String managerId, List<String> unitIds) {
        TycjParameterAssert.isAllNotBlank(managerId);
        String userId = this.myGeneralDao.getOneBySQL("select fk_sso_user_id from pe_manager where id = ?",
                managerId);
        this.myGeneralDao.executeBySQL("delete from pr_pri_manager_unit where fk_sso_user_id = ?", userId);
        StringBuilder sql = new StringBuilder();
        if (CollectionUtils.isNotEmpty(unitIds)) {
            sql.append("insert into pr_pri_manager_unit(fk_sso_user_id, fk_item_id) values ");
            sql.append(unitIds.stream().map(e -> " ('" + userId + "', '" + e + "'),")
                    .reduce("", Functions.stringAppend));
            sql.delete(sql.lastIndexOf(","), sql.length());
            myGeneralDao.executeBySQL(sql.toString());
        }
        ScopeHandleUtils.updateScopeCache(userId);
    }

    @Override
    public boolean checkIsHavePermission() throws ApiException {
        String roleId = userService.getCurrentUser().getRole().getId();
        return myGeneralDao.checkNotEmpty("select 1 from pe_pri_role role " +
                        "INNER JOIN enum_const em on em.id=role.FLAG_ROLE_TYPE AND em.code='9998' " +
                        "where role.id = ?", roleId);
    }

    /**
     * 查询数据转为treeNode
     *
     * @param treeMapList
     * @return
     */
    private List<com.whaty.core.framework.dto.TreeNode> mapToTreeNote(List<Map<String, Object>> treeMapList) {
        List<com.whaty.core.framework.dto.TreeNode> treeNodeList = new ArrayList<>();
        treeMapList.forEach(treeMap -> {
            com.whaty.core.framework.dto.TreeNode tree = new com.whaty.core.framework.dto.TreeNode();
            tree.setId((String) treeMap.get("id"));
            tree.setName((String) treeMap.get("name"));
            tree.setChildren(new ArrayList<>());
            tree.setIsLeaf("1");
            treeNodeList.add(tree);
        });
        return treeNodeList;
    }

    @Override
    public Map<String, List> getCheckedPriorityIdList(String managerId) {
        TycjParameterAssert.isAllNotBlank(managerId);
        Map<String, List> resultMap = new HashMap<>(2);
        resultMap.put("checkUnitList", myGeneralDao
                .getBySQL("select fk_item_id from pr_pri_manager_unit pr " +
                                "inner join pe_manager m on m.fk_sso_user_id = pr.fk_sso_user_id where m.id = ?",
                        managerId));
        return resultMap;
    }

    @Override
    public String getUserName(String managerId) {
        TycjParameterAssert.isAllNotBlank(managerId);
        return myGeneralDao.getOneBySQL(" select true_name as name from pe_manager where id = ?", managerId);
    }
}
