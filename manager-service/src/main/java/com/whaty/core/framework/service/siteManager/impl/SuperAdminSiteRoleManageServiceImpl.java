package com.whaty.core.framework.service.siteManager.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.bean.AbstractBean;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.util.CommonUtils;
import com.whaty.core.framework.bean.CorePePriRole;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.service.impl.GridServiceImpl;
import com.whaty.core.framework.service.siteManager.constant.SiteManagerConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.utils.HibernatePluginsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 站点超管角色管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("superAdminSiteRoleManageService")
public class SuperAdminSiteRoleManageServiceImpl extends GridServiceImpl<CorePePriRole> {
    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Override
    public Map update(CorePePriRole bean, GridConfig gridConfig) {
        AbstractBean dbInstance = this.generalDao.getById(bean.getClass(), bean.getId());
        TycjParameterAssert.isAllNotNull(dbInstance);
        this.mergeBeanByGridConfig(dbInstance, bean, gridConfig, true);
        List roleList = generalDao.getBySQL("select 1 from pe_pri_role where (name = '" + bean.getName()
                + "' or code = '" + bean.getCode() + "') " +
                "and id<>'" + bean.getId() + "' " +
                "and fk_web_site_id = '" + bean.getPeWebSite().getId() + "'");
        if (CollectionUtils.isNotEmpty(roleList)) {
            throw new ServiceException("更新失败，角色已存在");
        }
        roleList = generalDao.getBySQL("select 1 from pe_pri_role where fk_web_site_id = '"
                + bean.getPeWebSite().getId() + "' and id='" + bean.getId() + "'");
        if (CollectionUtils.isEmpty(roleList)) {
            throw new ServiceException("更新失败，站点不允许更改！");
        }
        this.generalDao.save(bean);
        MasterSlaveRoutingDataSource.setDbType(getUserWebSite(bean).getCode());
        StringBuilder sql = new StringBuilder();
        sql.append(" update pe_pri_role                                                  ");
        sql.append(" set                                                                 ");
        sql.append(" code = '" + bean.getCode() + "',                                        ");
        sql.append(" name = '" + bean.getName() + "',                                        ");
        sql.append(" FLAG_ROLE_TYPE = '" + bean.getEnumConstByFlagRoleType().getId() + "'    ");
        sql.append(" where id = '" + bean.getId() + "'                                       ");
        openGeneralDao.executeBySQL(sql.toString());
        return CommonUtils.createSuccessInfoMap("更新成功");
    }

    @Override
    public Map add(CorePePriRole bean, Map<String, Object> params, GridConfig gridConfig) {
        this.mergeBeanByGridConfig(bean, bean, gridConfig, false);
        PeWebSite site = getUserWebSite(bean);
        MasterSlaveRoutingDataSource.setDbType(site.getCode());
        List roleList = openGeneralDao.getBySQL("select 1 from pe_pri_role where name = '" + bean.getName() + "'");
        if (CollectionUtils.isNotEmpty(roleList)) {
            throw new ServiceException("添加失败，该角色已存在！");
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                      ");
        sql.append("   max(role.code + 1)                                        ");
        sql.append(" from                                                        ");
        sql.append("   pe_pri_role role                                          ");
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<Object> maxCodeList = openGeneralDao.getBySQL(sql.toString());
        int maxCode = 1;
        if (CollectionUtils.isNotEmpty(maxCodeList) && maxCodeList.get(0) != null) {
            maxCode = Double.valueOf(String.valueOf(maxCodeList.get(0))).intValue();
        }
        bean.setCode(String.valueOf(maxCode));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        sql.delete(0,sql.length());
        sql.append("  insert into pe_pri_role (                                            ");
        sql.append("    id,                                                                ");
        sql.append("    name,                                                              ");
        sql.append("    code,                                                              ");
        sql.append("    flag_role_type,                                                    ");
        sql.append("    fk_web_site_id,                                                    ");
        sql.append("    site_code                                                          ");
        sql.append("  )                                                                    ");
        sql.append("  VALUES                                                               ");
        sql.append("  (                                                                    ");
        sql.append("    '" + uuid + "',                                                    ");
        sql.append("    '" + bean.getName() + "',                                          ");
        sql.append("    '" + maxCode + "',                                                 ");
        sql.append("    '" + bean.getEnumConstByFlagRoleType().getId() + "',               ");
        sql.append("    '" + site.getId() + "',                                            ");
        sql.append("    '" + site.getCode() + "'                                           ");
        sql.append("  )                                                                    ");
        this.generalDao.executeBySQL(sql.toString());
        MasterSlaveRoutingDataSource.setDbType(site.getCode());
        sql.delete(0, sql.length());
        sql.append("  insert into pe_pri_role (                              ");
        sql.append("    id,                                                  ");
        sql.append("    name,                                                ");
        sql.append("    code,                                                ");
        sql.append("    site_code,                                           ");
        sql.append("    flag_role_type                                       ");
        sql.append("  )                                                      ");
        sql.append("  VALUES                                                 ");
        sql.append("  (                                                      ");
        sql.append("    '" + uuid + "',                                      ");
        sql.append("    '" + bean.getName() + "',                            ");
        sql.append("    '" + bean.getCode() + "',                            ");
        sql.append("    '" + MasterSlaveRoutingDataSource.getDbType() + "',  ");
        sql.append("    '" + bean.getEnumConstByFlagRoleType().getId() + "'  ");
        sql.append("  )                                                      ");
        openGeneralDao.executeBySQL(sql.toString());
        return CommonUtils.createSuccessInfoMap("添加成功");
    }

    @Override
    public Map delete(GridConfig gridConfig, String ids) {
        List idList = CommonUtils.convertIdsToList(ids);
        TycjParameterAssert.isAllNotEmpty(idList);
        List websiteList = openGeneralDao.getBySQL(" select DISTINCT fk_web_site_id from pe_pri_role where "
                + com.whaty.util.CommonUtils.madeSqlIn(idList, "id"));
        if (CollectionUtils.isNotEmpty(websiteList) && websiteList.size() > 1) {
            throw new ServiceException("只能删除同一站点的管理员！");
        }
        if (HibernatePluginsUtil.validateReferencingCurrentSession(CorePePriRole.class, ids)) {
            throw new ServiceException(SiteManagerConst.FOREIGN_KEY_ERROR);
        }
        int count = this.generalDao.deleteByIds(CorePePriRole.class, idList);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT                                                ");
        sql.append(" 	site.code as code                                           ");
        sql.append(" FROM                                                           ");
        sql.append(" 	pe_pri_role role                                            ");
        sql.append(" INNER JOIN pe_web_site site ON site.id = role.fk_web_site_id   ");
        sql.append(" where                                                          ");
        sql.append(com.whaty.util.CommonUtils.madeSqlIn(idList, "role.id"));
        String siteCode = openGeneralDao.getOneBySQL(sql.toString());
        TycjParameterAssert.isAllNotNull(siteCode);
        MasterSlaveRoutingDataSource.setDbType(siteCode);
        if (HibernatePluginsUtil.validateReferencingOpenSession(CorePePriRole.class,
                com.whaty.util.CommonUtils.join(idList, ",", null))) {
            throw new ServiceException(SiteManagerConst.FOREIGN_KEY_ERROR);
        }
        openGeneralDao.executeBySQL("delete from pe_pri_role where " + com.whaty.util.CommonUtils
                .madeSqlIn(idList, "id"));
        return CommonUtils.createSuccessInfoMap(String.format("删除成功，共删除%s条数据", count));
    }

    /**
     * 设为站点管理员
     *
     * @param ids
     * @return
     */
    public int setSiteSuperAdmin(String ids) throws ServiceException {
        TycjParameterAssert.isAllNotBlank(ids);
        CorePePriRole corePePriRole = this.generalDao.getById(CorePePriRole.class, ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" update pe_pri_role role                                           ");
        sql.append(" INNER JOIN enum_const em ON em.namespace = 'FlagSiteSuperAdmin'   ");
        sql.append(" AND em. CODE = '1'                                                ");
        sql.append(" SET role.flag_site_super_admin = em.id                            ");
        sql.append(" where                                                             ");
        sql.append(com.whaty.util.CommonUtils.madeSqlIn(ids, "role.id"));
        int count = generalDao.executeBySQL(sql.toString());
        MasterSlaveRoutingDataSource.setDbType(corePePriRole.getPeWebSite().getCode());
        this.openGeneralDao.executeBySQL(sql.toString());
        return count;
    }

    /**
     * 设为普通管理员
     *
     * @param ids
     * @return
     */
    public int setOrdinaryAdmin(String ids) throws ServiceException {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException(CommonConstant.PARAM_ERROR);
        }
        CorePePriRole corePePriRole = this.generalDao.getById(CorePePriRole.class, ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" update pe_pri_role role                                           ");
        sql.append(" INNER JOIN enum_const em ON em.namespace = 'FlagSiteSuperAdmin'   ");
        sql.append(" AND em. CODE = '0'                                                ");
        sql.append(" SET role.flag_site_super_admin = em.id                            ");
        sql.append(" where                                                             ");
        sql.append(com.whaty.util.CommonUtils.madeSqlIn(ids, "role.id"));
        int count = generalDao.executeBySQL(sql.toString());
        MasterSlaveRoutingDataSource.setDbType(corePePriRole.getPeWebSite().getCode());
        openGeneralDao.executeBySQL(sql.toString());
        return count;
    }

    /**
     * 根据bean获取站点信息
     *
     * @param bean
     * @return
     */
    private PeWebSite getUserWebSite(CorePePriRole bean) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return this.openGeneralDao.getById(PeWebSite.class, bean.getPeWebSite().getId());
    }
}
