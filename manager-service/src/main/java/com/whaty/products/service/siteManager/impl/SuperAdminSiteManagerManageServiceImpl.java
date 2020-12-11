package com.whaty.products.service.siteManager.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.CorePePriRole;
import com.whaty.core.framework.bean.CoreSsoUser;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.oauth.util.OauthRole;
import com.whaty.core.framework.service.RoleService;
import com.whaty.core.framework.service.siteManager.constant.SiteManagerConst;
import com.whaty.core.framework.util.UUIDUtil;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.LearningSpaceException;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UCenterException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.ucenter.utils.UCenterUtils;
import com.whaty.generator.WordImageGenerator;
import com.whaty.framework.im.TencentImManageSDK;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.products.service.user.UserDataOperateService;
import com.whaty.util.CommonUtils;
import com.whaty.utils.HibernatePluginsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.whaty.constant.CommonConstant.PROFILE_PICTURE_PATH;

/**
 * 站点超管用户管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("superAdminSiteManagerManageService")
public class SuperAdminSiteManagerManageServiceImpl extends TycjGridServiceAdapter<CoreSsoUser> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    @Resource(name = "core_roleService")
    private RoleService roleService;

    @Resource(name = "userDataOperateService")
    private UserDataOperateService userDataOperateService;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    private final WordImageGenerator generator = new WordImageGenerator(120, 120);

    @Override
    public Map add(CoreSsoUser bean, Map<String, Object> params, GridConfig gridConfig) {
        String managerId = UUIDUtil.getUUID().replace("-", "");
        PeWebSite site = SiteUtil.getSite(MasterSlaveRoutingDataSource.getDbType());
        List roleList = this.roleService.getPePriRoleByRoleTypeCode(site.getId(), OauthRole.SITE_ADMIN.getId());
        CorePePriRole role = (CorePePriRole) roleList.get(0);
        bean.setCorePePriRole(role);
        bean.setSiteCode(site.getCode());

        String defaultPassword = bean.getLoginId() + this.getDefaultPassword(site.getCode());
        bean.setPassword(CommonUtils.md5(defaultPassword));
        if (this.generalDao.checkNotEmpty(" select 1 from sso_user where login_id = '"
                + bean.getLoginId() + "' and site_code = ?", site.getCode())) {
            throw new ServiceException("添加失败，该用户名已存在！");
        }
        this.mergeBeanByGridConfig(bean, bean, gridConfig, false);
        bean.setPassword(CommonUtils.md5(bean.getLoginId() + this.getDefaultPassword(bean.getSiteCode())));
        bean.setId(CommonUtils.generateUUIDNoSign());
        String learnSpaceSiteCode = LearnSpaceUtil.getLearnSpaceSiteCode(bean.getSiteCode());
        String profilePicture = String.format(PROFILE_PICTURE_PATH, MasterSlaveRoutingDataSource.getDbType(),
                "manager", bean.getLoginId());
        StringBuilder sql = new StringBuilder();
        sql.append("  insert into sso_user (            ");
        sql.append("    id,                             ");
        sql.append("    FK_ROLE_ID,                     ");
        sql.append("    FLAG_ISVALID,                   ");
        sql.append("    LOGIN_ID,                       ");
        sql.append("    PASSWORD,                       ");
        sql.append("    site_code,                      ");
        sql.append("    true_name,                      ");
        sql.append("    profile_picture,                ");
        sql.append("    LEARNSPACE_SITE_CODE            ");
        sql.append("  )                                 ");
        sql.append("  VALUES (                          ");
        sql.append("'" + bean.getId() + "',");
        sql.append("'" + bean.getCorePePriRole().getId() + "',");
        sql.append("'" + bean.getEnumConstByFlagIsvalid().getId() + "',");
        sql.append("'" + bean.getLoginId() + "',");
        sql.append("'" + bean.getPassword() + "',");
        sql.append("'" + bean.getSiteCode() + "',");
        sql.append("'" + bean.getTrueName() + "',");
        sql.append("'" + profilePicture + "',");
        sql.append("'" + learnSpaceSiteCode + "'");
        sql.append("  )                                     ");
        this.generalDao.executeBySQL(sql.toString());
        try {
            this.generator.generateProfilePicture(bean.getTrueName().substring(bean.getTrueName().length() - 2),
                    CommonUtils.mkDir(CommonUtils.getRealPath(profilePicture)));
        } catch (IOException e) {
            throw new UncheckException(e);
        }
        sql.delete(0, sql.length());
        EnumConst valid = this.generalDao.getById(EnumConst.class, bean.getEnumConstByFlagIsvalid().getId());
        EnumConst active = this.generalDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, valid.getCode());
        sql.append(" insert into pe_manager (                       ");
        sql.append("   id,                                          ");
        sql.append("   FK_ROLE_ID,                                  ");
        sql.append("   login_id,                                    ");
        sql.append("   fk_sso_user_id,                              ");
        sql.append("   true_name,                                   ");
        sql.append("   name,                                        ");
        sql.append("   flag_active,                                 ");
        sql.append("   site_code                                    ");
        sql.append(" )                                              ");
        sql.append("  VALUES                                        ");
        sql.append("  (                                             ");
        sql.append("'" + managerId + "',");
        sql.append("'" + bean.getCorePePriRole().getId() + "',");
        sql.append("'" + bean.getLoginId() + "',");
        sql.append("'" + bean.getId() + "',");
        sql.append("'" + bean.getTrueName() + "',");
        sql.append("'" + bean.getTrueName() + "/" + bean.getLoginId() + "',");
        sql.append("'" + active.getId() + "',");
        sql.append("'" + MasterSlaveRoutingDataSource.getDbType() + "'");
        sql.append("  )                                             ");
        this.generalDao.executeBySQL(sql.toString());
        try {
            new TencentImManageSDK().insertUser(bean.getId());
        } catch (Exception e) {
            throw new UncheckException(e);
        }
        UCenterUtils.synchronousUserToUCenter(bean.getLoginId(), bean.getPassword(),
                SiteUtil.getSite(bean.getSiteCode()).getSsoAppId());
        if (LearnSpaceUtil.learnSpaceIsOpen(bean.getSiteCode())) {
            try {
                if (!learningSpaceWebService.saveManager(managerId, bean.getLoginId(),
                        bean.getTrueName(), bean.getId(), null, learnSpaceSiteCode, bean.getPassword())) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                logger.error("add manager failure", e);
                try {
                    UCenterUtils.removeUserFromUCenter(bean.getLoginId(),
                            SiteUtil.getSite(bean.getSiteCode()).getSsoAppId());
                } catch (Exception e1) {
                    logger.error(e1);
                }
                throw new LearningSpaceException(e);
            }
        }

        return com.whaty.core.commons.util.CommonUtils.createSuccessInfoMap("添加成功");
    }

    @Override
    public Map update(CoreSsoUser bean, GridConfig gridConfig) {
        if (this.generalDao.checkNotEmpty("select 1 from sso_user where login_id = ? and id <> ? and site_code = ?",
                bean.getLoginId(), bean.getId(), MasterSlaveRoutingDataSource.getDbType())) {
            throw new ServiceException("更新失败，该用户名已存在！");
        }
        CoreSsoUser instance = this.generalDao.getById(CoreSsoUser.class, bean.getId());
        this.generalDao.evict(instance);
        this.mergeBeanByGridConfig(instance, bean, gridConfig, true);
        StringBuilder sql = new StringBuilder();
        EnumConst valid = this.generalDao.getById(EnumConst.class, bean.getEnumConstByFlagIsvalid().getId());
        EnumConst active = this.generalDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, valid.getCode());
        sql.append(" update sso_user sso                                    ");
        sql.append(" INNER JOIN pe_manager ma ON sso.id = ma.FK_SSO_USER_ID ");
        sql.append(" SET sso.LOGIN_ID = '" + bean.getLoginId() + "',        ");
        sql.append("   ma.LOGIN_ID = '" + bean.getLoginId() + "',           ");
        sql.append("   ma.TRUE_NAME = '" + bean.getTrueName() + "',         ");
        sql.append("   ma.name = '" + bean.getTrueName() + "/" + bean.getLoginId() + "',         ");
        sql.append("   sso.flag_isvalid = '" + bean.getEnumConstByFlagIsvalid().getId() + "',        ");
        sql.append("   ma.flag_active = '" + active.getId() + "',         ");
        sql.append("   sso.TRUE_NAME = '" + bean.getTrueName() + "'         ");
        sql.append(" where                                                  ");
        sql.append("   sso.id = '" + bean.getId() + "'                      ");
        this.generalDao.executeBySQL(sql.toString());
        CoreSsoUser oldUser = this.openGeneralDao.getById(CoreSsoUser.class, bean.getId());
        try {
            UCenterUtils.updateUCenterSingleUser(oldUser, bean, SiteUtil.getSite(instance.getSiteCode()).getSsoAppId());
        } catch (UCenterException e) {
            logger.error("update teacher failure", e);
            throw new ServiceException("同步用户中心失败");
        }
        if (LearnSpaceUtil.learnSpaceIsOpen(instance.getSiteCode())) {
            try {
                String manId = (String) this.generalDao.getBySQL("select id from pe_manager where fk_sso_user_id=?",
                        instance.getId()).get(0);
                if (!learningSpaceWebService.updateManager(manId,
                        instance.getLoginId(), instance.getTrueName(), null,
                        valid.getCode(), instance.getPassword(),
                        LearnSpaceUtil.getLearnSpaceSiteCode(instance.getSiteCode()))) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                logger.error("update manager failure", e);
                UCenterUtils.updateUCenterSingleUser(bean, oldUser,
                        SiteUtil.getSite(instance.getSiteCode()).getSsoAppId());
                throw new ServiceException("同步课程空间失败");
            }
        }
        return com.whaty.core.commons.util.CommonUtils.createSuccessInfoMap("更新成功");
    }

    @Override
    public Map delete(GridConfig gridConfig, String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ParameterIllegalException();
        }
        if (this.generalDao.checkNotEmpty("select 1 from sso_user ss " +
                "inner join enum_const ac on ac.id = ss.flag_isvalid where ac.code = '1' AND "
                + CommonUtils.madeSqlIn(ids, "ss.id"))) {
            throw new ServiceException("存在有效的数据无法删除");
        }
        try {
            this.generalDao
                    .executeBySQL("DELETE m FROM sso_user s INNER JOIN pe_manager m ON m.FK_SSO_USER_ID = s.ID where "
                            + CommonUtils.madeSqlIn(ids, "s.id"));
        } catch (Exception e) {
            logger.error("delete failure", e);
            throw new ServiceException(SiteManagerConst.FOREIGN_KEY_ERROR);
        }
        if (HibernatePluginsUtil.validateReferencingCurrentSession(SsoUser.class, ids)) {
            throw new ServiceException(SiteManagerConst.FOREIGN_KEY_ERROR);
        }
        int count = generalDao.executeBySQL("DELETE from sso_user where " + CommonUtils.madeSqlIn(ids, "id"));
        List<Map<String, Object>> userInfo = this.openGeneralDao
                .getMapBySQL("SELECT m.TRUE_NAME AS trueName, m.login_id AS loginId, ss.password as password" +
                        " FROM pe_manager m inner join sso_user ss on ss.id = m.fk_sso_user_id where " +
                        CommonUtils.madeSqlIn(ids, "ss.id"));
        List<String> loginIds = userInfo.stream().map(e -> (String) e.get("loginId")).collect(Collectors.toList());
        UCenterUtils.removeUserFromUCenter(loginIds, SiteUtil
                .getSite(MasterSlaveRoutingDataSource.getDbType()).getSsoAppId());
        //同步课程空间
        if (LearnSpaceUtil.learnSpaceIsOpen(MasterSlaveRoutingDataSource.getDbType())) {
            try {
                List managerIds = generalDao.getBySQL(" select id from pe_manager where "
                        + CommonUtils.madeSqlIn(ids, "FK_SSO_USER_ID"));
                if (!learningSpaceWebService.removeManager(CommonUtils
                                .join(managerIds, CommonConstant.SPLIT_ID_SIGN, null),
                        LearnSpaceUtil.getLearnSpaceSiteCode(MasterSlaveRoutingDataSource.getDbType()))) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                logger.error("delete manager failure", e);
                try {
                    UCenterUtils.synchronousUserToUCenter(userInfo, SiteUtil
                            .getSite(MasterSlaveRoutingDataSource.getDbType()).getSsoAppId());
                } catch (Exception e1) {
                    logger.error(e1);
                }
                throw new ServiceException("同步课程空间失败");
            }
        }
        return com.whaty.core.commons.util.CommonUtils
                .createSuccessInfoMap(String.format("删除成功，共删除%s条数据", count));
    }

    /**
     * 重置管理员密码
     *
     * @param ids
     * @return
     */
    public int resetUserPwd(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException(CommonConstant.PARAM_ERROR);
        }
        List<Map<String, Object>> oldUserInfo = this.generalDao
                .getMapBySQL("select login_id as loginId, password as password from sso_user where " +
                        CommonUtils.madeSqlIn(ids, "id"));
        int count = this.generalDao.executeBySQL("update sso_user set password = md5(concat(login_id, '" +
                this.getDefaultPassword(MasterSlaveRoutingDataSource.getDbType()) + "')) where " +
                CommonUtils.madeSqlIn(ids, "id"));
        List<Map<String, Object>> userInfo = this.generalDao
                .getMapBySQL("select login_id as loginId, password as password from sso_user where " +
                        CommonUtils.madeSqlIn(ids, "id"));
        List<String> loginIdList = userInfo.stream().map(e -> (String) e.get("loginId")).collect(Collectors.toList());
        userDataOperateService.removeUserCache(loginIdList,
                SiteUtil.getSite(MasterSlaveRoutingDataSource.getDbType()).getDomain());
        UCenterUtils.updatePasswordForUCenterUser(userInfo, oldUserInfo,
                SiteUtil.getSite(MasterSlaveRoutingDataSource.getDbType()).getSsoAppId());
        return count;
    }

    /**
     * 获取业务库默认密码
     *
     * @param siteCode
     * @return
     */
    private String getDefaultPassword(String siteCode) {
        String defaultPassword = this.generalDao.getOneBySQL(
                " SELECT `value` FROM `system_variables` WHERE `NAME` = 'defaultpassword' AND `site_code` = ? ",
                siteCode);
        return defaultPassword == null ? "@" + siteCode : defaultPassword;
    }

    /**
     * 生成头像
     *
     * @param ids
     */
    public void doGenerateProfilePicture(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        List<Map<String, Object>> userInfo = this.generalDao
                .getMapBySQL("select ss.id as userId, m.true_name as name, ss.login_id as loginId " +
                        "from pe_manager m inner join sso_user ss on ss.id = m.fk_sso_user_id " +
                        "where " + CommonUtils.madeSqlIn(ids, "ss.id"));
        if (CollectionUtils.isEmpty(userInfo)) {
            return;
        }
        userInfo.forEach(user -> {
            String profilePicture = String.format(PROFILE_PICTURE_PATH, MasterSlaveRoutingDataSource.getDbType(),
                    "manager", user.get("loginId"));
            try {
                String name = (String) user.get("name");
                name = name.length() > 2 ? name.substring(name.length() - 2) : name;
                this.generator.generateProfilePicture(name, CommonUtils.mkDir(CommonUtils.getRealPath(profilePicture)));
            } catch (IOException e) {
                throw new UncheckException(e);
            }
            this.generalDao.executeBySQL("update sso_user set profile_picture = ? where id = ?",
                    profilePicture, user.get("userId"));
        });
    }

    /**
     * 同步管理员到im
     *
     * @param ids
     * @return
     * @throws Exception
     */
    public int syncUserToIm(String ids) throws Exception {
        new TencentImManageSDK().insertUser(Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN)));
        return ids.split(CommonConstant.SPLIT_ID_SIGN).length;
    }
}
