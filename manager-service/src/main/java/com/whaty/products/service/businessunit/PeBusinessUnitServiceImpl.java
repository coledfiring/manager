package com.whaty.products.service.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.Site;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeBusinessUnit;
import com.whaty.domain.bean.PePriRole;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.LearningSpaceException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UCenterException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.ucenter.utils.UCenterUtils;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.products.service.businessunit.constants.BusinessUnitStudentEnrollConstant;
import com.whaty.products.service.user.UserDataOperateService;
import com.whaty.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 单位基本信息service
 *
 * @author shanshuai
 */
@Lazy
@Service("peBusinessUnitService")
public class PeBusinessUnitServiceImpl extends TycjGridServiceAdapter<PeBusinessUnit> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    @Resource(name = "userDataOperateService")
    private UserDataOperateService userDataOperateService;

    /**
     * 取消报名
     * @param ids
     */
    @LogAndNotice("重置密码")
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
        String defaultPassword = (String) this.myGeneralDao
                .getOneBySQL(" select code from enum_const where namespace = 'defaultpassword'");
        return defaultPassword == null ? "@" + siteCode : defaultPassword;
    }

    @Override
    public void checkBeforeAdd(PeBusinessUnit bean) throws EntityException {
        String loginId = bean.getSsoUser().getLoginId();
        if (!ValidateUtils.checkCustomValidate(BusinessUnitStudentEnrollConstant.LOGIN_ID_REGEX, loginId)) {
            throw new EntityException("单位用户名命名错误，只能是字母或数字");
        }
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL("SELECT 1 FROM sso_user WHERE LOGIN_ID = ?", loginId))) {
            throw new EntityException("单位用户名已经存在，请修改单位用户名后再操作");
        }
        if (!ValidateUtils.checkCardNoReg(bean.getCardNo())) {
            throw new EntityException("身份证号格式错误");
        }
        if (!ValidateUtils.checkMobilePhoneReg(bean.getSsoUser().getMobilePhone())) {
            throw new EntityException("联系人手机格式错误");
        }
        SsoUser ssoUser = new SsoUser();
        PePriRole pePriRole = (PePriRole) myGeneralDao.
                getOneByHQL("FROM PePriRole WHERE name = '事业单位管理员' AND siteCode = ?", SiteUtil.getSiteCode());
        if (pePriRole == null) {
            throw new EntityException("请先添加名称为事业单位管理员的角色，再进行本操作");
        }
        ssoUser.setPePriRole(pePriRole);
        ssoUser.setLoginId(loginId);
        ssoUser.setTrueName(bean.getSsoUser().getTrueName());
        ssoUser.setPassword(CommonUtils.md5(bean.getSsoUser().getMobilePhone()));
        ssoUser.setLearnspaceSiteCode(LearnSpaceUtil.getLearnSpaceSiteCode());
        ssoUser.setMobilePhone(bean.getSsoUser().getMobilePhone());
        ssoUser.setSiteCode(SiteUtil.getSiteCode());
        EnumConst active = this.myGeneralDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_IS_VALID, "1");
        ssoUser.setEnumConstByFlagIsvalid(active);
        myGeneralDao.save(ssoUser);
        myGeneralDao.flush();
        bean.setSsoUser(ssoUser);
    }

    @Override
    public void afterAdd(PeBusinessUnit bean) throws EntityException {
        String managerId = UUID.randomUUID().toString().replace("-", "");
        String trueName = bean.getSsoUser().getTrueName();
        String loginId = bean.getSsoUser().getLoginId();
        String ssoUserId = bean.getSsoUser().getId();
        String password = bean.getSsoUser().getPassword();
        EnumConst active = this.myGeneralDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, "1");
        StringBuilder sql = new StringBuilder();
        sql.append("    INSERT INTO pe_manager (                                                  ");
        sql.append("    	id,                                                                   ");
        sql.append("    	login_id,                                                             ");
        sql.append("    	NAME,                                                                 ");
        sql.append("    	true_name,                                                            ");
        sql.append("    	fk_sso_user_id,                                                       ");
        sql.append("    	flag_active,                                                          ");
        sql.append("    	card_no,                                                              ");
        sql.append("    	mobile,                                                               ");
        sql.append("    	fk_role_id,                                                           ");
        sql.append("    	site_code                                                             ");
        sql.append("    )                                                                         ");
        sql.append("    VALUES                                                                    ");
        sql.append("    	(                                                                     ");
        sql.append("    		'" + managerId + "',                                              ");
        sql.append("    		'" + loginId + "',                                                ");
        sql.append("    		'" + trueName + "/" + loginId + "',                               ");
        sql.append("    		'" + trueName + "',                                               ");
        sql.append("    		'" + ssoUserId + "',                                              ");
        sql.append("    		'" + active.getId() + "',                                         ");
        sql.append("    		'" + bean.getCardNo() + "',                                       ");
        sql.append("    		'" + bean.getSsoUser().getMobilePhone() + "',                     ");
        sql.append("    		'" + bean.getSsoUser().getPePriRole().getId() + "',               ");
        sql.append("    		'" + SiteUtil.getSiteCode() + "'                                  ");
        sql.append("    	)                                                                     ");
        myGeneralDao.executeBySQL(sql.toString());
        try {
            UCenterUtils.synchronousUserToUCenter(loginId, password, SiteUtil.getSite().getSsoAppId());
        } catch (UCenterException e) {
            throw new EntityException(e.getInfo());
        }
        //同步课程空间
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                if (!learningSpaceWebService.saveManager(managerId, loginId, trueName, ssoUserId, null,
                        LearnSpaceUtil.getLearnSpaceSiteCode(), password)) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                UCenterUtils.removeUserFromUCenter(loginId, SiteUtil.getSite().getSsoAppId());
                throw new EntityException("同步课程空间失败");
            }
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                  ");
        sql.append("    	1                                                                   ");
        sql.append("    FROM                                                                    ");
        sql.append("    	pe_business_unit unit                                               ");
        sql.append("    INNER JOIN pe_student stu ON stu.fk_business_unit_id = unit.id          ");
        sql.append("    WHERE " + CommonUtils.madeSqlIn(idList, "unit.id"));
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new EntityException("存在已经报名的单位,不能删除");
        }
        myGeneralDao.executeBySQL("DELETE FROM pe_business_unit WHERE " + CommonUtils.madeSqlIn(idList, "id"));
    }

}
