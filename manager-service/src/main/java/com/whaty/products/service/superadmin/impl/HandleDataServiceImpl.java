package com.whaty.products.service.superadmin.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeManager;
import com.whaty.domain.bean.PePriRole;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.LearningSpaceException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.products.service.superadmin.HandleDataService;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * 数据处理服务实现类
 *
 * @author weipengsen
 */
@Lazy
@Service("handleDataServiceImpl")
public class HandleDataServiceImpl implements HandleDataService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    @Override
    @LogAndNotice("保存openApi的登录用户")
    public void saveOpenApiLoginUser(String loginId, String password, String siteCode) {
        PeManager bean = new PeManager();
        bean.setSiteCode(siteCode);
        EnumConst active = this.generalDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, "1");
        bean.setLoginId(loginId);
        bean.setTrueName("openApi");
        bean.setEnumConstByFlagActive(active);
        List checkList = this.generalDao.getBySQL(" select 1 from sso_user where login_id = '"
                + bean.getLoginId() + "'");
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new ServiceException("添加失败，该用户名已存在！");
        }
        SsoUser ssoUser = new SsoUser();
        PePriRole pePriRole = (PePriRole) this.generalDao.getOneByHQL("from PePriRole where code = '155'");
        checkList = this.generalDao.getBySQL("select 1 from sso_user where fk_role_id = '" + pePriRole.getId() + "'");
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new ServiceException("一个业务平台只能有一个openApi管理员用户");
        }
        ssoUser.setPePriRole(pePriRole);
        ssoUser.setPassword(CommonUtils.md5(password));
        ssoUser.setLearnspaceSiteCode(LearnSpaceUtil.getLearnSpaceSiteCode(siteCode));
        ssoUser.setLoginId(bean.getLoginId());
        ssoUser.setEnumConstByFlagIsvalid(active);
        ssoUser.setSiteCode(siteCode);
        this.generalDao.save(ssoUser);
        bean.setSsoUser(ssoUser);
        this.generalDao.save(bean);
        this.generalDao.flush();
        // 添加系统常量
        String id = UUID.randomUUID().toString().replace("-", "");
        String sql = "insert into system_variables(id, name, value, note, site_code) values('" + id +
                "', 'openTokenLogin', '{username: \\'" + loginId + "\\', password: \\'" + password +
                "\\'}', 'openApi的token获取用户信息', '" + SiteUtil.getSiteCode() + "')";
        this.generalDao.executeBySQL(sql);
        id = UUID.randomUUID().toString().replace("-", "");
        sql = "insert into system_variables(id, name, value, note, site_code) values('" + id +
                "', 'openLoginManage', '" + loginId + "', 'openApi访问课程空间管理员简单登录loginId', '"
                + SiteUtil.getSiteCode() + "')";
        this.generalDao.executeBySQL(sql);
        // 同步课程空间
        if (LearnSpaceUtil.learnSpaceIsOpen(siteCode)) {
            try {
                boolean flag = learningSpaceWebService.saveManager(
                        bean.getId(),
                        bean.getSsoUser().getLoginId(),
                        bean.getTrueName(),
                        bean.getSsoUser().getId(),
                        null,
                        LearnSpaceUtil.getLearnSpaceSiteCode(siteCode),
                        bean.getSsoUser().getPassword()
                );
                if (!flag) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                throw new LearningSpaceException(e);
            }
        }
    }

}
