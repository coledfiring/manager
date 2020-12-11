package com.whaty.products.service.oltrain.train.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.user.util.SsoConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PePriRole;
import com.whaty.domain.bean.SsoUser;
import com.whaty.domain.bean.online.OlPeTeacher;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.LearningSpaceException;
import com.whaty.framework.exception.UCenterException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.grid.doamin.ExcelCheckResult;
import com.whaty.framework.ucenter.utils.UCenterUtils;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 在线培训教师资源管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("olTeacherResourceManageService")
public class OLTeacherResourceManageServiceImpl extends TycjGridServiceAdapter<OlPeTeacher> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Override
    public void checkBeforeAdd(OlPeTeacher bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setCreateDate(new Date());
        bean.setCreateBy(UserUtils.getCurrentUser());
        SsoUser ssoUser = new SsoUser();
        bean.setSsoUser(ssoUser);
        ssoUser.setLoginId(bean.getLoginId());
        ssoUser.setEnumConstByFlagIsvalid(this.myGeneralDao.getEnumConstByNamespaceAndName(
                CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ISVALID, bean.getEnumConstByFlagActive().getCode()));
        ssoUser.setPassword(CommonUtils.md5(bean.getLoginId()));
        ssoUser.setLearnspaceSiteCode(LearnSpaceUtil.getLearnSpaceSiteCode());
        ssoUser.setSiteCode(SiteUtil.getSiteCode());
        ssoUser.setTrueName(bean.getTrueName());
        DetachedCriteria dc = DetachedCriteria.forClass(PePriRole.class);
        dc.add(Restrictions.eq("name", SsoConstant.SSO_DEFAULT_TEACHER_ROLE_NAME));
        PePriRole role = (PePriRole) this.myGeneralDao.getOneByHQL("from PePriRole where code = ? and siteCode = ?",
                SsoConstant.SSO_TEACHER, SiteUtil.getSiteCode());
        if (role != null) {
            ssoUser.setPePriRole(role);
        }
        this.myGeneralDao.save(ssoUser);
        bean.setName(bean.getTrueName() + "/" + bean.getLoginId());
    }

    @Override
    public void checkBeforeUpdate(OlPeTeacher bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.myGeneralDao.checkNotEmpty("select 1 from ol_pe_teacher where flag_active = ? and "
                + CommonUtils.madeSqlIn(idList, "id"), this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_ACTIVE, "1").getId())) {
            throw new EntityException("存在有效的数据，无法删除");
        }
    }

    @Override
    public void afterAdd(OlPeTeacher bean) throws EntityException {
        try {
            UCenterUtils.synchronousUserToUCenter(bean.getLoginId(), bean.getSsoUser().getPassword(),
                    SiteUtil.getSite().getSsoAppId());
        } catch (UCenterException e) {
            throw new EntityException(e.getInfo());
        }
        try {
            //向新课程空间保存教师
            if (LearnSpaceUtil.learnSpaceIsOpen()) {
                if (!learningSpaceWebService.saveTeacher(bean.getId(), bean.getLoginId(), bean.getTrueName(),
                        bean.getSsoUser().getId(), null, LearnSpaceUtil.getLearnSpaceSiteCode(),
                        bean.getSsoUser().getPassword())) {
                    throw new LearningSpaceException();
                }
            }
        } catch (Exception e) {
            UCenterUtils.removeUserFromUCenter(bean.getLoginId(), SiteUtil.getSite().getSsoAppId());
            throw new EntityException("课程空间同步失败");
        }
    }

    @Override
    protected void afterUpdate(OlPeTeacher bean) throws EntityException {
        SsoUser oldUser = this.openGeneralDao.getById(SsoUser.class, bean.getSsoUser().getId());
        try {
            UCenterUtils.updateUCenterSingleUser(oldUser, bean.getSsoUser(), SiteUtil.getSite().getSsoAppId());
        } catch (UCenterException e) {
            throw new EntityException(e.getInfo());
        }
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                if (!learningSpaceWebService.updateTeacher(bean.getId(), bean.getLoginId(), bean.getTrueName(),
                        bean.getSsoUser().getPassword(), LearnSpaceUtil.getLearnSpaceSiteCode())) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                UCenterUtils.updateUCenterSingleUser(bean.getSsoUser(), oldUser, SiteUtil.getSite().getSsoAppId());
                throw new EntityException("同步课程空间失败");
            }
        }
    }

    @Override
    public void afterExcelImport(List<OlPeTeacher> beanList) throws EntityException {
        try {
            List<Map<String, Object>> userInfo = beanList.stream().map(bean -> {
                Map<String, Object> user = new HashMap<>(4);
                user.put("trueName", bean.getTrueName());
                user.put("loginId", bean.getLoginId());
                user.put("password", bean.getSsoUser().getPassword());
                return user;
            }).collect(Collectors.toList());
            UCenterUtils.synchronousUserToUCenter(userInfo, SiteUtil.getSite().getSsoAppId());
        } catch (UCenterException e) {
            throw new EntityException(e.getInfo());
        }
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                List<String[]> learnSpaceData = beanList.stream().map(bean -> new String[]{bean.getId(),
                        bean.getLoginId(), bean.getTrueName(), bean.getSsoUser().getId(), null,
                        MasterSlaveRoutingDataSource.getDbType(), bean.getSsoUser().getPassword()})
                        .collect(Collectors.toList());
                learningSpaceWebService.saveTeacherBatch(learnSpaceData);
            } catch (Exception e) {
                throw new EntityException("课程空间同步失败");
            }
        }
    }

    @Override
    protected ExcelCheckResult checkExcelImportError(List<OlPeTeacher> beanList, Workbook workbook,
                                                     ExcelCheckResult excelCheckResult) {
        Map<String, Map<String, List<Integer>>> sameDataMap = new HashMap<>();
        sameDataMap.put("表格中已存在相同用户名的教师；", this.checkSameField(beanList, Arrays.asList("loginId")));
        return this.checkSameData(sameDataMap, workbook, excelCheckResult);
    }

    /**
     * 添加、修改前校验
     *
     * @param bean
     * @throws EntityException
     */
    private void checkBeforeAddOrUpdate(OlPeTeacher bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " and id<>'" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from ol_pe_teacher where login_id = ? and site_code = ? "
                + additionalSql, bean.getLoginId(), MasterSlaveRoutingDataSource.getDbType())) {
            throw new EntityException("已存在此用户名的教师");
        }
    }
}
