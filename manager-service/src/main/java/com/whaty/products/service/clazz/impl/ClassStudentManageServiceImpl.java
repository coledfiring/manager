package com.whaty.products.service.clazz.impl;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeClass;
import com.whaty.domain.bean.PePriRole;
import com.whaty.domain.bean.PeStudent;
import com.whaty.domain.bean.SsoUser;
import com.whaty.file.excel.upload.constant.ExcelConstant;
import com.whaty.file.excel.upload.service.ExcelUploadService;
import com.whaty.file.excel.upload.util.ExcelUtils;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.PlatformConfigUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.doamin.ExcelCheckResult;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.generator.WordImageGenerator;
import com.whaty.framework.im.TencentImManageSDK;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.products.service.clazz.constants.ClazzConstants;
import com.whaty.products.service.clazz.template.StudentPictureZipUploadTemplate;
import com.whaty.products.service.common.UtilService;
import com.whaty.products.service.common.template.AbstractZipReadTemplate;
import com.whaty.products.service.resource.template.TeacherPictureZipUploadTemplate;
import com.whaty.util.CommonUtils;
import com.whaty.util.SQLHandleUtils;
import com.whaty.util.ValidateUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import static com.whaty.constant.CommonConstant.PROFILE_PICTURE_PATH;

/**
 * 班级学员管理
 *
 * @author weipengsen
 */
@Lazy
@Service("classStudentManageService")
public class ClassStudentManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<PeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.EXCEL_UPLOAD_BEAN_NAME)
    private ExcelUploadService excelUploadService;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Resource(name = CommonConstant.UTIL_SERVICE_BEAN_NAME)
    private UtilService utilService;

    private final WordImageGenerator generator = new WordImageGenerator(120, 120);

    @Override
    public void checkBeforeAdd(PeStudent bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        this.checkBeforeAddAndUpdate(bean);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
        SsoUser ssoUser = new SsoUser();
        ssoUser.setSiteCode(SiteUtil.getSiteCode());
        ssoUser.setEnumConstByFlagIsvalid(this.myGeneralDao
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ISVALID, "1"));
        ssoUser.setPassword(CommonUtils.md5(this.myGeneralDao
                .getOneBySQL("select code from pe_class where id = ?", bean.getPeClass().getId())));
        ssoUser.setPePriRole(this.myGeneralDao.getById(PePriRole.class, "0"));
        ssoUser.setEnumConstByFlagGender(bean.getEnumConstByFlagGender());
        ssoUser.setLoginId(this.generateStudentLoginId(bean.getPeClass()));
        ssoUser.setTrueName(bean.getTrueName());
        String profilePicture = String.format(PROFILE_PICTURE_PATH, SiteUtil.getSiteCode(), "student",
                ssoUser.getLoginId());
        ssoUser.setProfilePicture(profilePicture);
        try {
            this.generator.generateProfilePicture(bean.getTrueName().substring(bean.getTrueName().length() - 2),
                    CommonUtils.mkDir(CommonUtils.getRealPath(profilePicture)));
        } catch (IOException e) {
            throw new UncheckException(e);
        }
        this.myGeneralDao.save(ssoUser);
        this.myGeneralDao.flush();
        bean.setSsoUser(ssoUser);
    }

    @Override
    protected void afterAdd(PeStudent bean) throws EntityException {
        super.afterAdd(bean);
        if (PlatformConfigUtil.getPlatformConfig().getOpenTencentIm()) {
            try {
                new TencentImManageSDK().insertUser(bean.getSsoUser().getId());
            } catch (Exception e) {
                throw new UncheckException(e);
            }
        }
        this.myGeneralDao.flush();
        this.generalTrainingId(bean);
    }

    /**
     * 生成trainingId
     * @param bean
     */
    private void generalTrainingId(PeStudent bean) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                      ");
        sql.append("    	RIGHT (max(training_id), 6) maxTrainCode                ");
        sql.append("    FROM                                                        ");
        sql.append("    	pe_student                                              ");
        sql.append("    WHERE                                                       ");
        sql.append("    	site_code = ?                                           ");
        sql.append("    AND training_id IS NOT NULL                                 ");
        sql.append("    AND DATE_FORMAT(NOW(), '%y') = substring(training_id, 2, 2) ");
        String maxTrainCode = myGeneralDao.getOneBySQL(sql.toString(), SiteUtil.getSiteCode());
        maxTrainCode = StringUtils.isBlank(maxTrainCode) ? "0" : maxTrainCode;

        sql.delete(0, sql.length());
        sql.append("    UPDATE pe_student                                       ");
        sql.append("    INNER JOIN (SELECT @i/*'*/:=/*'*/?) a ON 1 = 1          ");
        sql.append("    SET training_id = concat(                               ");
        sql.append("    	'7',                                                ");
        sql.append("    	DATE_FORMAT(NOW(), '%y'),                           ");
        sql.append("    	'01',                                               ");
        sql.append("    	LPAD((@i/*'*/:=/*'*/@i + 1), 6, '0')                ");
        sql.append("    )                                                       ");
        sql.append("    WHERE                                                   ");
        sql.append("    	site_code = ?                                       ");
        sql.append("    AND IFNULL(training_id, '') = ''                        ");
        sql.append("    AND id = ?                                              ");
        myGeneralDao.executeBySQL(sql.toString(), maxTrainCode, SiteUtil.getSiteCode(), bean.getId());
    }

    @Override
    protected ExcelCheckResult checkExcelImportError(List<PeStudent> beanList, Workbook workbook,
                                                     ExcelCheckResult excelCheckResult) {
        Map<String, Map<String, List<Integer>>> sameDataMap = new HashMap<>(2);
        sameDataMap.put("表格中已存在姓名及联系电话均相同的学员数据；",
                this.checkSameField(beanList, Arrays.asList("trueName", "mobile")));
        return this.checkSameData(sameDataMap, workbook, excelCheckResult);
    }

    /**
     * 生成用户名
     * @param clazz
     * @return
     */
    private String generateStudentLoginId(PeClass clazz) {
        String code = this.myGeneralDao
                .getOneBySQL("select code from pe_class where id = ?", clazz.getId());
        String sql = "select max(login_id) from sso_user where length(login_id) = ? and login_id like '" + code + "%'";
        String maxCode = this.myGeneralDao.getOneBySQL(sql, code.length() + 4);
        if (StringUtils.isNotBlank(maxCode)) {
            maxCode = maxCode.replace(code, "");
            maxCode = code + CommonUtils.leftAddZero(Integer.parseInt(maxCode) + 1, 4);
        } else {
            maxCode = code + CommonUtils.leftAddZero(1, 4);
        }
        return maxCode;
    }

    /**
     * 验证姓名长度以及姓名，手机号码是否重复
     *
     * @param bean
     */
    public void checkBeforeAddAndUpdate(PeStudent bean) throws EntityException {
        if ((bean.getEnumConstByFlagCardType() == null || StringUtils.isBlank(bean.getCardNo()))) {
            throw new EntityException("证件号和证件类型不能为空");
        }

        EnumConst enumCondtByFlagCardType = this.myGeneralDao.getById(EnumConst.class, bean.getEnumConstByFlagCardType().getId());

        if ("1".equals(enumCondtByFlagCardType.getCode())) {
            if (!ValidateUtils.checkCardNoReg(bean.getCardNo())) {
                throw new EntityException("身份证号格式不正确");
            }
            if (bean.getEnumConstByFlagGender() != null && !ValidateUtils.checkCardNoSex(bean.getCardNo(),
                    this.myGeneralDao.getById(EnumConst.class, bean.getEnumConstByFlagGender().getId()).getName())) {
                throw new EntityException("身份证号与性别不对应");
            }
        }

        if ("2".equals(enumCondtByFlagCardType.getCode()) && !ValidateUtils.checkGATNoReg(bean.getCardNo())) {
            throw new EntityException("港澳台证件格式不正确");
        }

        if ("3".equals(enumCondtByFlagCardType.getCode()) && !ValidateUtils.checkOfficersNoReg(bean.getCardNo())) {
            throw new EntityException("军官证证件格式不正确");
        }

        if (StringUtils.isBlank(bean.getTrueName()) || bean.getTrueName().length() < 2) {
            throw new EntityException("姓名长度请在2字及以上");
        }
        if (myGeneralDao.checkNotEmpty(String.format(" SELECT 1 FROM pe_student stu " +
                        " INNER JOIN pe_class c ON stu.fk_class_id = c.id " +
                        " WHERE c.id = ? AND stu.site_code = ? AND stu.mobile = ? AND stu.true_name = ? %s ",
                StringUtils.isBlank(bean.getId()) ? "" : " AND stu.id <> '" + bean.getId() + "' "),
                bean.getPeClass().getId(), SiteUtil.getSiteCode(), bean.getMobile(), bean.getTrueName())) {
            throw new EntityException("已存在姓名及联系电话均相同的学员");
        }
    }

    @LogAndNotice("上传学员图片")
    public Map<String, Integer> doUploadPicture(String classId, String uploadType, File upload) {
        TycjParameterAssert.isAllNotBlank(classId);
        TycjParameterAssert.fileAllExists(upload);
        Map<String, Integer> resultMap = new HashMap<>(2);
        try {
            Map<String, Object> extraParams = new HashMap<>(2);
            extraParams.put("classId", classId);
            extraParams.put("uploadType", uploadType);
            Map<String, Object> handleMap = new StudentPictureZipUploadTemplate(upload,
                    extraParams).readZip();
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
                noticeMap.put(ExcelConstant.UPLOAD_RETURN_PARAM_INFO, "学员图片批量导入成功，成功导入"
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
     * 勾选生成证书编号
     *
     * @param ids
     * @return
     */
    public int doGenerateCertificateNumber(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                            ");
        sql.append("   id as id                                                        ");
        sql.append(" from                                                              ");
        sql.append("   pe_student stu                                                  ");
        sql.append(" where                                                             ");
        sql.append("   certificate_number is null                                      ");
        sql.append(" and " + CommonUtils.madeSqlIn(ids, "id"));
        List<String> valuableIdList = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isEmpty(valuableIdList)) {
            throw new ServiceException("没有需要生成证书编号的数据");
        }
        sql.delete(0, sql.length());
        sql.append(" select                                                            ");
        sql.append("   un.code                                                         ");
        sql.append(" from                                                              ");
        sql.append("   pe_student stu                                                  ");
        sql.append(" inner join pe_class c on c.id = stu.fk_class_id                   ");
        sql.append(" inner join pe_unit un on un.id = c.fk_unit_id                     ");
        sql.append(" where                                                             ");
        sql.append("   stu.id = ?                                                      ");
        String code = this.myGeneralDao.getOneBySQL(sql.toString(), valuableIdList.get(0));
        String prefix = CommonUtils.changeDateToString(new Date(), "yyyy") + code;
        String maxCode = this.myGeneralDao
                .getOneBySQL("select max(certificate_number) from pe_student " +
                                "where substr(certificate_number, 1, ?) = ? and length(certificate_number) = ?",
                        prefix.length(), prefix, prefix.length() + 5);
        int maxSerial = 0;
        if (StringUtils.isNotBlank(maxCode)) {
            maxSerial = Integer.parseInt(maxCode.replace(prefix, ""));
        }
        sql.delete(0, sql.length());
        sql.append(" UPDATE pe_student stu                                              ");
        sql.append(" INNER JOIN (                                                       ");
        sql.append(" 	SELECT                                                          ");
        sql.append(" 		@row_number :=@row_number + 1 AS number,                    ");
        sql.append(" 		stu.id AS id                                                ");
        sql.append(" 	FROM                                                            ");
        sql.append(" 		(                                                           ");
        sql.append(" 			SELECT                                                  ");
        sql.append(" 				id                                                  ");
        sql.append(" 			FROM                                                    ");
        sql.append(" 				pe_student                                          ");
        sql.append("            WHERE                                                   ");
        sql.append(CommonUtils.madeSqlIn(valuableIdList, "id"));
        sql.append(" 			ORDER BY                                                ");
        sql.append(" 				IFNULL(order_no,99999),                             ");
        sql.append(" 				CONVERT (true_name USING 'gbk')                     ");
        sql.append(" 		) stu                                                       ");
        sql.append(" 	INNER JOIN (SELECT @row_number := 0) r                          ");
        sql.append(" ) num ON num.id = stu.id                                           ");
        sql.append(" SET certificate_number = concat(?, lpad(num.number + ?, 5, '0'))   ");
        sql.append(" WHERE                                                              ");
        sql.append(CommonUtils.madeSqlIn(valuableIdList, "stu.id"));
        return this.myGeneralDao.executeBySQL(SQLHandleUtils.handleSignInSQL(sql.toString()), prefix, maxSerial);
    }

    @Override
    public void checkBeforeUpdate(PeStudent bean) throws EntityException {
        super.checkBeforeUpdate(bean);
        this.checkBeforeAddAndUpdate(bean);
        this.myGeneralDao.executeBySQL("update sso_user ss inner join pe_student stu on stu.fk_sso_user_id = ss.id" +
                        " set stu.flag_gender = ?, ss.true_name = ? where stu.id = ?",
                bean.getEnumConstByFlagGender().getId(), bean.getTrueName(), bean.getId());
    }

    @Override
    protected void afterDelete(List idList) throws EntityException {
        List<String> userIds = this.openGeneralDao.getBySQL("select fk_sso_user_id from pe_student where " +
                CommonUtils.madeSqlIn(idList, "id"));
        List<String> profilePicture = this.myGeneralDao.getBySQL("select profile_picture from sso_user" +
                " where " + CommonUtils.madeSqlIn(userIds, "id"));
        profilePicture = profilePicture.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<File> files = CommonUtils.bakWebPathFiles(profilePicture);
        try {
            profilePicture.stream().map(CommonUtils::getRealPath).peek(CommonUtils::mkDir)
                    .map(File::new).filter(File::exists).forEach(File::delete);
            this.myGeneralDao.executeBySQL("delete from sso_user where " + CommonUtils.madeSqlIn(userIds, "id"));
        } catch (Exception e) {
            try {
                CommonUtils.rollbackFiles(files);
            } catch (Exception e1) { ;
            }
        }
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "peClass.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "peClass.id";
    }

    /**
     * 导出学生照片
     *
     * @param ids
     * @param response
     * @throws UnsupportedEncodingException
     */
    public void doExportStudentPicture(String ids, HttpServletResponse response) {
        TycjParameterAssert.isAllNotBlank(ids);
        List<Map<String, Object>> picUrls= this.myGeneralDao
                .getMapBySQL("SELECT picture_url picUrl, true_name trueName FROM pe_student WHERE "
                        + CommonUtils.madeSqlIn(ids, "id"));
        Map<String, File> fileMap = new HashMap<>(16);
        Map<String, Integer> serialMap = new HashMap<>(16);
        picUrls.stream().filter(e-> Objects.nonNull(e.get("picUrl")) && Objects.nonNull(e.get("trueName")))
                .forEach(e-> {
                    String trueName = (String) e.get("trueName");
                    String picUrl = (String) e.get("picUrl");
                    if(serialMap.containsKey(trueName)) {
                        serialMap.put(trueName, serialMap.get(trueName) + 1);
                    } else {
                        serialMap.put(trueName, 0);
                    }
                    String picType = picUrl.substring(picUrl.lastIndexOf("."));
                    trueName += serialMap.get(trueName) == 0? "" : serialMap.get(trueName);
                    fileMap.put(trueName + picType, new File(CommonUtils.getRealPath(picUrl)));
                });
        if(MapUtils.isEmpty(fileMap)) {
            throw new ServiceException("所选学员都没有照片");
        }
        try (ZipOutputStream out = new ZipOutputStream(response.getOutputStream())) {
            CommonUtils.setContentToDownload(response, "学员照片.rar");
            CommonUtils.packageToZip(fileMap, out);
        } catch (Exception e) {
            throw new ServiceException("导出错误，存在无法预览的图片");
        }
    }

    /**
     * 导出学员信息
     * @param outputStream
     */
    public void doExportStuCertificateInfo(String ids, OutputStream outputStream) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                ");
        sql.append("    	stu.true_name trueName,                                           ");
        sql.append("    	cardType. NAME cardTypeName,                                      ");
        sql.append("    	stu.card_no cardNo,                                               ");
        sql.append("    	gender. NAME genderName,                                          ");
        sql.append("    	'' birthday,                                                      ");
        sql.append("    	'',                                                               ");
        sql.append("    	'',                                                               ");
        sql.append("    	stu.work_unit unitName,                                           ");
        sql.append("    	stu.mobile mobile,                                                ");
        sql.append("    	stu.email email,                                                  ");
        sql.append("    	stu.positional_title zhiwu,                                       ");
        sql.append("    	'',                                                               ");
        sql.append("    	''                                                                ");
        sql.append("    FROM                                                                  ");
        sql.append("    	pe_student stu                                                    ");
        sql.append("    INNER JOIN enum_const gender ON gender.id = stu.flag_gender           ");
        sql.append("    LEFT JOIN enum_const cardType ON cardType.id = stu.flag_card_type     ");
        sql.append("    WHERE                 " + CommonUtils.madeSqlIn(ids, "stu.id"));
        this.utilService.generateExcelAndWrite(outputStream, null,
                ClazzConstants.UPLOAD_EXCEL_STUDENT_CERTIFICATE, null, myGeneralDao.getBySQL(sql.toString()));
    }
}
