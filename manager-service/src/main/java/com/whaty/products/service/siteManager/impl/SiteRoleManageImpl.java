package com.whaty.products.service.siteManager.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.util.CommonUtils;
import com.whaty.core.framework.bean.CorePePriRole;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.service.siteManager.ManagerControlDataOperateService;
import com.whaty.core.framework.service.siteManager.constant.SiteManagerConst;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PePriRole;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 站点角色管理服务类
 * @author suoqiangqiang
 */
@Lazy
@Service("siteRoleManageService")
public class SiteRoleManageImpl extends TycjGridServiceAdapter<PePriRole> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = "managerControlDataOperateService")
    private ManagerControlDataOperateService managerControlDataOperateService;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public void initGrid(GridConfig gridConfig, Map<String, Object> mapParam) {
        String roleId = userService.getCurrentUser().getRole().getId();
        List roleList = myGeneralDao
                .getBySQL("select 1 from pe_pri_role role " +
                        "INNER JOIN enum_const em on em.id=role.FLAG_ROLE_TYPE AND em.code='9998' " +
                        "where role.id = '" + roleId + "'");
        //只有站点超管用户可以操作此列表
        if (CollectionUtils.isEmpty(roleList)) {
            throw new ServiceException(SiteManagerConst.NO_PRIORITY_ERROR);
        }
    }

    @Override
    public Map update(PePriRole bean, GridConfig gridConfig) {
        try {
            List roleList = myGeneralDao
                    .getBySQL("select 1 from pe_pri_role where name = ? and site_code = ? and id <> ?",
                            bean.getName(), SiteUtil.getSiteCode(), bean.getId());
            if (CollectionUtils.isNotEmpty(roleList)) {
                return CommonUtils.createFailInfoMap("更新失败，角色已存在");
            }
            PePriRole pePriRoleRaw = myGeneralDao.getById(PePriRole.class, bean.getId());
            managerControlDataOperateService.updatePePriRole(bean);
            try {
                if (myGeneralDao
                        .executeBySQL("update pe_pri_role set name = '" + bean.getName() + "' where id = '" + bean.getId() + "'") != 1) {
                    throw new ServiceException(CommonConstant.ERROR_STR);
                }
            } catch (Exception e) {
                //更新角色到业务库失败，从管理平台还原更新
                managerControlDataOperateService.updatePePriRole(pePriRoleRaw);
                return CommonUtils.createFailInfoMap("更新失败，角色不存在");
            }
            return CommonUtils.createSuccessInfoMap("更新成功");
        } catch (Exception e) {
            return CommonUtils.createFailInfoMap(String.format("更新失败，%s", CommonConstant.ERROR_STR));
        }
    }

    @Override
    public Map add(PePriRole bean, Map<String, Object> params, GridConfig gridConfig) {
        try {
            List roleList = myGeneralDao.getBySQL("select 1 from pe_pri_role where name = ? and site_code = ?",
                    bean.getName(), SiteUtil.getSiteCode());
            if (CollectionUtils.isNotEmpty(roleList)) {
                return CommonUtils.createFailInfoMap("添加失败，角色已存在");
            }
            CorePePriRole corePePriRole = new CorePePriRole();
            corePePriRole.setName(bean.getName());
            //保存角色到管理平台
            corePePriRole = managerControlDataOperateService.savePePriRole(corePePriRole, SiteUtil.getSiteCode());
            try {
                EnumConst roleType = myGeneralDao.getEnumConstByNamespaceCode("FlagRoleType", "3");
                EnumConst flagSiteSuperAdmin = myGeneralDao.getEnumConstByNamespaceCode("FlagSiteSuperAdmin", "0");
                bean.setId(corePePriRole.getId());
                bean.setEnumConstByFlagRoleType(roleType);
                bean.setCode(corePePriRole.getCode());
                StringBuilder sql = new StringBuilder();
                sql.append("  insert into pe_pri_role (             ");
                sql.append("    id,                                 ");
                sql.append("    name,                               ");
                sql.append("    code,                               ");
                sql.append("    flag_role_type,                     ");
                sql.append("    flag_site_super_admin,              ");
                sql.append("    site_code                           ");
                sql.append("  )                                     ");
                sql.append("  VALUES                                ");
                sql.append("  (                                     ");
                sql.append("    '" + corePePriRole.getId() + "',    ");
                sql.append("    '" + corePePriRole.getName() + "',  ");
                sql.append("    '" + corePePriRole.getCode() + "',  ");
                sql.append("    '" + roleType.getId() + "',         ");
                sql.append("    '" + flagSiteSuperAdmin.getId() + "',");
                sql.append("    '" + SiteUtil.getSiteCode() + "'    ");
                sql.append("  )                                     ");
                myGeneralDao.executeBySQL(sql.toString());
            } catch (Exception e) {
                //保存角色到业务库失败，从管理平台删除该角色
                managerControlDataOperateService.deletePePriRoleByIds(Arrays.asList(corePePriRole.getId()));
                return CommonUtils.createFailInfoMap("添加失败");
            }
            return CommonUtils.createSuccessInfoMap("添加成功");
        } catch (Exception e) {
            return CommonUtils.createFailInfoMap("添加失败");
        }
    }

    @Override
    public void afterDelete(List list) throws EntityException {
        try {
            //从管理平台删除该角色
            managerControlDataOperateService.deletePePriRoleByIds(list);
        } catch (EntityException e) {
            throw new EntityException(e.getMessage());
        } catch (Exception e) {
            throw new EntityException(CommonConstant.ERROR_STR);
        }
    }
}
