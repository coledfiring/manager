package com.whaty.products.service.resource.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.Site;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.user.util.SsoConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PePriRole;
import com.whaty.domain.bean.PeTeacher;
import com.whaty.domain.bean.SsoUser;
import com.whaty.file.excel.upload.constant.ExcelConstant;
import com.whaty.file.excel.upload.service.ExcelUploadService;
import com.whaty.file.excel.upload.util.ExcelUtils;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
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
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.products.service.common.template.AbstractZipReadTemplate;
import com.whaty.products.service.resource.template.TeacherPictureZipUploadTemplate;
import com.whaty.products.service.user.UserDataOperateService;
import com.whaty.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import com.whaty.utils.HibernatePluginsUtil;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.whaty.constant.CommonConstant.PROFILE_PICTURE_PATH;

/**
 * 师资管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("teacherResourceManageService")
public class TeacherResourceManageServiceImpl extends TycjGridServiceAdapter<PeTeacher> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Resource(name = "userDataOperateService")
    private UserDataOperateService userDataOperateService;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    @Resource(name = CommonConstant.EXCEL_UPLOAD_BEAN_NAME)
    private ExcelUploadService excelUploadService;

    private final WordImageGenerator generator = new WordImageGenerator(120, 120);

    /**
     * 重置密码
     *
     * @param ids
     * @return
     */
    @LogAndNotice("教师重置密码")
    public int doResetPassword(String ids, Site site) {
        if (StringUtils.isBlank(ids)) {
            throw new ParameterIllegalException(CommonConstant.PARAM_IDS);
        }
        StringBuilder sql = new StringBuilder();
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                    ");
        sql.append(" 	su.login_id as loginId,                                ");
        sql.append(" 	su. PASSWORD as password                               ");
        sql.append(" FROM                                                      ");
        sql.append(" 	sso_user su                                            ");
        sql.append(" INNER JOIN pe_teacher tea ON tea.fk_sso_user_id = su.id   ");
        sql.append(" WHERE                                                     ");
        sql.append(CommonUtils.madeSqlIn(ids, "tea.id"));
        List<Map<String, Object>> oldUserInfo = this.myGeneralDao.getMapBySQL(sql.toString());
        if (CollectionUtils.isEmpty(oldUserInfo)) {
            throw new ServiceException("教师不存在");
        }
        sql.delete(0, sql.length());
        sql.append(" UPDATE                                                ");
        sql.append(" 	pe_teacher tea                                     ");
        sql.append(" INNER JOIN sso_user ss ON ss.id = tea.FK_SSO_USER_ID  ");
        sql.append(" SET                                                   ");
        sql.append(" 	ss.password = md5(ss.login_id)                     ");
        sql.append(" WHERE                                                 ");
        sql.append(CommonUtils.madeSqlIn(ids, "tea.id"));
        int count = this.myGeneralDao.executeBySQL(sql.toString());
        List<String> loginIdList = oldUserInfo.stream().map(e -> (String) e.get("loginId"))
                .collect(Collectors.toList());
        userDataOperateService.removeUserCache(loginIdList, site.getDomain());
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                 ");
        sql.append(" 	su.login_id as loginId,                             ");
        sql.append(" 	su. PASSWORD as password                            ");
        sql.append(" FROM                                                   ");
        sql.append(" 	sso_user su                                         ");
        sql.append(" INNER JOIN pe_teacher tea ON tea.fk_sso_user_id = su.id");
        sql.append(" WHERE                                                  ");
        sql.append(CommonUtils.madeSqlIn(ids, "tea.id"));
        List<Map<String, Object>> userInfo = this.myGeneralDao.getMapBySQL(sql.toString());
        UCenterUtils.updatePasswordForUCenterUser(userInfo, oldUserInfo,
                SiteUtil.getSite(site.getCode()).getSsoAppId());
        return count;
    }

    @Override
    public void checkBeforeAdd(PeTeacher bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        //设置用户名
        bean.setLoginId(this.generateTeacherLoginId());
        bean.setLoginId(bean.getLoginId().toLowerCase());
        bean.setCreateDate(new Date());
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setEnumConstByFlagCourseTeacher(this.myGeneralDao.getEnumConstByNamespaceCode(
                CommonConstant.ENUM_CONST_NAMESPACE_FLAG_COURSE_TEACHER, "1"));
        bean.setEnumConstByFlagTutorTeacher(this.myGeneralDao.getEnumConstByNamespaceCode(
                CommonConstant.ENUM_CONST_NAMESPACE_FLAG_TUTOR_TEACHER, "1"));
        bean.setEnumConstByFlagIsclassmaster(this.myGeneralDao.getEnumConstByNamespaceCode(
                CommonConstant.ENUM_CONST_NAMESPACE_FLAG_IS_CLASSMASTER, "0"));
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
        bean.setName(bean.getTrueName() + "/" + bean.getLoginId() + "/" + bean.getWorkUnit());
    }

    @Override
    protected void afterAdd(PeTeacher bean) throws EntityException {
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
    public void afterExcelImport(List<PeTeacher> beanList) throws EntityException {
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
            List<String[]> learnSpaceData = beanList.stream().map(bean -> new String[]{bean.getId(),
                    bean.getLoginId(), bean.getTrueName(), bean.getSsoUser().getId(), null,
                    MasterSlaveRoutingDataSource.getDbType(), bean.getSsoUser().getPassword()})
                    .collect(Collectors.toList());
            try {
                this.learningSpaceWebService.saveTeacherBatch(learnSpaceData);
            } catch (Exception e) {
                try {
                    List<String> loginIds = beanList.stream().map(PeTeacher::getLoginId).collect(Collectors.toList());
                    UCenterUtils.removeUserFromUCenter(loginIds, SiteUtil.getSite().getSsoAppId());
                } catch (Exception e1) {
                    logger.error(e1);
                }
                throw new EntityException(CommonConstant.LEARNING_SPACE_ERROR);
            }
        }
    }

    @Override
    public void checkBeforeUpdate(PeTeacher bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setLoginId(bean.getLoginId().toLowerCase());
        bean.getSsoUser().setLoginId(bean.getLoginId());
        bean.getSsoUser().setLearnspaceSiteCode(LearnSpaceUtil.getLearnSpaceSiteCode());
        bean.getSsoUser().setEnumConstByFlagIsvalid(this.myGeneralDao.getEnumConstByNamespaceAndName(
                CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ISVALID, bean.getEnumConstByFlagActive().getCode()));
        bean.getSsoUser().setTrueName(bean.getTrueName());
        this.myGeneralDao.save(bean.getSsoUser());
        bean.setName(bean.getTrueName() + "/" + bean.getLoginId() + "/" + bean.getWorkUnit());
    }

    @Override
    protected void afterUpdate(PeTeacher bean) throws EntityException {
        SsoUser oldUser = this.openGeneralDao.getById(SsoUser.class, bean.getSsoUser().getId());
        try {
            UCenterUtils.updateUCenterSingleUser(oldUser, bean.getSsoUser(), SiteUtil.getSite().getSsoAppId());
        } catch (UCenterException e) {
            logger.error("update teacher failure", e);
            throw new EntityException(e.getInfo());
        }
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                if (!learningSpaceWebService.updateTeacher(bean.getId(), bean.getLoginId(), bean.getTrueName(),
                        bean.getSsoUser().getPassword(), LearnSpaceUtil.getLearnSpaceSiteCode())) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                logger.error("update teacher failure", e);
                UCenterUtils.updateUCenterSingleUser(bean.getSsoUser(), oldUser, SiteUtil.getSite().getSsoAppId());
                throw new EntityException("同步课程空间失败");
            }
        }
    }

    private void checkBeforeAddOrUpdate(PeTeacher bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " AND id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_teacher where login_id = ? and site_code = ?"
                + additionalSql, bean.getLoginId(), MasterSlaveRoutingDataSource.getDbType())) {
            throw new EntityException("此用户名已被占用");
        }
        if ((bean.getEnumConstByFlagCardType() != null && StringUtils.isBlank(bean.getCardNo()))
                || (bean.getEnumConstByFlagCardType() == null && StringUtils.isNotBlank(bean.getCardNo()))) {
            throw new EntityException("证件号跟证件类型必须都为空或都不为空");
        }
        if (bean.getEnumConstByFlagCardType() != null) {
            bean.setEnumConstByFlagCardType(this.myGeneralDao
                    .getById(EnumConst.class, bean.getEnumConstByFlagCardType().getId()));
            if ("1".equals(bean.getEnumConstByFlagCardType().getCode())) {
                if (!ValidateUtils.checkCardNoReg(bean.getCardNo())) {
                    throw new EntityException("身份证号格式不正确");
                }
                if (bean.getEnumConstByFlagGender() != null && !ValidateUtils.checkCardNoSex(bean.getCardNo(),
                        this.myGeneralDao.getById(EnumConst.class, bean.getEnumConstByFlagGender().getId()).getName())) {
                    throw new EntityException("身份证号与性别不对应");
                }
                if (bean.getBirthday() != null
                        && !ValidateUtils.checkCardNoBirthday(bean.getCardNo(),
                        CommonUtils.changeDateToString(bean.getBirthday()))) {
                    throw new EntityException("身份证号与出生日期不对应");
                }
            }
        }
    }

    @Override
    public Map delete(GridConfig gridConfig, String ids) {
        List idList = com.whaty.core.commons.util.CommonUtils.convertIdsToList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            return com.whaty.core.commons.util.CommonUtils.createFailInfoMap("参数错误");
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_teacher tea " +
                "inner join enum_const ac on ac.id = tea.flag_active where ac.code = '1' and " +
                CommonUtils.madeSqlIn(idList, "tea.id"))) {
            return com.whaty.core.commons.util.CommonUtils.createFailInfoMap("存在有效的数据，无法删除");
        }

        List ssoUserIdList = myGeneralDao.getBySQL("select fk_sso_user_id from pe_teacher where "
                + CommonUtils.madeSqlIn(idList, "id"));
        if (HibernatePluginsUtil.validateReferencingCurrentSession(PeTeacher.class, ids)) {
            return com.whaty.core.commons.util.CommonUtils.createFailInfoMap("数据已被其他信息引用，不能删除");
        }
        int n = this.myGeneralDao.deleteByIds(PeTeacher.class, idList);
        if (HibernatePluginsUtil.validateReferencingCurrentSession(SsoUser.class,
                CommonUtils.join(ssoUserIdList, ",", null))) {
            return com.whaty.core.commons.util.CommonUtils.createFailInfoMap("数据已被其他信息引用，不能删除");
        }
        // 删除教师后删除相应用户
        myGeneralDao.executeBySQL("delete from sso_user where " + CommonUtils.madeSqlIn(ssoUserIdList, "id"));
        try {
            this.afterDelete(idList);
        } catch (EntityException e) {
            throw new ServiceException(e.getMessage());
        }
        return com.whaty.core.commons.util.CommonUtils
                .createSuccessInfoMap(String.format("删除成功，共删除%s条数据", n));
    }

    @Override
    protected void afterDelete(List idList) throws EntityException {
        List<Map<String, Object>> teacherInfo = this.openGeneralDao
                .getMapBySQL("SELECT tea.TRUE_NAME AS trueName, tea.login_id AS loginId, ss.password as password" +
                        " FROM pe_teacher tea inner join sso_user ss on ss.id = tea.fk_sso_user_id where " +
                        CommonUtils.madeSqlIn(idList, "tea.id"));
        List<String> loginIds = teacherInfo.stream().map(e -> (String) e.get("loginId")).collect(Collectors.toList());
        UCenterUtils.removeUserFromUCenter(loginIds, SiteUtil.getSite().getSsoAppId());
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            // 保存已保存的id用于回滚课程空间
            List<String> savedIds = new ArrayList<>();
            for (Object id : idList) {
                try {
                    //同步新课程空间删除教师，如果同步失败则回滚
                    learningSpaceWebService.removeTeacher((String) id, LearnSpaceUtil.getLearnSpaceSiteCode());
                    savedIds.add((String) id);
                } catch (Exception e) {
                    DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PeTeacher.class)
                            .add(Restrictions.in("id", savedIds));
                    List<PeTeacher> peTeachers = this.openGeneralDao.getList(detachedCriteria);
                    for (PeTeacher peTeacher : peTeachers) {
                        try {
                            //新课程空间回滚
                            if (!learningSpaceWebService.saveTeacher(peTeacher.getId(),
                                    peTeacher.getLoginId(), peTeacher.getTrueName(),
                                    peTeacher.getSsoUser().getId(), null,
                                    LearnSpaceUtil.getLearnSpaceSiteCode(),
                                    peTeacher.getSsoUser().getPassword())) {
                                logger.warn(String.format("roll back teacher info failure: [%s]", peTeacher));
                            }
                        } catch (Exception e1) {
                            logger.error(String.format("roll back teacher info failure: [%s]", peTeacher), e1);
                        }
                    }
                    try {
                        UCenterUtils.synchronousUserToUCenter(teacherInfo, SiteUtil.getSite().getSsoAppId());
                    } catch (Exception e1) {
                        logger.error("roll back uCenter failure", e1);
                    }
                    throw new LearningSpaceException(e);
                }
            }
        }
    }

    /**
     * 上传图片
     *
     * @param upload
     * @return
     */
    @LogAndNotice("上传教师图片")
    public Map<String, Integer> doUploadPicture(File upload) {
        Map<String, Integer> resultMap = new HashMap<>(2);
        try {
            Map<String, Object> handleMap = new TeacherPictureZipUploadTemplate(upload).readZip();
            int totalNum = (Integer) handleMap.get(AbstractZipReadTemplate.RESULT_TOTAL_NUM);
            int successNum = (Integer) handleMap.get(AbstractZipReadTemplate.RESULT_SUCCESS_NUM);
            List<String> errorList = (List<String>) handleMap.get(AbstractZipReadTemplate.RESULT_ERROR_LIST);
            resultMap.put(TeacherPictureZipUploadTemplate.RESULT_TOTAL_NUM, totalNum);
            resultMap.put(TeacherPictureZipUploadTemplate.RESULT_SUCCESS_NUM, successNum);
            if (CollectionUtils.isNotEmpty(errorList)) {
                // 有错误数据则发送通知
                String webPath = ExcelUtils.getDefaultErrorInfoFilePath();
                String realPath = CommonUtils.getRealPath(webPath);
                this.excelUploadService.writeErrorInfoToFile(realPath, errorList);
                Map<String, Object> noticeMap = new HashMap<>(4);
                noticeMap.put(ExcelConstant.UPLOAD_RETURN_PARAM_INFO, "教师图片批量导入成功，成功导入"
                        + successNum + "个文件，失败" + (totalNum - successNum) + "个文件");
                noticeMap.put(ExcelConstant.UPLOAD_RETURN_FILE_PATH_INFO, webPath);
                NoticeServerPollUtils.noticeUploadError(noticeMap);
            }
            return resultMap;
        } catch (IllegalArgumentException e) {
            throw new ServiceException("文件命名错误，照片名中不可带有数字跟26个字母以外的字符");
        } catch (Exception e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 同步教师数据
     *
     * @param ids
     */
    @LogAndNotice("同步数据")
    public void doSyncData(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                        ");
        sql.append("    	su.id ssoUserId,                                          ");
        sql.append("    	su.login_id loginId,                                      ");
        sql.append("    	tea.true_name trueName,                                   ");
        sql.append("    	su.FK_ROLE_ID roleId,                                     ");
        sql.append("    	su. PASSWORD PASSWORD,                                    ");
        sql.append("    	su.site_code siteCode,                                    ");
        sql.append("    	tea.id teaId                                               ");
        sql.append("    FROM                                                          ");
        sql.append("    	pe_teacher tea                                            ");
        sql.append("    INNER JOIN sso_user su ON su.id = tea.fk_sso_user_id          ");
        sql.append("    WHERE                        " + CommonUtils.madeSqlIn(ids, "tea.id"));
        List<Map<String, Object>> userInfo = this.myGeneralDao.getMapBySQL(sql.toString());
        List<String> loginIdList = new ArrayList<>();
        List<String[]> learnSpaceList = new ArrayList<>();
        userInfo.forEach(e -> {
            loginIdList.add((String) e.get("loginId"));
            learnSpaceList.add(new String[]{
                    (String) e.get("teaId"),
                    (String) e.get("loginId"),
                    (String) e.get("trueName"),
                    (String) e.get("ssoUserId"),
                    null,
                    (String) e.get("siteCode"),
                    (String) e.get("password")
            });
        });
        UCenterUtils.synchronousUserToUCenter(userInfo, SiteUtil.getSite().getSsoAppId());
        if (LearnSpaceUtil.learnSpaceIsOpen(SiteUtil.getSite().getCode())) {
            try {
                if (!learningSpaceWebService.saveManagerBatch(learnSpaceList)) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                try {
                    UCenterUtils.removeUserFromUCenter(loginIdList, SiteUtil.getSite().getSsoAppId());
                } catch (Exception e1) {
                    logger.error("roll back failure", e1);
                }
                throw new LearningSpaceException(e);
            }
        }
    }

    /**
     * 生成用户名
     *
     * @return
     */
    private String generateTeacherLoginId() {
        String code = CommonUtils.changeDateToString(new Date(), "yyyy");
        String hql = "select max(loginId) from SsoUser where length(loginId) = ? and loginId like '" + code + "%'";
        String maxCode = (String) this.myGeneralDao.getOneByHQL(hql, code.length() + 4);
        if (StringUtils.isNotBlank(maxCode)) {
            maxCode = maxCode.replace(code, "");
            maxCode = code + CommonUtils.leftAddZero(Integer.parseInt(maxCode) + 1, 4);
        } else {
            maxCode = code + CommonUtils.leftAddZero(1, 4);
        }
        return maxCode;
    }

    /**
     * 生成头像
     *
     * @param ids
     */
    public void doGenerateProfilePicture(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        List<Map<String, Object>> userInfo = this.myGeneralDao
                .getMapBySQL("select ss.id as userId, tea.true_name as name, ss.login_id as loginId " +
                        "from pe_teacher tea " +
                        "inner join sso_user ss on ss.id = tea.fk_sso_user_id " +
                        "where " + CommonUtils.madeSqlIn(ids, "tea.id"));
        if (CollectionUtils.isEmpty(userInfo)) {
            return;
        }
        userInfo.forEach(user -> {
            String profilePicture = String.format(PROFILE_PICTURE_PATH, SiteUtil.getSiteCode(), "teacher",
                    user.get("loginId"));
            try {
                String name = (String) user.get("name");
                this.generator.generateProfilePicture(name.substring(name.length() - 2),
                        CommonUtils.mkDir(CommonUtils.getRealPath(profilePicture)));
            } catch (IOException e) {
                throw new UncheckException(e);
            }
            this.myGeneralDao.executeBySQL("update sso_user set profile_picture = ? where id = ?",
                    profilePicture, user.get("userId"));
        });
    }

    /**
     * 解绑微信用户
     *
     * @param ids
     * @return
     */
    public int doUnbindWeChatUser(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" delete wu                                                             ");
        sql.append(" from wechat_user wu                                                   ");
        sql.append(" inner join pe_teacher man on man.fk_sso_user_id=wu.fk_sso_user_id     ");
        sql.append(" where                                                                 ");
        sql.append(CommonUtils.madeSqlIn(ids, "man.id"));
        return this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 解除小程序绑定
     * @param ids
     * @return
     */
    public int doUnbindWeChatAppUser(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        String currentCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        int count = this.openGeneralDao.executeBySQL("delete from wechat_app_user where "
                + CommonUtils.madeSqlIn(ids, "fk_sso_user_id"));
        MasterSlaveRoutingDataSource.setDbType(currentCode);
        return count;
    }

}
