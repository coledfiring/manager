package com.whaty.products.service.oltrain.clazz.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PePriRole;
import com.whaty.domain.bean.SsoUser;
import com.whaty.domain.bean.online.OlClassStudent;
import com.whaty.domain.bean.online.OlPeStudent;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

import static com.whaty.constant.CommonConstant.PROFILE_PICTURE_PATH;

/**
 * 在线培训学员管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("olClassStudentManageService")
public class OLClassStudentManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<OlClassStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(OlClassStudent bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        this.checkBeforeAddAndUpdate(bean.getOlPeStudent());
        String siteCode = MasterSlaveRoutingDataSource.getDbType();
        SsoUser createBy = UserUtils.getCurrentUser();
        bean.getOlPeStudent().setCreateBy(createBy);
        bean.getOlPeStudent().setCreateDate(new Date());
        bean.getOlPeStudent().setSiteCode(siteCode);
        bean.getOlPeStudent().setLoginId(bean.getOlPeStudent().getMobile());
        this.myGeneralDao.save(bean.getOlPeStudent());
        SsoUser ssoUser = new SsoUser();
        ssoUser.setSiteCode(siteCode);
        ssoUser.setEnumConstByFlagIsvalid(this.myGeneralDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ISVALID, "1"));
        ssoUser.setPassword(CommonUtils.md5(bean.getOlPeStudent().getLoginId()));
        ssoUser.setPePriRole(this.myGeneralDao.getById(PePriRole.class, "0"));
        ssoUser.setEnumConstByFlagGender(bean.getOlPeStudent().getEnumConstByFlagGender());
        ssoUser.setLoginId(bean.getOlPeStudent().getLoginId());
        ssoUser.setTrueName(bean.getOlPeStudent().getTrueName());
        String profilePicture = String.format(PROFILE_PICTURE_PATH, SiteUtil.getSiteCode(), "student",
                ssoUser.getLoginId());
        ssoUser.setProfilePicture(profilePicture);
        this.myGeneralDao.save(ssoUser);
        this.myGeneralDao.flush();
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateTime(new Date());
        bean.getOlPeStudent().setSsoUser(ssoUser);
    }

    @Override
    public void checkBeforeUpdate(OlClassStudent bean) throws EntityException {
        bean.getOlPeStudent().setLoginId(bean.getOlPeStudent().getMobile());
        bean.setOlPeStudent((OlPeStudent) this.setSubIds(this.myGeneralDao.getById(OlClassStudent.class, bean.getId())
                .getOlPeStudent(), bean.getOlPeStudent()));
        super.checkBeforeUpdate(bean);
    }

    /**
     * 验证姓名长度以及用户名，手机号码是否重复
     *
     * @param bean
     */
    public void checkBeforeAddAndUpdate(OlPeStudent bean) throws EntityException {
        if (StringUtils.isBlank(bean.getTrueName()) || bean.getTrueName().length() < 2) {
            throw new EntityException("姓名长度请在2字及以上");
        }
        if (myGeneralDao.checkNotEmpty(String.format(" SELECT 1 FROM ol_pe_student " +
                        " WHERE site_code = ? AND mobile = ? %s ",
                StringUtils.isBlank(bean.getId()) ? "" : " AND id <> '" + bean.getId() + "' "),
                SiteUtil.getSiteCode(), bean.getMobile())) {
            throw new EntityException("已存在联系电话相同的学员");
        }
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "olPeClass.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "olPeClass.id";
    }
}
