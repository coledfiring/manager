package com.whaty.products.service.siteManager.impl;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.cache.service.CacheService;
import com.whaty.core.commons.cache.util.CacheUtil;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.util.OrderItem;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.Site;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.bean.RequestConfig;
import com.whaty.core.framework.grid.bean.menu.AjaxGridMenu;
import com.whaty.core.framework.service.siteManager.constant.SiteManagerConst;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeManager;
import com.whaty.domain.bean.PePriRole;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.PlatformConfigUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.LearningSpaceException;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UCenterException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.scope.constants.ScopeEnum;
import com.whaty.framework.scope.util.ScopeHandleUtils;
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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.whaty.constant.CommonConstant.PROFILE_PICTURE_PATH;

/**
 * 站点用户管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("siteManagerManageService")
public class SiteManagerManageServiceImpl extends TycjGridServiceAdapter<PeManager> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Resource(name = "userDataOperateService")
    private UserDataOperateService userDataOperateService;

    @Resource(name = "core_cacheService")
    private CacheService cacheService;

    @Resource(name = CommonConstant.REDIS_CACHE_SERVICE_BEAN_NAME)
    private RedisCacheService redisCacheService;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    private final WordImageGenerator generator = new WordImageGenerator(120, 120);

    @Override
    public void initGrid(GridConfig gridConfig, Map<String, Object> mapParam) {
        List roleList = myGeneralDao.getBySQL("select 1 from pe_pri_role role " +
                "INNER JOIN enum_const em on em.id = role.FLAG_ROLE_TYPE AND em.code='9998' " +
                "where role.id = '" + userService.getCurrentUser().getRole().getId() + "'");
        //只有站点超管用户可以操作此列表
        if (CollectionUtils.isEmpty(roleList)) {
            throw new ServiceException(SiteManagerConst.NO_PRIORITY_ERROR);
        }
        if ("siteManagerManage".equals(gridConfig.getGridId())) {
            String defaultPwd = this.myGeneralDao.getOneBySQL(
                    " SELECT `value` FROM `system_variables` WHERE `NAME` = 'defaultpassword' AND `site_code` = ? ",
                    SiteUtil.getSiteCode());
            defaultPwd = defaultPwd == null ? "@" + SiteUtil.getSiteCode() : defaultPwd;
            AjaxGridMenu resetPwdMenu = new AjaxGridMenu();
            resetPwdMenu.setRequestConfig(new RequestConfig("/entity/siteManager/siteManagerManage/resetUserPwd"));
            resetPwdMenu.setText("重置密码为：用户名" + defaultPwd);
            resetPwdMenu.setDataIndex("id");
            resetPwdMenu.setSelectLimit(10);
            resetPwdMenu.setShowConfirm(true);
            resetPwdMenu.setMustSelectRow(true);
            resetPwdMenu.setShowType("top");
            gridConfig.addMenu(resetPwdMenu);
        }
    }

    /**
     * 验证手机号非空时的唯一性
     *
     * @param beanId
     * @param mobile
     * @return 验证通过返回true、反之返回false
     */
    private boolean checkMobile(String beanId, String mobile) {
        //不验证手机号未填写的数据
        if (StringUtils.isBlank(mobile)) {
            return true;
        }
        String querySql = "SELECT 1 FROM pe_manager where mobile = ? AND site_code = ? ";
        if (StringUtils.isNotBlank(beanId)) {
            querySql = querySql + " AND id <> '" + beanId + "'";
        }
        return CollectionUtils.isEmpty(myGeneralDao.getBySQL(querySql, mobile, SiteUtil.getSiteCode()));
    }

    @Override
    public void checkBeforeUpdate(PeManager bean) throws EntityException {
        List sameLoginIdList = myGeneralDao.getBySQL("select FK_SSO_USER_ID from pe_manager where login_id = ?" +
                " and id <> ? and site_code = ?", bean.getLoginId(), bean.getId(), SiteUtil.getSiteCode());
        if (CollectionUtils.isNotEmpty(sameLoginIdList)) {
            throw new EntityException("更新失败，该用户名已存在！");
        }
        //验证手机号唯一
        if (!checkMobile(bean.getId(), bean.getMobile())) {
            throw new EntityException("更新失败，手机号已被其它用户占用！");
        }
        PeManager peManager = myGeneralDao.getById(PeManager.class, bean.getId());
        SsoUser ssoUser = peManager.getSsoUser();
        PePriRole pePriRole = bean.getPePriRole();
        ssoUser.setTrueName(bean.getTrueName());
        ssoUser.setPePriRole(pePriRole);
        ssoUser.setLoginId(bean.getLoginId());
        ssoUser.setSiteCode(SiteUtil.getSiteCode());
        bean.setSsoUser(ssoUser);
       /* String unitId = this.openGeneralDao.getOneBySQL("select fk_unit_id from pe_manager where id = ?",
                bean.getId());
        if (!unitId.equals(bean.getPeUnit().getId())) {
            this.updateUnitAuthority(ssoUser.getId(), bean.getPeUnit().getId());
        } */
        bean.setName(bean.getTrueName() + "/" + bean.getLoginId());
    }

    @Override
    public void checkBeforeAdd(PeManager bean) throws EntityException {
        List sameLoginIdList = myGeneralDao.getBySQL(" select 1 from sso_user where login_id = ? and site_code = ?",
                bean.getLoginId(), MasterSlaveRoutingDataSource.getDbType());
        if (CollectionUtils.isNotEmpty(sameLoginIdList)) {
            throw new EntityException("添加失败，该用户名已存在！");
        }
        //验证手机号唯一
        if (!checkMobile(bean.getId(), bean.getMobile())) {
            throw new EntityException("添加失败，手机号已被其它用户占用！");
        }
        SsoUser ssoUser = new SsoUser();
        PePriRole pePriRole = bean.getPePriRole();
        ssoUser.setPePriRole(pePriRole);
        ssoUser.setTrueName(bean.getTrueName());
        ssoUser.setPassword(CommonUtils.md5(bean.getLoginId() + getDefaultPassword(SiteUtil.getSiteCode())));
        ssoUser.setLearnspaceSiteCode(LearnSpaceUtil.getLearnSpaceSiteCode());
        ssoUser.setLoginId(bean.getLoginId());
        EnumConst active = this.myGeneralDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_IS_VALID,
                        bean.getEnumConstByFlagActive().getCode());
        ssoUser.setEnumConstByFlagIsvalid(active);
        ssoUser.setSiteCode(MasterSlaveRoutingDataSource.getDbType());
        String profilePicture = String.format(PROFILE_PICTURE_PATH, SiteUtil.getSiteCode(), "manager",
                ssoUser.getLoginId());
        ssoUser.setProfilePicture(profilePicture);
        try {
            this.generator.generateProfilePicture(bean.getTrueName().substring(bean.getTrueName().length() - 2),
                    CommonUtils.mkDir(CommonUtils.getRealPath(profilePicture)));
        } catch (IOException e) {
            throw new UncheckException(e);
        }
        myGeneralDao.save(ssoUser);
        myGeneralDao.flush();
        bean.setSsoUser(ssoUser);
        // this.updateUnitAuthority(ssoUser.getId(), bean.getPeUnit().getId());
        bean.setName(bean.getTrueName() + "/" + bean.getLoginId());
    }

    @Override
    public Map update(PeManager bean, GridConfig gridConfig) {
        Map resultMap = super.update(bean, gridConfig);
        if ((boolean) resultMap.get("success")) {
            resultMap.computeIfPresent(
                    "info", (key, oldValue) -> oldValue );
        }
        return resultMap;
    }

    /**
     * 是否绑定小程序column dataindex
     */
    private static final String HAS_WXAPP_COLUMN_DATAINDEX = "hasWeChatApp";

    @Override
    public Page list(Page pageParam, GridConfig gridConfig, Map<String, Object> mapParam) {
        // 去除grid配置中的 hasWeChatApp列
        gridConfig.setListColumnConfig(gridConfig.getListColumnConfig().stream()
                .filter(e -> !HAS_WXAPP_COLUMN_DATAINDEX.equals(e.getDataIndex())).collect(Collectors.toList()));
        // 去除排序条件中的 hasWeChatApp列
        if (null != pageParam.getOrderItem()) {
            pageParam.setOrderItem(Stream.of(pageParam.getOrderItem()).filter(Objects::nonNull)
                    .filter(e -> !HAS_WXAPP_COLUMN_DATAINDEX.equals(e.getDataIndex())).toArray(OrderItem[]::new));
        }
        return super.list(pageParam, gridConfig, mapParam);
    }


    @Override
    protected Page afterList(Page page) {
        List<Map> items = page.getItems();
        //添加是否绑定小程序
        String currentCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        Set<String> weChatAppUserIds;
        try {
            weChatAppUserIds = new HashSet<String>(this.openGeneralDao.getBySQL(
                    "select fk_sso_user_id from wechat_app_user where " + CommonUtils.madeSqlIn(
                            items.stream().map(e -> (String) e.get("userId")).collect(Collectors.toList()),
                            "fk_sso_user_id")));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentCode);
        }
        items.forEach(e -> e.put(HAS_WXAPP_COLUMN_DATAINDEX, weChatAppUserIds.contains(e.get("userId")) ? "是" : "否"));
        return super.afterList(page);
    }

    /**
     * 更改单位，权限
     * @param userId
     * @param unitId
     */
    private void updateUnitAuthority(String userId, String unitId) {
        this.myGeneralDao.executeBySQL("delete from pr_pri_manager_unit where fk_sso_user_id = ?", userId);
        this.myGeneralDao.executeBySQL("insert into pr_pri_manager_unit (fk_sso_user_id, fk_item_id) values(?, ?)",
                userId, unitId);
        //更新缓存
        ScopeHandleUtils.removeScopeCache(userId, ScopeEnum.peUnit);
    }

    @Override
    public Map delete(GridConfig gridConfig, String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ParameterIllegalException();
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_manager m " +
                "inner join enum_const ac on ac.id = m.flag_active where ac.code = '1' AND "
                + CommonUtils.madeSqlIn(ids, "m.id"))) {
            throw new ServiceException("存在有效的数据无法删除");
        }
        List ssoUserIdList = myGeneralDao.getBySQL("select fk_sso_user_id from pe_manager where "
                + CommonUtils.madeSqlIn(ids, "id"));
        //清除缓存信息
        List<String> loginIdList = myGeneralDao.getBySQL("select login_id from pe_manager where "
                + CommonUtils.madeSqlIn(ids, "id"));
        if (CollectionUtils.isNotEmpty(loginIdList)) {
            loginIdList.forEach(loginId ->
                    this.cacheService.remove(CacheUtil
                            .getCacheKeyWithParams("%s_%s", SiteUtil.getSite().getDomain(), loginId))
            );
        }
        this.myGeneralDao.executeBySQL("delete from pr_pri_manager_unit where " +
                CommonUtils.madeSqlIn(ssoUserIdList, "fk_sso_user_id"));
        int count = this.myGeneralDao.deleteByIds(PeManager.class,
                Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN)));
        if (HibernatePluginsUtil.validateReferencingCurrentSession(SsoUser.class,
                CommonUtils.join(ssoUserIdList, CommonConstant.SPLIT_ID_SIGN, null))) {
            throw new ServiceException("数据已被其他信息引用");
        }
        myGeneralDao.executeBySQL("DELETE from sso_user where " + CommonUtils.madeSqlIn(ssoUserIdList, "id"));
        List<Map<String, Object>> userInfo = this.openGeneralDao
                .getMapBySQL("SELECT m.TRUE_NAME AS trueName, m.login_id AS loginId, ss.password as password" +
                        " FROM pe_manager m inner join sso_user ss on ss.id = m.fk_sso_user_id where " +
                        CommonUtils.madeSqlIn(ids, "m.id"));
        List<String> loginIds = userInfo.stream().map(e -> (String) e.get("loginId")).collect(Collectors.toList());
        UCenterUtils.removeUserFromUCenter(loginIds, SiteUtil.getSite().getSsoAppId());
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                if (!learningSpaceWebService.removeManager(ids, LearnSpaceUtil.getLearnSpaceSiteCode())) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                logger.error("delete manager failure", e);
                try {
                    UCenterUtils.synchronousUserToUCenter(userInfo, SiteUtil.getSite().getSsoAppId());
                } catch (Exception e1) {
                    logger.error(e1);
                }
                throw new ServiceException("同步课程空间失败");
            }
        }
        this.afterDelete(ids);
        return com.whaty.core.commons.util.CommonUtils.
                createSuccessInfoMap(String.format("删除成功，共删除%s条数据", count));
    }

    /**
     * 删除用户后清除微信小程序，微信公众号绑定
     *
     * @param ids
     */
    private void afterDelete(String ids) {
        this.doUnbindWeChatAppUser(ids);
        this.doUnbindWeChatUser(ids);
    }

    @Override
    public void afterAdd(PeManager bean) throws EntityException {
        if (PlatformConfigUtil.getPlatformConfig().getOpenTencentIm()) {
            try {
                new TencentImManageSDK().insertUser(bean.getSsoUser().getId());
            } catch (Exception e) {
                throw new UncheckException(e);
            }
        }
        String loginId = bean.getSsoUser().getLoginId();
        UCenterUtils.synchronousUserToUCenter(bean.getLoginId(), bean.getSsoUser().getPassword(),
                SiteUtil.getSite().getSsoAppId());
        //同步课程空间
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                if (!learningSpaceWebService.saveManager(bean.getId(), loginId,
                        bean.getTrueName(), bean.getSsoUser().getId(), null, LearnSpaceUtil.getLearnSpaceSiteCode(),
                        bean.getSsoUser().getPassword())) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                logger.error("add manager failure", e);
                try {
                    UCenterUtils.removeUserFromUCenter(bean.getLoginId(), SiteUtil.getSite().getSsoAppId());
                } catch (Exception e1) {
                    logger.error(e1);
                }
                throw new EntityException("同步课程空间失败");
            }
        }

    }

    @Override
    public void afterUpdate(PeManager bean) throws EntityException {
        SsoUser oldUser = this.openGeneralDao.getById(SsoUser.class, bean.getSsoUser().getId());
        try {
            UCenterUtils.updateUCenterSingleUser(oldUser, bean.getSsoUser(), SiteUtil.getSite().getSsoAppId());
        } catch (UCenterException e) {
            logger.error("update teacher failure", e);
            throw new EntityException(e.getInfo());
        }
        //同步课程空间
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                if (!learningSpaceWebService.updateManager(bean.getId(), bean.getSsoUser().getLoginId(),
                        bean.getTrueName(), null, bean.getEnumConstByFlagActive().getCode(),
                        bean.getSsoUser().getPassword(), LearnSpaceUtil.getLearnSpaceSiteCode())) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                logger.error("update site manager failure", e);
                UCenterUtils.updateUCenterSingleUser(bean.getSsoUser(), oldUser, SiteUtil.getSite().getSsoAppId());
                throw new EntityException("课程空间同步失败");
            }
        }
        this.cacheService.remove(CacheUtil
                .getCacheKeyWithParams("%s_%s", SiteUtil.getSite().getDomain(), bean.getLoginId()));
    }

    /**
     * 重置管理员密码
     *
     * @param ids
     * @param site
     * @return
     */
    public int resetUserPwd(String ids, Site site) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException(CommonConstant.PARAM_ERROR);
        }
        List<Map<String, Object>> oldUserInfo = this.myGeneralDao
                .getMapBySQL("select ss.id as id, ss.login_id as loginId, ss.password as password from sso_user ss " +
                        "inner join pe_manager m on m.fk_sso_user_id = ss.id where " +
                        CommonUtils.madeSqlIn(ids, "m.id"));
        List<String> userIds = oldUserInfo.stream().map(e -> (String) e.get("id")).collect(Collectors.toList());
        int count = this.myGeneralDao.executeBySQL("update sso_user set password = md5(concat(login_id, '" +
                this.getDefaultPassword(site.getCode()) + "')) where " + CommonUtils.madeSqlIn(userIds, "id"));
        List<Map<String, Object>> userInfo = this.myGeneralDao
                .getMapBySQL("select login_id as loginId, password as password from sso_user where " +
                        CommonUtils.madeSqlIn(userIds, "id"));
        List<String> loginIdList = userInfo.stream().map(e -> (String) e.get("loginId")).collect(Collectors.toList());
        userDataOperateService.removeUserCache(loginIdList, CommonUtils.getServerName());
        UCenterUtils.updatePasswordForUCenterUser(userInfo, oldUserInfo,
                SiteUtil.getSite(site.getCode()).getSsoAppId());
        return count;
    }

    /**
     * 获取业务库默认密码
     *
     * @param siteCode
     * @return
     */
    private String getDefaultPassword(String siteCode) {
        String defaultPassword = this.myGeneralDao.getOneBySQL(
                " SELECT `value` FROM `system_variables` WHERE `NAME` = 'defaultpassword' AND `site_code` = ? ",
                siteCode);
        return defaultPassword == null ? "@" + siteCode : defaultPassword;
    }

    /**
     * 同步数据
     * @param ids
     */
    @LogAndNotice("同步数据")
    public void doSyncData(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                      ");
        sql.append("    	su.id ssoUserId,                                                        ");
        sql.append("    	su.login_id loginId,                                                    ");
        sql.append("    	pm.true_name trueName,                                                  ");
        sql.append("    	su.FK_ROLE_ID roleId,                                                   ");
        sql.append("    	su.`PASSWORD` password,                                                 ");
        sql.append("    	su.site_code siteCode,                                                  ");
        sql.append("    	pm.id pmId                                                              ");
        sql.append("    FROM                                                                        ");
        sql.append("    	pe_manager pm                                                           ");
        sql.append("    INNER JOIN sso_user su ON su.id = pm.fk_sso_user_id                         ");
        sql.append("    WHERE                        " + CommonUtils.madeSqlIn(ids, "pm.id"));
        List<Map<String, Object>> userInfo = this.myGeneralDao.getMapBySQL(sql.toString());

        List<String> loginIdList = new ArrayList<>();
        List<String[]> learnSpaceList = new ArrayList<>();
        userInfo.forEach(e -> {
            loginIdList.add((String)e.get("loginId"));
            learnSpaceList.add(new String[] {
                    (String)e.get("pmId"),
                    (String)e.get("loginId"),
                    (String)e.get("trueName"),
                    (String)e.get("ssoUserId"),
                    null,
                    (String)e.get("siteCode"),
                    (String)e.get("password")
            });
        });
        try {
            UCenterUtils.synchronousUserToUCenter(userInfo, SiteUtil.getSite().getSsoAppId());
        } catch (UCenterException e) {
            logger.error(e.getLogInfo(), e);
            throw new ServiceException("同步用户中心失败");
        }
        if (LearnSpaceUtil.learnSpaceIsOpen(SiteUtil.getSite().getCode())) {
            try {
                if (!learningSpaceWebService.saveManagerBatch(learnSpaceList)) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                UCenterUtils.removeUserFromUCenter(loginIdList, SiteUtil.getSite().getSsoAppId());
                logger.error("add manager failure", e);
                throw new LearningSpaceException(e);
            }
        }
    }

    @LogAndNotice("管理员设为有效")
    public int doSetActive(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" update                                                    ");
        sql.append(" pe_manager man                                            ");
        sql.append(" inner join enum_const ac on ac.namespace = 'flagActive'   ");
        sql.append(" ac.code='1'                                               ");
        sql.append(" set man.flag_active=ac.id                                 ");
        sql.append(" where                                                     ");
        sql.append(CommonUtils.madeSqlIn(ids, "man.id"));
        return this.myGeneralDao.executeBySQL(sql.toString());
    }

    @LogAndNotice("管理员设为无效")
    public int doSetNotActive(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" update                                                    ");
        sql.append(" pe_manager man                                            ");
        sql.append(" inner join enum_const ac on ac.namespace='flagActive'     ");
        sql.append(" ac.code='0'                                               ");
        sql.append(" set man.flag_active=ac.id                                 ");
        sql.append(" where                                                     ");
        sql.append(CommonUtils.madeSqlIn(ids, "man.id"));
        return this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 生成头像
     * @param ids
     */
    public void doGenerateProfilePicture(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        List<Map<String, Object>> userInfo = this.myGeneralDao
                .getMapBySQL("select ss.id as userId, m.true_name as name, ss.login_id as loginId " +
                        "from pe_manager m " +
                        "inner join sso_user ss on ss.id = m.fk_sso_user_id " +
                        "where " + CommonUtils.madeSqlIn(ids, "m.id"));
        if (CollectionUtils.isEmpty(userInfo)) {
            return;
        }
        userInfo.forEach(user -> {
            String profilePicture = String.format(PROFILE_PICTURE_PATH, SiteUtil.getSiteCode(), "manager",
                    user.get("loginId"));
            try {
                String name = (String) user.get("name");
                name = name.length() > 2 ? name.substring(name.length() - 2) : name;
                this.generator.generateProfilePicture(name, CommonUtils.mkDir(CommonUtils.getRealPath(profilePicture)));
            } catch (IOException e) {
                throw new UncheckException(e);
            }
            this.myGeneralDao.executeBySQL("update sso_user set profile_picture = ? where id = ?",
                    profilePicture, user.get("userId"));
        });
    }

    /**
     * 同步管理员到im
     * @param ids
     * @return
     * @throws Exception
     */
    public int syncUserToIm(String ids) throws Exception {
        if (!PlatformConfigUtil.getPlatformConfig().getOpenTencentIm()) {
            throw new ServiceException("未开启腾讯im配置");
        }
        List<String> userIds = this.myGeneralDao.getBySQL("select fk_sso_user_id from pe_manager where " +
                CommonUtils.madeSqlIn(ids, "id"));
        new TencentImManageSDK().insertUser(userIds);
        return userIds.size();
    }

    /**
     * 解除微信绑定
     *
     * @param ids
     * @return
     */
    public int doUnbindWeChatUser(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        return this.myGeneralDao.executeBySQL(UserBindType.WECHAT.getUnBindSql(CommonUtils.madeSqlIn(ids, "man.id")));
    }

    /**
     * 解除小程序绑定
     *
     * @param ids
     * @return
     */
    public int doUnbindWeChatAppUser(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        String currentCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        try {
            return this.openGeneralDao.executeBySQL(
                    UserBindType.WECHATAPP.getUnBindSql(CommonUtils.madeSqlIn(ids, "fk_sso_user_id")));
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentCode);
        }
    }

    /**
     * 用户绑定类型信息
     */
    private enum UserBindType {
        /**
         * 微信绑定绑定类型
         */
        WECHAT("weChat", (e) -> String.format(
                " delete wu from wechat_user wu inner join pe_manager man on man.fk_sso_user_id=wu.fk_sso_user_id " +
                        " where %s ", e)),
        /**
         * 微信小程序绑定类型
         */
        WECHATAPP("weChatApp", (e) -> String.format(" delete from wechat_app_user where %s ", e));

        /**
         * 名称
         */
        private String name;

        /**
         * 解除绑定sql
         */
        private Function<String, String> unBindSql;

        UserBindType(String name, Function<String, String> unBindSql) {
            this.name = name;
            this.unBindSql = unBindSql;
        }

        public String getName() {
            return name;
        }

        /**
         * 生成解除绑定sql
         *
         * @param unBindWhereCondition
         * @return
         */
        public String getUnBindSql(String unBindWhereCondition) {
            return unBindSql.apply(unBindWhereCondition);
        }
    }
}
