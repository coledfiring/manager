package com.whaty.products.service.information.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.bean.Site;
import com.whaty.core.framework.service.SiteService;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeManager;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.ucenter.utils.UCenterUtils;
import com.whaty.products.service.information.ManagerPersonInfoService;
import com.whaty.products.service.information.constant.InformationConstant;
import com.whaty.products.service.user.UserDataOperateService;
import com.whaty.ucenter.oauth2.exception.ApiInvokeFailedException;
import com.whaty.ucenter.oauth2.sdk.service.UCenterService;
import com.whaty.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员个人信息服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("managerPersonInfoService")
public class ManagerPersonInfoServiceImpl implements ManagerPersonInfoService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Resource(name = "userDataOperateService")
    private UserDataOperateService userDataOperateService;

    @Resource(name = CommonConstant.U_CENTER_SERVICE_BEAN_NAME)
    private UCenterService uCenterService;

    @Resource(name = BeanNames.SITE_SERVICE)
    private SiteService siteService;

    private final static Logger logger = LoggerFactory.getLogger(ManagerPersonInfoServiceImpl.class);

    @Override
    public Map<String, Object> getPersonInfo() {
        PeManager manager = this.getCurrentPeManager();
        // 拼接个人信息
        Map<String, Object> personInfo = new HashMap<>(16);
        personInfo.put(InformationConstant.PARAM_TRUE_NAME, manager.getTrueName());
        personInfo.put(InformationConstant.PARAM_LOGIN_NUM, manager.getSsoUser().getLoginNum());
        personInfo.put(InformationConstant.PARAM_LAST_LOGIN_TIME, manager.getSsoUser().getLastLoginDate());
        personInfo.put(InformationConstant.PARAM_CARD_NO, manager.getCardNo());
        personInfo.put(InformationConstant.PARAM_EMAIL, manager.getEmail());
        personInfo.put(InformationConstant.PARAM_PHONE, manager.getTelephone());
        personInfo.put(InformationConstant.PARAM_MOBILE, manager.getMobile());
        personInfo.put(InformationConstant.PARAM_ADDRESS, manager.getAddress());
        if (manager.getEnumConstByFlagGender() != null) {
            personInfo.put(InformationConstant.PARAM_SEX, manager.getEnumConstByFlagGender().getName());
        }
        return personInfo;
    }

    @Override
    public void updatePersonInfo(Map<String, Object> personInfo) {
        if (!personInfo.containsKey(InformationConstant.PARAM_EMAIL)
                || !personInfo.containsKey(InformationConstant.PARAM_PHONE)
                || !personInfo.containsKey(InformationConstant.PARAM_MOBILE)
                || !personInfo.containsKey(InformationConstant.PARAM_ADDRESS)) {
            throw new ParameterIllegalException();
        }
        PeManager manager = this.getCurrentPeManager();
        manager.setEmail((String) personInfo.get(InformationConstant.PARAM_EMAIL));
        manager.setTelephone((String) personInfo.get(InformationConstant.PARAM_PHONE));
        manager.setMobile((String) personInfo.get(InformationConstant.PARAM_MOBILE));
        manager.setAddress((String) personInfo.get(InformationConstant.PARAM_ADDRESS));
        this.generalDao.save(manager);
    }

    @Override
    public void doUnbindWeChat() {
        String userId = this.userService.getCurrentUser().getId();
        this.generalDao
                .executeBySQL("update sso_user set wechat_union_id = null, wechat_nick_name = null where id = ?", userId);
        try {
            if (this.uCenterService.unbindThirdAccount(userId, 1, this.getSite().getSsoAppId())) {
                throw new ServiceException("解绑微信失败，同步用户中心失败！");
            }
        } catch (ApiInvokeFailedException e) {
            logger.error("unbind wechat failure", e);
            throw new ServiceException("解绑微信失败，同步用户中心失败！");
        }
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            throw new ParameterIllegalException("原始密码为空");
        }
        SsoUser user = this.generalDao.getById(SsoUser.class, this.userService.getCurrentUser().getId());
        if (!user.getPassword().equals(CommonUtils.md5(oldPassword))) {
            throw new ServiceException("原始密码错误");
        }
        // 验证密码强度
        if (newPassword.length() < 8 || !ValidateUtils.LETTER_PATTERN.matcher(newPassword).find()
                || !ValidateUtils.NUM_PATTERN.matcher(newPassword).find()) {
            throw new ServiceException("密码强度不够！密码规则：包含字母数字，8位长度以上");
        }
        user.setPassword(CommonUtils.md5(newPassword));
        this.generalDao.save(user);
        this.userDataOperateService.removeUserCache(Collections.singletonList(user.getLoginId()),
                CommonUtils.getServerName());
        UCenterUtils.updatePasswordForUCenterSingleUser(user.getLoginId(), user.getPassword(),
                SiteUtil.getSite().getSsoAppId());
    }

    /**
     * 获取当前管理员
     *
     * @return
     */
    private PeManager getCurrentPeManager() {
        DetachedCriteria dc = DetachedCriteria.forClass(PeManager.class);
        dc.createCriteria("ssoUser", "ssoUser")
                .add(Restrictions.eq("ssoUser.id", this.userService.getCurrentUser().getId()));
        List<PeManager> managerList = this.generalDao.getList(dc);
        if (CollectionUtils.isEmpty(managerList)) {
            throw new ServiceException("用户信息获取失败");
        }
        return managerList.get(0);
    }

    private Site getSite() {
        OAuth2Authentication authentication = this.userService.getAuthentication();
        if (authentication != null) {
            String clientId = authentication.getOAuth2Request().getClientId();
            if (StringUtils.isNotBlank(clientId)) {
                return this.siteService.getSiteByAppId(clientId);
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getBusinessUnitInfo() {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                               ");
        sql.append("    	manager.id managerId,                                                            ");
        sql.append("    	manager.login_id loginId,                                                        ");
        sql.append("    	manager.card_no cardNo,                                                          ");
        sql.append("    	manager.true_name trueName,                                                      ");
        sql.append("    	manager.mobile,                                                                  ");
        sql.append("    	ec.id unitPropertiesId,                                                          ");
        sql.append("    	ec.name unitPropertiesName,                                                      ");
        sql.append("    	unit. NAME unitName,                                                             ");
        sql.append("    	unit.unit_phone unitPhone,                                                       ");
        sql.append("    	unit.zip_code zipCode,                                                           ");
        sql.append("    	unit.unit_address unitAddress                                                    ");
        sql.append("    FROM                                                                                 ");
        sql.append("    	sso_user su                                                                      ");
        sql.append("    INNER JOIN pe_manager manager on manager.fk_sso_user_id = su.ID                      ");
        sql.append("    INNER JOIN pe_business_unit unit ON unit.fk_sso_user_id = su.id                      ");
        sql.append("    LEFT JOIN enum_const ec ON ec.id = unit.flag_unit_properties                         ");
        sql.append("    WHERE                                                                                ");
        sql.append("    	manager.id = ?");
        Map<String, Object> managerInfoMap = this.generalDao.getOneMapBySQL(sql.toString(), this.getCurrentPeManager().getId());
        if (MapUtils.isNotEmpty(managerInfoMap)) {
            managerInfoMap.put("unitProperties", this.generalDao.
                    getMapBySQL("SELECT id, name FROM enum_const WHERE NAMESPACE = 'FlagUnitProperties'"));
        }
        return managerInfoMap;
    }

    @Override
    public void updateBusinessUnitInfo(Map<String, Object> businessUnitInfo) {
        TycjParameterAssert.isAllNotEmpty(businessUnitInfo);
        StringBuilder sql = new StringBuilder();
        sql.append("    UPDATE pe_manager manager                                                                    ");
        sql.append("    INNER JOIN sso_user su on su.id = manager.fk_sso_user_id                                     ");
        sql.append("    INNER JOIN pe_business_unit unit ON unit.fk_sso_user_id = su.id                              ");
        sql.append("    SET manager.card_no = '" + businessUnitInfo.get("cardNo") + "',                              ");
        sql.append("     manager.true_name = '" + businessUnitInfo.get("trueName") + "',                             ");
        sql.append("     manager.mobile = '" + businessUnitInfo.get("mobile") + "',                                  ");
        sql.append("     unit.flag_unit_properties = '" + businessUnitInfo.get("unitPropertiesId") + "',             ");
        sql.append("     unit. NAME = '" + businessUnitInfo.get("unitName") + "',                                    ");
        sql.append("     unit.unit_phone = '" + businessUnitInfo.get("unitPhone") + "',                              ");
        sql.append("     unit.zip_code = '" + businessUnitInfo.get("zipCode") + "',                                  ");
        sql.append("     unit.unit_address = '" + businessUnitInfo.get("unitAddress") + "'                           ");
        sql.append("    WHERE                                                                                        ");
        sql.append("    	manager.id = '" + this.getCurrentPeManager().getId() + "'                                ");
        this.generalDao.executeBySQL(sql.toString());
    }

}
