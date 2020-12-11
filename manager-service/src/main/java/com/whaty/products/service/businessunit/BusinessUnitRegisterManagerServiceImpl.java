package com.whaty.products.service.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeStudent;
import com.whaty.file.excel.upload.exception.StreamOperateException;
import com.whaty.file.excel.upload.service.ExcelUploadService;
import com.whaty.file.excel.upload.util.ExcelUtils;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.businessunit.constants.BusinessUnitStudentEnrollConstant;
import com.whaty.products.service.businessunit.domain.EnrollStudentInfParameter;
import com.whaty.products.service.common.UtilService;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 单位报名信息管理service
 *
 * @author shanshuai
 */
@Lazy
@Service("businessUnitRegisterManagerService")
public class BusinessUnitRegisterManagerServiceImpl extends TycjGridServiceAdapter<PeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.UTIL_SERVICE_BEAN_NAME)
    private UtilService utilService;

    @Resource(name = "excelUploadService")
    private ExcelUploadService excelUploadService;

    /**
     * 获取单个学生报名时需要的信息 即期次信息、性别
     *
     * @return
     */
    public Map<String, Object> getEnrollInfo() {
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("genderList", myGeneralDao.
                getMapBySQL("SELECT id genderId, name genderName FROM enum_const where namespace = 'flaggender'"));
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                             ");
        sql.append("    	class.id classId,                                                              ");
        sql.append("    	class. NAME className,                                                         ");
        sql.append("    	class.training_person_number,                                                  ");
        sql.append("        IFNULL(item.food_fee, 0) foodFee,                                              ");
        sql.append("        IFNULL(item.room_fee, 0) roomFee,                                              ");
        sql.append("        IFNULL(item.material_fee, 0) materialFee                                       ");
        sql.append("    FROM                                                                               ");
        sql.append("    	pe_class class                                                                 ");
        sql.append("    INNER JOIN training_item item ON class.fk_training_item_id = item.id               ");
        sql.append("    LEFT JOIN pe_student stu ON stu.fk_class_id = class.id                             ");
        sql.append("    WHERE                                                                              ");
        sql.append("        class.site_code = ?                                                            ");
        sql.append("    and now() <= class.start_time                                                      ");
        sql.append("    GROUP BY                                                                           ");
        sql.append("    	class.id                                                                       ");
        sql.append("    HAVING                                                                             ");
        sql.append("    	class.training_person_number > COUNT(stu.id)                                   ");
        sql.append("    ORDER BY                                                                           ");
        sql.append("    	CONVERT (class. NAME USING gbk) ASC                                            ");
        resultMap.put("classInfosList", myGeneralDao.getMapBySQL(sql.toString(), SiteUtil.getSiteCode()));
        return resultMap;
    }

    /**
     * 列出可以用来报名的培训班级信息 即期次信息
     *
     * @return
     */
    public List<Object[]> listCanEnrollClassInfo() {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                             ");
        sql.append("    	class.id classId,                                                              ");
        sql.append("    	class. NAME className,                                                         ");
        sql.append("    	class.training_person_number                                                   ");
        sql.append("    FROM                                                                               ");
        sql.append("    	pe_class class                                                                 ");
        sql.append("    LEFT JOIN pe_student stu ON stu.fk_class_id = class.id                             ");
        sql.append("    WHERE                                                                              ");
        sql.append("        class.site_code = ?                                                            ");
        sql.append("    and now() <= class.start_time                                                      ");
        sql.append("    GROUP BY                                                                           ");
        sql.append("    	class.id                                                                       ");
        sql.append("    HAVING                                                                             ");
        sql.append("    	class.training_person_number > COUNT(stu.id)                                   ");
        sql.append("    ORDER BY                                                                           ");
        sql.append("    	CONVERT (class. NAME USING gbk) ASC                                            ");
        return myGeneralDao.getBySQL(sql.toString(), SiteUtil.getSiteCode());
    }

    /**
     * 单个学生单位报名
     *
     * @return
     */
    @LogAndNotice("单个学生单位报名")
    public void saveSingleBusinessEnrollInfo(EnrollStudentInfParameter enrollStudentInfParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                    ");
        sql.append("    	1                                                     ");
        sql.append("    FROM                                                      ");
        sql.append("    	pe_class class                                        ");
        sql.append("    WHERE                                                     ");
        sql.append("    	class.site_code = ?                                   ");
        sql.append("    AND class.id = ?                                          ");
        sql.append("    AND now() > class.start_time                              ");
        // 判断当前时间是否在所选期次开班时间之前
        if (CollectionUtils.isNotEmpty(myGeneralDao.
                getMapBySQL(sql.toString(), SiteUtil.getSiteCode(), enrollStudentInfParameter.getClassId()))) {
            throw new ServiceException("当前时间需要在期次开班截止时间之前,请刷新页面重新选择期次");
        }
        // 期次人数报名剩余大于等于1
        sql.delete(0, sql.length());
        sql.append("    SELECT                                                            ");
        sql.append("    	class.id,                                                     ");
        sql.append("    	class.training_person_number                                  ");
        sql.append("    FROM                                                              ");
        sql.append("    	pe_class class                                                ");
        sql.append("    left JOIN pe_student stu ON stu.fk_class_id = class.id            ");
        sql.append("    WHERE                                                             ");
        sql.append("    	class.site_code = ?                                           ");
        sql.append("    AND class.id = ?                                                  ");
        sql.append("    GROUP BY                                                          ");
        sql.append("    	class.id                                                      ");
        sql.append("    HAVING                                                            ");
        sql.append("    	class.training_person_number - COUNT(stu.id) < 1              ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.
                getMapBySQL(sql.toString(), SiteUtil.getSiteCode(), enrollStudentInfParameter.getClassId()))) {
            throw new ServiceException("本期次已经报满，请选择其他期次报名");
        }

        // 判断此学生是否已经报考本批次
        if (CollectionUtils.isNotEmpty(myGeneralDao.
                getMapBySQL("SELECT 1 FROM pe_student WHERE site_code = ? AND card_no = ? AND fk_class_id = ?",
                        SiteUtil.getSiteCode(), enrollStudentInfParameter.getCardNo(), enrollStudentInfParameter.getClassId()))) {
            throw new ServiceException("此学生已经报名过本期次，请勿重复报名");
        }

        // 判断是否首次报名
        String firstEnrollStatusCode = CollectionUtils.isNotEmpty(myGeneralDao.
                getMapBySQL("SELECT 1 FROM pe_student WHERE site_code = ? AND card_no = ? AND fk_class_id IS NOT NULL",
                        SiteUtil.getSiteCode(), enrollStudentInfParameter.getCardNo())) ? "0" : "1";
        EnumConst flagFirstEnrollStatus = myGeneralDao.
                getEnumConstByNamespaceCode("flagFirstEnrollStatus", firstEnrollStatusCode);
        if (!ValidateUtils.checkCardNoSex(enrollStudentInfParameter.getCardNo(),
                this.myGeneralDao.getById(EnumConst.class, enrollStudentInfParameter.getGenderId()).getName())) {
            throw new ServiceException("身份证号与性别不对应");
        }

        String stuId = UUID.randomUUID().toString().replace("-", "");
        // 新增学生
        sql.delete(0, sql.length());
        sql.append("    INSERT INTO pe_student (                                                             ");
        sql.append("    	id,                                                                              ");
        sql.append("    	true_name,                                                                       ");
        sql.append("    	card_no,                                                                         ");
        sql.append("    	email,                                                                           ");
        sql.append("    	flag_student_status,                                                             ");
        sql.append("    	mobile,                                                                          ");
        sql.append("    	create_by,                                                                       ");
        sql.append("    	create_date,                                                                     ");
        sql.append("    	site_code,                                                                       ");
        sql.append("    	flag_gender,                                                                     ");
        sql.append("    	work_unit,                                                                       ");
        sql.append("    	fk_business_unit_id,                                                             ");
        sql.append("    	address,                                                                         ");
        sql.append("    	positional_title,                                                                ");
        sql.append("    	professional_title,                                                              ");
        sql.append("    	fk_training_item_id,                                                             ");
        sql.append("    	fk_class_id,                                                                     ");
        sql.append("    	flag_fee_status,                                                                 ");
        sql.append("    	flag_first_enroll_status                                                         ");
        sql.append("    ) SELECT                                                                             ");
        sql.append("    	'" + stuId + "',                                                                 ");
        sql.append("    	'" + enrollStudentInfParameter.getTrueName() + "',                               ");
        sql.append("    	'" + enrollStudentInfParameter.getCardNo() + "',                                 ");
        sql.append("    	'" + enrollStudentInfParameter.getEmail() + "',                                  ");
        sql.append("    	stustatus.id,                                                                    ");
        sql.append("    	'" + enrollStudentInfParameter.getMobile() + "',                                 ");
        sql.append("    	'" + UserUtils.getCurrentUserId() + "',                                          ");
        sql.append("    	now(),                                                                           ");
        sql.append("    	'" + SiteUtil.getSiteCode() + "',                                                ");
        sql.append("    	'" + enrollStudentInfParameter.getGenderId() + "',                               ");
        sql.append("    	'" + enrollStudentInfParameter.getUnitName() + "',                               ");
        sql.append("    	unit.id,                                                                         ");
        sql.append("    	'" + enrollStudentInfParameter.getAddress() + "',                                ");
        sql.append("    	'" + enrollStudentInfParameter.getPositional() + "',                             ");
        sql.append("    	'" + enrollStudentInfParameter.getProfessional() + "',                           ");
        sql.append("    	class.fk_training_item_id,                                                       ");
        sql.append("    	class.id,                                                                        ");
        sql.append("    	feestatus.id,                                                                    ");
        sql.append("    	'" + flagFirstEnrollStatus.getId() + "'                                          ");
        sql.append("    FROM                                                                                 ");
        sql.append("    	pe_class class                                                                   ");
        sql.append("    INNER JOIN enum_const stustatus ON stustatus.namespace = 'FlagStudentStatus'         ");
        sql.append("    AND stustatus. CODE = '1'                                                            ");
        sql.append("    INNER JOIN pe_business_unit unit ON unit.fk_sso_user_id = '" +
                UserUtils.getCurrentUserId() + "'    ");
        sql.append("    INNER JOIN enum_const feestatus ON feestatus.NAMESPACE = 'flagfeestatus'             ");
        sql.append("    AND feestatus. CODE = '0'                                                            ");
        sql.append("    WHERE                                                                                ");
        sql.append("    	class.site_code = '" + SiteUtil.getSiteCode() + "'                               ");
        sql.append("    AND class.id = '" + enrollStudentInfParameter.getClassId() + "'                      ");
        sql.append("    AND NOT EXISTS (                                                                     ");
        sql.append("    	SELECT                                                                           ");
        sql.append("    		class.id,                                                                    ");
        sql.append("    		class.training_person_number                                                 ");
        sql.append("    	FROM                                                                             ");
        sql.append("    		pe_class class                                                               ");
        sql.append("    	LEFT JOIN pe_student stu ON stu.fk_class_id = class.id                           ");
        sql.append("    	WHERE                                                                            ");
        sql.append("    		class.site_code = '" + SiteUtil.getSiteCode() + "'                           ");
        sql.append("    	AND class.id = '" + enrollStudentInfParameter.getClassId() + "'                  ");
        sql.append("    	GROUP BY                                                                         ");
        sql.append("    		class.id                                                                     ");
        sql.append("    	HAVING                                                                           ");
        sql.append("    		class.training_person_number - COUNT(stu.id) < 1                             ");
        sql.append("    )                                                                                    ");
        sql.append("    AND NOT EXISTS (                                                                     ");
        sql.append("    	SELECT                                                                           ");
        sql.append("    		1                                                                            ");
        sql.append("    	FROM                                                                             ");
        sql.append("    		pe_student                                                                   ");
        sql.append("    	WHERE                                                                            ");
        sql.append("    		site_code = '" + SiteUtil.getSiteCode() + "'                                 ");
        sql.append("    	AND card_no = '" + enrollStudentInfParameter.getCardNo() + "'                    ");
        sql.append("    	AND fk_class_id = '" + enrollStudentInfParameter.getClassId() + "'               ");
        sql.append("    )                                                                                    ");
        myGeneralDao.executeBySQL(sql.toString());

        // 是否需要培训用书、是否需要餐券、是否单间住宿
        boolean isFoodFee = enrollStudentInfParameter.isChooseFoodFee();
        boolean isRoomFee = enrollStudentInfParameter.isChooseRoomFee();
        boolean isMaterialFee = enrollStudentInfParameter.isChooseMaterialFee();
        sql.delete(0, sql.length());
        sql.append("    INSERT INTO student_enroll_fee (                                                     ");
        sql.append("    	fk_student_id,                                                                   ");
        sql.append("    	room_fee,                                                                        ");
        sql.append("    	flag_need_room,                                                                  ");
        sql.append("    	material_fee,                                                                    ");
        sql.append("    	flag_need_materia,                                                               ");
        sql.append("    	food_fee,                                                                        ");
        sql.append("    	flag_need_food,                                                                  ");
        sql.append("    	train_fee,                                                                       ");
        sql.append("    	total_fee                                                                        ");
        sql.append("    )                                                                                    ");
        sql.append("    SELECT                                                                               ");
        sql.append("    	'" + stuId + "',                                                                 ");
        sql.append("    	item.room_fee,                                                                   ");
        sql.append("    	needRoom.id,                                                                     ");
        sql.append("    	item.material_fee,                                                               ");
        sql.append("    	needsMaterial.id,                                                                ");
        sql.append("    	item.food_fee,                                                                   ");
        sql.append("    	needFood.id,                                                                     ");
        sql.append("    	ifnull(item.enroll_fee, 0),                                                      ");
        sql.append("    IF ( " + isRoomFee + ", ifnull(item.room_fee, 0), 0 ) +                              ");
        sql.append("    IF ( " + isMaterialFee + ", ifnull(item.material_fee, 0), 0 ) +                      ");
        sql.append("    IF ( " + isFoodFee + ", ifnull(item.food_fee, 0), 0 ) +                              ");
        sql.append("    ifnull(item.enroll_fee, 0)                                                           ");
        sql.append("    FROM                                                                                 ");
        sql.append("    	pe_class class                                                                   ");
        sql.append("    INNER JOIN training_item item on item.id = class.fk_training_item_id                 ");
        sql.append("    INNER JOIN enum_const needFood ON needFood.namespace = 'FlagNeedFood'                ");
        sql.append("    AND needFood. CODE = ?                                                               ");
        sql.append("    INNER JOIN enum_const needRoom ON needRoom.namespace = 'FlagNeedRoom'                ");
        sql.append("    AND needRoom. CODE = ?                                                               ");
        sql.append("    INNER JOIN enum_const needsMaterial ON needsMaterial.namespace = 'FlagNeedMaterial'  ");
        sql.append("    AND needsMaterial. CODE = ?                                                          ");
        sql.append("    WHERE                                                                                ");
        sql.append("    	class.id = '" + enrollStudentInfParameter.getClassId() + "'                      ");
        sql.append("    AND class.site_code = '" + SiteUtil.getSiteCode() + "'                               ");
        sql.append("    AND NOT EXISTS (                                                                     ");
        sql.append("    	SELECT                                                                           ");
        sql.append("    		1                                                                            ");
        sql.append("    	FROM                                                                             ");
        sql.append("    		pe_student ps                                                                ");
        sql.append("    	INNER JOIN student_enroll_fee fee ON fee.fk_student_id = ps.id                   ");
        sql.append("    	WHERE                                                                            ");
        sql.append("    		ps.id = '" + stuId + "'                                                      ");
        sql.append("    )                                                                                    ");
        myGeneralDao.executeBySQL(sql.toString(),
                isRoomFee ? "1" : "0", isFoodFee ? "1" : "0", isMaterialFee ? "1" : "0");
    }

    /**
     * 取消报名
     * @param ids
     */
    @LogAndNotice("取消报名")
    public void doCancelEnroll(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                            ");
        sql.append("    	1                                                             ");
        sql.append("    FROM                                                              ");
        sql.append("    	pe_student stu                                                ");
        sql.append("    WHERE                                                             ");
        sql.append("                      " + CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append("    AND stu.site_code = '" + SiteUtil.getSiteCode() + "'              ");
        sql.append("    AND fk_business_order_id IS NOT NULL                              ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("学生已经添加到订单信息中了，无法取消");
        }
        // 去除学生引用
        sql.delete(0, sql.length());
        sql.append("    DELETE                                                     ");
        sql.append("    FROM                                                       ");
        sql.append("    	student_enroll_fee                                     ");
        sql.append("    WHERE                                                      ");
        sql.append("         " + CommonUtils.madeSqlIn(ids, "fk_student_id"));
        myGeneralDao.executeBySQL(sql.toString());
        sql.delete(0, sql.length());
        sql.append("    DELETE                                                    ");
        sql.append("    FROM                                                      ");
        sql.append("    	pe_student                                            ");
        sql.append("    WHERE                                                     ");
        sql.append("    	         " +  CommonUtils.madeSqlIn(ids, "id"));
        myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 批量导入模板
     *
     * @param outputStream
     */
    public void downloadEnrollTemplate(OutputStream outputStream) {
        Map<String, List<String>> selectMap = new HashMap<>(5);
        selectMap.put("性别*", Arrays.asList("男", "女"));
        selectMap.put("是否需要培训用书", Arrays.asList("是", "否"));
        selectMap.put("是否需要单间住宿", Arrays.asList("是", "否"));
        selectMap.put("是否需要餐券", Arrays.asList("是", "否"));
        this.utilService.generateExcelAndWrite(outputStream, selectMap,
                BusinessUnitStudentEnrollConstant.UPLOAD_EXCEL_STUDENT_ENROLL);
    }

    /**
     * 批量导入报名信息
     * @param file
     * @return
     */
    public Map<String,Object> doUploadStudentEnrollInfo(File file) {
        Map<String, Object> resultMap = new HashMap<>(16);
        String namespace = UUID.randomUUID().toString().replace("-", "");
        Workbook book = null;
        try {
            Map<String, Object> result = this.excelUploadService.doLoadFileDataToTable(file,
                    BusinessUnitStudentEnrollConstant.UPLOAD_EXCEL_STUDENT_ENROLL, 0, namespace);
            book = (Workbook) result.get("book");
            StringBuilder sql = new StringBuilder();
            sql.append("    EXISTS (                                                                            ");
            sql.append("    	SELECT                                                                          ");
            sql.append("    		1                                                                           ");
            sql.append("    	FROM                                                                            ");
            sql.append("    		(                                                                           ");
            sql.append("    			SELECT                                                                  ");
            sql.append("    				t.id,                                                               ");
            sql.append("    				t.column2,                                                          ");
            sql.append("    				t.column3                                                           ");
            sql.append("    			FROM                                                                    ");
            sql.append("    				util_excel_table t                                                  ");
            sql.append("    			WHERE                                                                   ");
            sql.append("    				t.namespace = '" + namespace + "'                                   ");
            sql.append("    		) a                                                                         ");
            sql.append("    	WHERE                                                                           ");
            sql.append("    		a.column2 = e.COLUMN2                                                       ");
            sql.append("    	AND a.column3 = e.column3                                                       ");
            sql.append("    	AND a.id <> e.id                                                                ");
            sql.append("    )                                                                                   ");
            List<String[]> checkList = new ArrayList<>();
            checkList.add(new String[]{"学生一个期次下只能报名一次;", sql.toString()});
            sql.delete(0, sql.length());
            sql.append("NOT (e.COLUMN3 REGEXP '" + ValidateUtils.PERSON_CARD_NO_REG_STR + "')");
            checkList.add(new String[]{"身份证号格式不正确;", sql.toString()});

            sql.delete(0, sql.length());
            sql.append("NOT (e.COLUMN5 REGEXP '" + ValidateUtils.MOBILE_REGEX + "')");
            checkList.add(new String[]{"手机号格式不正确;", sql.toString()});

            sql.delete(0, sql.length());
            sql.append("    NOT EXISTS (                                                                        ");
            sql.append("    	SELECT                                                                          ");
            sql.append("    		1                                                                           ");
            sql.append("    	FROM                                                                            ");
            sql.append("    		pe_class class                                                              ");
            sql.append("    	WHERE                                                                           ");
            sql.append("    		class.site_code = '" + SiteUtil.getSiteCode() + "'                          ");
            sql.append("    	AND class. NAME = e.COLUMN2                                                     ");
            sql.append("    )                                                                                   ");
            checkList.add(new String[]{"期次名称不存在;", sql.toString()});

            sql.delete(0, sql.length());
            sql.append("    EXISTS (                                                                            ");
            sql.append("    	SELECT                                                                          ");
            sql.append("    		1                                                                           ");
            sql.append("    	FROM                                                                            ");
            sql.append("    		pe_class class                                                              ");
            sql.append("    	WHERE                                                                           ");
            sql.append("    		class. site_code = '" + SiteUtil.getSiteCode() + "'                         ");
            sql.append("    	AND class. NAME = e.COLUMN2                                                     ");
            sql.append("    	AND class.start_time < now()                                                    ");
            sql.append("    )                                                                                   ");
            checkList.add(new String[]{"报名时间已经截止;", sql.toString()});

            sql.delete(0, sql.length());
            sql.append("    EXISTS (                                                                            ");
            sql.append("    	SELECT                                                                          ");
            sql.append("    		1                                                                           ");
            sql.append("    	FROM                                                                            ");
            sql.append("    		pe_student stu                                                              ");
            sql.append("    	INNER JOIN pe_class class ON class.id = stu.fk_class_id                         ");
            sql.append("    	WHERE                                                                           ");
            sql.append("    		stu.site_code = '" + SiteUtil.getSiteCode() + "'                            ");
            sql.append("    	AND stu.card_no = e.COLUMN3                                                     ");
            sql.append("    	AND class. NAME = e.COLUMN2                                                     ");
            sql.append("    )                                                                                   ");
            checkList.add(new String[]{"学生已经报考过本期次;", sql.toString()});

            sql.delete(0, sql.length());
            sql.append("    EXISTS (                                                                           ");
            sql.append("    	SELECT                                                                         ");
            sql.append("    		1                                                                          ");
            sql.append("    	FROM                                                                           ");
            sql.append("    		(                                                                          ");
            sql.append("    			SELECT                                                                 ");
            sql.append("    				u.COLUMN11,                                                        ");
            sql.append("    				u.COLUMN12,                                                        ");
            sql.append("    				u.COLUMN13                                                         ");
            sql.append("    			FROM                                                                   ");
            sql.append("    				util_excel_table u                                                 ");
            sql.append("    			WHERE                                                                  ");
            sql.append("    				u.id = '" + namespace + "'                                         ");
            sql.append("    		) aa                                                                       ");
            sql.append("    	WHERE                                                                          ");
            sql.append("    		aa.COLUMN11 NOT IN ('是', '否', '')                                        ");
            sql.append("    	OR aa.COLUMN12 NOT IN ('是', '否', '')                                         ");
            sql.append("    	OR aa.COLUMN13 NOT IN ('是', '否', '')                                         ");
            sql.append("    )                                                                                 ");
            checkList.add(new String[]{"是否需要培训用书, 是否需要餐券, 是否需要单间住宿只能填写是、否或者不填;", sql.toString()});

            sql.delete(0, sql.length());
            sql.append("    EXISTS (                                                                                 ");
            sql.append("    	SELECT                                                                               ");
            sql.append("    		1                                                                                ");
            sql.append("    	FROM                                                                                 ");
            sql.append("    		(                                                                                ");
            sql.append("    			SELECT                                                                       ");
            sql.append("    				%s                                                                       ");
            sql.append("    			FROM                                                                         ");
            sql.append("    				util_excel_table u                                                       ");
            sql.append("    			INNER JOIN pe_class class ON class. NAME = u.COLUMN2                         ");
            sql.append("    			INNER JOIN training_item item ON item.id = class.fk_training_item_id         ");
            sql.append("    			WHERE                                                                        ");
            sql.append("    				u.namespace = '" + namespace + "'                                        ");
            sql.append("    		) aa                                                                             ");
            sql.append("    	WHERE                                                                                ");
            sql.append("    		ifnull(%s, 0) <= 0                                                               ");
            sql.append("    	AND e.COLUMN11 = '是'                                                                ");
            sql.append("    )                                                                                        ");
            checkList.add(new String[]{"此期次没有培训用书费，请查证excel中对应栏目是否为否或者不填写;",
                    String.format(sql.toString(), "item.material_fee material_fee", "aa.material_fee")});

            checkList.add(new String[]{"此期次没有餐券费，请查证excel中对应栏目是否为否或者不填写;",
                    String.format(sql.toString(), "item.food_fee food_fee", "aa.food_fee")});

            checkList.add(new String[]{"此期次没有住宿费，请查证excel中对应栏目是否为否或者不填写;",
                    String.format(sql.toString(), "item.room_fee room_fee", "aa.room_fee")});

            sql.delete(0, sql.length());
            sql.append("    UPDATE util_excel_table e                                                           ");
            sql.append("    INNER JOIN (                                                                        ");
            sql.append("    	SELECT                                                                          ");
            sql.append("    		class. NAME className,                                                      ");
            sql.append("    		COUNT(u.ID) uetNum,                                                         ");
            sql.append("    		class.training_person_number totalNum,                                      ");
            sql.append("    		aa.stuNum existStuNum                                                       ");
            sql.append("    	FROM                                                                            ");
            sql.append("    		util_excel_table u                                                          ");
            sql.append("    	INNER JOIN pe_class class ON class. NAME = u.COLUMN2                            ");
            sql.append("    	LEFT JOIN (                                                                     ");
            sql.append("    		SELECT                                                                      ");
            sql.append("    			stu.fk_class_id classId,                                                ");
            sql.append("    			count(stu.id) stuNum                                                    ");
            sql.append("    		FROM                                                                        ");
            sql.append("    			pe_student stu                                                          ");
            sql.append("    		WHERE                                                                       ");
            sql.append("    			stu.site_code = '" + SiteUtil.getSiteCode() + "'                        ");
            sql.append("    		AND stu.fk_class_id IS NOT NULL                                             ");
            sql.append("    		GROUP BY                                                                    ");
            sql.append("    			stu.fk_class_id                                                         ");
            sql.append("    	) aa ON aa.classId = class.id                                                   ");
            sql.append("    	WHERE                                                                           ");
            sql.append("    		u.namespace = '" + namespace + "'                                           ");
            sql.append("    	GROUP BY                                                                        ");
            sql.append("    		u.column2                                                                   ");
            sql.append("    	HAVING                                                                          ");
            sql.append("    		IFNULL(                                                                     ");
            sql.append("    			class.training_person_number,                                           ");
            sql.append("    			0                                                                       ");
            sql.append("    		) - IFNULL(aa.stuNum, 0) < COUNT(u.ID)                                      ");
            sql.append("    ) uet ON uet.className = e.column2                                                  ");
            sql.append("    SET e.valid = '2',                                                                  ");
            sql.append("     e.error_info = concat(                                                             ");
            sql.append("    	ifnull(e.error_info, ''),                                                       ");
            sql.append("    	'本期次剩余',                                                                    ");
            sql.append("    	uet.totalNum - uet.existStuNum,                                                 ");
            sql.append("    	'报名额，小于excel本期次的报考人数',                                               ");
            sql.append("    	uet.uetNum,                                                                     ");
            sql.append("    	'个'                                                                            ");
            sql.append("    )                                                                                   ");
            sql.append("    WHERE                                                                               ");
            sql.append("    	e.namespace = '" + namespace + "'                                               ");
            // 执行校验
            this.excelUploadService.checkExcelTable(namespace, checkList, BusinessUnitStudentEnrollConstant.UPLOAD_EXCEL_STUDENT_ENROLL);
            this.myGeneralDao.executeBySQL(sql.toString());

            sql.delete(0, sql.length());
            sql.append("    INSERT INTO pe_student (                                                          ");
            sql.append("    	id,                                                                           ");
            sql.append("    	true_name,                                                                    ");
            sql.append("    	card_no,                                                                      ");
            sql.append("    	email,                                                                        ");
            sql.append("    	flag_student_status,                                                          ");
            sql.append("    	mobile,                                                                       ");
            sql.append("    	create_by,                                                                    ");
            sql.append("    	create_date,                                                                  ");
            sql.append("    	site_code,                                                                    ");
            sql.append("    	flag_gender,                                                                  ");
            sql.append("    	work_unit,                                                                    ");
            sql.append("    	fk_business_unit_id,                                                          ");
            sql.append("    	address,                                                                      ");
            sql.append("    	positional_title,                                                             ");
            sql.append("    	professional_title,                                                           ");
            sql.append("    	fk_training_item_id,                                                          ");
            sql.append("    	fk_class_id,                                                                  ");
            sql.append("    	flag_fee_status,                                                              ");
            sql.append("    	flag_first_enroll_status                                                      ");
            sql.append("    )                                                                                 ");
            sql.append("    SELECT                                                                            ");
            sql.append("    	REPLACE (UUID(), '-', ''),                                                    ");
            sql.append("    	u.column1,                                                                    ");
            sql.append("    	u.column3,                                                                    ");
            sql.append("    	u.column6,                                                                    ");
            sql.append("    	stustatus.id,                                                                 ");
            sql.append("    	u.column5,                                                                    ");
            sql.append("    	'" + UserUtils.getCurrentUserId() + "',                                       ");
            sql.append("    	now(),                                                                        ");
            sql.append("    	'" + SiteUtil.getSiteCode() + "',                                             ");
            sql.append("    	gender.id,                                                                    ");
            sql.append("    	u.column10,                                                                   ");
            sql.append("    	unit.id,                                                                      ");
            sql.append("    	u.column7,                                                                    ");
            sql.append("    	u.column8,                                                                    ");
            sql.append("    	u.column9,                                                                    ");
            sql.append("    	class.fk_training_item_id,                                                    ");
            sql.append("    	class.id,                                                                     ");
            sql.append("    	feestatus.id,                                                                 ");
            sql.append("    IF (                                                                              ");
            sql.append("    	ps.Id IS NULL,                                                                ");
            sql.append("    	firstEnrollstatus1.id,                                                        ");
            sql.append("    	firstEnrollstatus.id                                                          ");
            sql.append("    )                                                                                 ");
            sql.append("    FROM                                                                              ");
            sql.append("    	util_excel_table u                                                            ");
            sql.append("    INNER JOIN pe_class class ON class. NAME = u.COLUMN2                              ");
            sql.append("    INNER JOIN enum_const gender ON gender. NAME = u.column4                          ");
            sql.append("    INNER JOIN enum_const stustatus ON stustatus.namespace = 'FlagStudentStatus'      ");
            sql.append("    AND stustatus. CODE = '1'                                                         ");
            sql.append("    INNER JOIN pe_business_unit unit ON unit.fk_sso_user_id = '" +
                    UserUtils.getCurrentUserId() + "' ");
            sql.append("    INNER JOIN enum_const feestatus ON feestatus.NAMESPACE = 'flagfeestatus'          ");
            sql.append("    AND feestatus. CODE = '0'                                                         ");
            sql.append("    INNER JOIN enum_const firstEnrollstatus ON  firstEnrollstatus. CODE = '0'         ");
            sql.append("    AND firstEnrollstatus.namespace = 'flagFirstEnrollStatus'                         ");
            sql.append("    INNER JOIN enum_const firstEnrollstatus1 ON  firstEnrollstatus1. CODE = '1'       ");
            sql.append("    AND firstEnrollstatus1.namespace = 'flagFirstEnrollStatus'                        ");
            sql.append("    LEFT JOIN pe_student ps ON ps.card_no = u.COLUMN3                                 ");
            sql.append("    AND ps.site_code = '" + SiteUtil.getSiteCode() + "'                               ");
            sql.append("    AND ps.fk_class_id IS NOT NULL                                                    ");
            sql.append("    WHERE                                                                             ");
            sql.append("    	u.namespace = '" + namespace + "'                                             ");
            sql.append("    AND class.site_code = '" + SiteUtil.getSiteCode() + "'                            ");
            sql.append("    AND u.valid = '1'                                                                 ");
            sql.append("    AND NOT EXISTS (                                                                  ");
            sql.append("    	SELECT                                                                        ");
            sql.append("    		1                                                                         ");
            sql.append("    	FROM                                                                          ");
            sql.append("    		pe_student ps                                                             ");
            sql.append("    	WHERE                                                                         ");
            sql.append("    		ps.card_no = u.column3                                                    ");
            sql.append("    	AND ps.site_code = '" + SiteUtil.getSiteCode() + "'                           ");
            sql.append("    	AND ps.fk_class_id = class.id                                                 ");
            sql.append("    )                                                                                 ");
            sql.append("    GROUP BY u.id                                                                     ");
            int count = this.myGeneralDao.executeBySQL(sql.toString());

            sql.delete(0, sql.length());
            sql.append("    INSERT INTO student_enroll_fee (                                                 ");
            sql.append("    	fk_student_id,                                                               ");
            sql.append("    	room_fee,                                                                    ");
            sql.append("    	flag_need_room,                                                              ");
            sql.append("    	material_fee,                                                                ");
            sql.append("    	flag_need_materia,                                                           ");
            sql.append("    	food_fee,                                                                    ");
            sql.append("    	flag_need_food,                                                              ");
            sql.append("    	train_fee,                                                                   ");
            sql.append("    	total_fee                                                                    ");
            sql.append("    ) SELECT                                                                         ");
            sql.append("    	ps.id,                                                                       ");
            sql.append("    	ifnull(item.room_fee, 0),                                                    ");
            sql.append("    	needRoom.ID,                                                                 ");
            sql.append("    	ifnull(item.material_fee, 0),                                                ");
            sql.append("    	needsMaterial.id,                                                            ");
            sql.append("    	ifnull(item.food_fee, 0),                                                    ");
            sql.append("    	needFood.id,                                                                 ");
            sql.append("    	item.enroll_fee,                                                             ");
            sql.append("    IF (                                                                             ");
            sql.append("    	needsMaterial. CODE = '1',                                                   ");
            sql.append("    	ifnull(item.material_fee, 0),                                                ");
            sql.append("    	0                                                                            ");
            sql.append("    ) +                                                                              ");
            sql.append("    IF (                                                                             ");
            sql.append("    	needRoom. CODE = '1',                                                        ");
            sql.append("    	ifnull(item.room_fee, 0),                                                    ");
            sql.append("    	0                                                                            ");
            sql.append("    ) +                                                                              ");
            sql.append("    IF (                                                                             ");
            sql.append("    	needFood. CODE = '1',                                                        ");
            sql.append("    	ifnull(item.food_fee, 0),                                                    ");
            sql.append("    	0                                                                            ");
            sql.append("    ) + item.enroll_fee                                                              ");
            sql.append("    FROM                                                                             ");
            sql.append("    	util_excel_table u                                                           ");
            sql.append("    INNER JOIN pe_class class ON class. NAME = u.COLUMN2                             ");
            sql.append("    INNER JOIN training_item item ON item.id = class.fk_training_item_id             ");
            sql.append("    INNER JOIN pe_student ps ON ps.fk_class_id = class.id                            ");
            sql.append("    AND ps.card_no = u.COLUMN3                                                       ");
            sql.append("    INNER JOIN enum_const needsMaterial ON needsMaterial.namespace = 'FlagNeedMaterial' ");
            sql.append("    AND needsMaterial. NAME = ifnull(u.column11, '否')                               ");
            sql.append("    INNER JOIN enum_const needRoom ON needRoom.namespace = 'FlagNeedRoom'            ");
            sql.append("    AND needRoom. NAME = ifnull(u.COLUMN13, '否')                                    ");
            sql.append("    INNER JOIN enum_const needFood ON needFood.namespace = 'FlagNeedFood'            ");
            sql.append("    AND needFood. NAME = ifnull(u.column12, '否')                                    ");
            sql.append("    WHERE                                                                            ");
            sql.append("    	u.namespace = '" + namespace + "'                                            ");
            sql.append("    AND class.site_code = '" + SiteUtil.getSiteCode() + "'                           ");
            sql.append("    AND u.valid = '1'                                                                ");
            myGeneralDao.executeBySQL(sql.toString());
            int totalNum = (Integer) result.get("count");
            resultMap.put("msg", "批量导入学生报名信息：成功导入" + count + "条, 失败" + (totalNum - count) + "条");
            if (this.excelUploadService.getExcelErrorNum(namespace) > 0) {
                String webPath = ExcelUtils.getDefaultErrorInfoFilePath();
                String realPath = CommonUtils.getRealPath(webPath);
                this.excelUploadService.writeTemporaryErrorInfoToFile(realPath, file, namespace, 0);
                resultMap.put("url", webPath);
            }
            return resultMap;
        } catch (StreamOperateException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                this.excelUploadService.closeBook(book);
            } catch (StreamOperateException e) {
                throw new RuntimeException(e);
            }
            //删除临时表
            this.myGeneralDao.executeBySQL("delete from util_excel_table where namespace = '" + namespace + "'");
        }
    }

    /**
     * 获取期次费用信息
     * @param classId
     * @return
     */
    public Map<String, Object> getClassFeeInfo(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                        ");
        sql.append("    	IFNULL(item.food_fee, 0) foodFee,                                         ");
        sql.append("    	IFNULL(item.material_fee, 0) materialFee,                                 ");
        sql.append("    	IFNULL(item.room_fee, 0) roomFee                                          ");
        sql.append("    FROM                                                                          ");
        sql.append("    	pe_class class                                                            ");
        sql.append("    INNER JOIN training_item item ON item.id = class.fk_training_item_id          ");
        sql.append("    WHERE                                                                         ");
        sql.append("    	class.id = ?                                                              ");
        return myGeneralDao.getOneMapBySQL(sql.toString(), classId);
    }
}
