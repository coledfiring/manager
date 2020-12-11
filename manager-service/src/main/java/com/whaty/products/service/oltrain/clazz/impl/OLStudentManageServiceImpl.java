package com.whaty.products.service.oltrain.clazz.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PePriRole;
import com.whaty.domain.bean.SsoUser;
import com.whaty.domain.bean.online.OlPeStudent;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.grid.doamin.ExcelCheckResult;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.whaty.constant.CommonConstant.PROFILE_PICTURE_PATH;

/**
 * 学员基本信息管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("olStudentManageService")
public class OLStudentManageServiceImpl extends TycjGridServiceAdapter<OlPeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(OlPeStudent bean) throws EntityException {
        this.checkBeforeAddAndUpdate(bean);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
        bean.setLoginId(bean.getMobile());
        SsoUser ssoUser = new SsoUser();
        ssoUser.setSiteCode(SiteUtil.getSiteCode());
        ssoUser.setEnumConstByFlagIsvalid(this.myGeneralDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ISVALID, "1"));
        ssoUser.setPassword(CommonUtils.md5(bean.getLoginId()));
        ssoUser.setPePriRole(this.myGeneralDao.getById(PePriRole.class, "0"));
        ssoUser.setEnumConstByFlagGender(bean.getEnumConstByFlagGender());
        ssoUser.setLoginId(bean.getLoginId());
        ssoUser.setTrueName(bean.getTrueName());
        String profilePicture = String.format(PROFILE_PICTURE_PATH, SiteUtil.getSiteCode(), "student",
                ssoUser.getLoginId());
        ssoUser.setProfilePicture(profilePicture);
        this.myGeneralDao.save(ssoUser);
        this.myGeneralDao.flush();
        bean.setSsoUser(ssoUser);
    }

    @Override
    public void checkBeforeUpdate(OlPeStudent bean) throws EntityException {
        this.checkBeforeAddAndUpdate(bean);
        bean.setLoginId(bean.getMobile());
    }

    @Override
    protected ExcelCheckResult checkExcelImportError(List<OlPeStudent> beanList, Workbook workbook,
                                                     ExcelCheckResult excelCheckResult) {
        Map<String, Map<String, List<Integer>>> sameDataMap = new HashMap<>();
        sameDataMap.put("表格中已存在相同手机号的学员；", this.checkSameField(beanList, Arrays.asList("mobile")));
        return this.checkSameData(sameDataMap, workbook, excelCheckResult);
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
}
