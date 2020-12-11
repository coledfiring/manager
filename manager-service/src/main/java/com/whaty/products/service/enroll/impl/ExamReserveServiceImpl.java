package com.whaty.products.service.enroll.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.ExamReserve;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.common.UtilService;
import com.whaty.products.service.enroll.constant.StudentEnrollConstant;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 考点管理service
 *
 * @author shanshuai
 */
@Lazy
@Service("examReserveService")
public class ExamReserveServiceImpl extends TycjGridServiceAdapter<ExamReserve> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Resource(name = CommonConstant.UTIL_SERVICE_BEAN_NAME)
    private UtilService utilService;

    /**
     * 审核考生报名通过
     * @param ids
     * @return
     */
    public int doCheckEnrollPass(String ids) {
        return operateCheckEnroll(ids, "2");
    }

    /**
     * 审核考生报名不通过
     * @param ids
     * @return
     */
    public int doCheckEnrollNoPass(String ids) {
        return operateCheckEnroll(ids, "3");
    }

    /**
     * 审核考生报名操作
     * @param ids
     * @param checkStatusCode
     * @return
     */
    private int operateCheckEnroll(String ids, String checkStatusCode) {
        TycjParameterAssert.isAllNotBlank(ids, checkStatusCode);
        StringBuilder sql = new StringBuilder();
        sql.append("    UPDATE exam_reserve er                                                              ");
        sql.append("    INNER JOIN enum_const enrollStatus ON enrollStatus.namespace = 'flagenrollstatus'   ");
        sql.append("    AND enrollStatus. CODE = ?                                                          ");
        sql.append("    SET er.flag_enroll_status = enrollStatus.id,                                         ");
        sql.append("        er.check_date = now(),                                                          ");
        sql.append("        er.check_user_id = ?                                                            ");
        sql.append("    WHERE  " + CommonUtils.madeSqlIn(ids, "er.id"));
        return myGeneralDao.executeBySQL(sql.toString(), checkStatusCode, userService.getCurrentUser().getId());
    }

    /**
     * 考场信息导入模版下载
     * @param out
     */
    public void downloadExamInfoNoTemplate(OutputStream out) {
        this.utilService.generateExcelAndWrite(out, null, StudentEnrollConstant.UPLOAD_EXCEL_EXAM_INFO);
    }

    /**
     * 考场信息导入
     * @param file
     * @return
     */
    public Map<String,Object> doUploadExamInfo(File file) {
        List<String[]> checkList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" EXISTS (                                                                      ");
        sql.append(" 	SELECT                                                                     ");
        sql.append(" 		1                                                                      ");
        sql.append(" 	FROM                                                                       ");
        sql.append(" 		(                                                                      ");
        sql.append(" 			SELECT                                                             ");
        sql.append(" 				t.id,                                                          ");
        sql.append(" 				t.column1                                                      ");
        sql.append(" 			FROM                                                               ");
        sql.append(" 				util_excel_table t                                             ");
        sql.append(" 			WHERE                                                              ");
        sql.append(" 				t.namespace = ${namespace}       					           ");
        sql.append(" 		) a                                                                    ");
        sql.append(" 	WHERE                                                                      ");
        sql.append("        a.column1 = e.column1                                                  ");
        sql.append(" 	AND a.id <> e.id                                                           ");
        sql.append(" )                                                                             ");
        checkList.add(new String[]{"文件中存在相同的身份证号;", sql.toString()});
        checkList.add(new String[]{"身份证号格式错误;", "NOT (e.COLUMN1 REGEXP '" +
                ValidateUtils.PERSON_CARD_NO_REG_STR + "')"});

        sql.delete(0, sql.length());
        sql.append("    UPDATE util_excel_table uet                                           ");
        sql.append("    INNER JOIN pe_student stu ON stu.card_no = uet.COLUMN1                ");
        sql.append("    INNER JOIN enum_const active ON active.namespace = 'flagActive'       ");
        sql.append("    AND active. CODE = '1'                                                ");
        sql.append("    INNER JOIN enum_const canPrint ON canPrint.namespace = 'flagCanPrint' ");
        sql.append("    AND canPrint. CODE = '1'                                              ");
        sql.append("    INNER JOIN exam_batch bat ON bat.flag_active = active.ID              ");
        sql.append("    INNER JOIN exam_reserve er ON er.fk_student_id = stu.id               ");
        sql.append("    AND er.fk_exam_batch_id = bat.id                                      ");
        sql.append("    SET er.exam_place = uet.column2,                                      ");
        sql.append("     er.exam_date = uet.column3,                                          ");
        sql.append("     er.exam_seat_number = uet.column4,                                   ");
        sql.append("     er.note = ifnull(uet.column5, NULL),                                 ");
        sql.append("     er.flag_can_print = canPrint.id                                      ");
        sql.append("    WHERE                                                                 ");
        sql.append("    	uet.valid = '1'                                                   ");
        sql.append("    AND uet.namespace = ${namespace}                                      ");
        sql.append("    AND stu.site_code = '" + SiteUtil.getSiteCode() + "'                  ");
        return this.utilService.doUploadFile(file, StudentEnrollConstant.UPLOAD_EXCEL_EXAM_INFO,
                checkList, Arrays.asList(sql.toString()), 0, "考场信息");
    }

    /**
     * 一键生成准考证号
     *
     * 生成规则：批次编号前两位+考点编码+科目+3位流水号
     * @return
     */
    public int doGenerateExamCardNo() {
        StringBuilder sql = new StringBuilder();
        sql.append("    UPDATE exam_reserve reserve                                                         ");
        sql.append("    INNER JOIN (                                                                        ");
        sql.append("    	SELECT                                                                          ");
        sql.append("    		er.id id,                                                                   ");
        sql.append("    		CONCAT(                                                                     ");
        sql.append("    			LEFT (bat. CODE, 2),                                                    ");
        sql.append("    			LEFT (site. CODE, 2),                                                   ");
        sql.append("    			LEFT (course. CODE, 1),                                                 ");
        sql.append("    			LPAD((@i/*'*/:=/*'*/@i + 1), 3, '0')                                    ");
        sql.append("    		) examCardNo                                                                ");
        sql.append("    	FROM                                                                            ");
        sql.append("    		exam_reserve er                                                             ");
        sql.append("    	INNER JOIN exam_batch bat ON er.fk_exam_batch_id = bat.id                       ");
        sql.append("    	INNER JOIN enum_const active ON active.id = bat.flag_active                     ");
        sql.append("    	INNER JOIN exam_course course ON er.fk_exam_course_id = course.id               ");
        sql.append("    	INNER JOIN exam_site site ON er.fk_exam_site_id = site.id                       ");
        sql.append("    	INNER JOIN pe_student stu ON er.fk_student_id = stu.id                          ");
        sql.append("    	INNER JOIN enum_const enrollStatus ON enrollStatus.id = er.flag_enroll_status   ");
        sql.append("    	INNER JOIN (                                                                    ");
        sql.append("    		SELECT                                                                      ");
        sql.append("    			@i/*'*/:=/*'*/IFNULL(                                                   ");
        sql.append("    				(                                                                   ");
        sql.append("    					SELECT                                                          ");
        sql.append("    						max(RIGHT(res.exam_card_no, 3)) maxExamCardNo               ");
        sql.append("    					FROM                                                            ");
        sql.append("    						exam_reserve res                                            ");
        sql.append("    					INNER JOIN exam_batch eb ON res.fk_exam_batch_id = eb.id        ");
        sql.append("    					INNER JOIN enum_const active ON active.id = eb.flag_active      ");
        sql.append("    					AND active. CODE = '1'                                          ");
        sql.append("    					WHERE                                                           ");
        sql.append("    						res.exam_card_no IS NOT NULL                                ");
        sql.append("    				),                                                                  ");
        sql.append("    				0                                                                   ");
        sql.append("    			)                                                                       ");
        sql.append("    	) a ON 1 = 1                                                                    ");
        sql.append("    	WHERE                                                                           ");
        sql.append("    		active. CODE = '1'                                                          ");
        sql.append("    	AND enrollStatus. CODE = '2'                                                    ");
        sql.append("    	AND er.exam_card_no IS NULL                                                     ");
        sql.append("    	ORDER BY                                                                        ");
        sql.append("    		site.id,                                                                    ");
        sql.append("    		course.id,                                                                  ");
        sql.append("    		stu.card_no                                                                 ");
        sql.append("    ) aa ON aa.id = reserve.id                                                          ");
        sql.append("    SET exam_card_no = aa.examCardNo                                                    ");
        return myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 删除学生报名信息
     * @param ids
     * @return
     */
    @LogAndNotice("删除学生报名信息")
    public int deleteExamStudentInfo(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                    ");
        sql.append("    	1                                                                     ");
        sql.append("    FROM                                                                      ");
        sql.append("    	exam_reserve er                                                       ");
        sql.append("    INNER JOIN enum_const ec ON ec.id = er.flag_enroll_status                 ");
        sql.append("    WHERE                                                                     ");
        sql.append("    	ec. CODE = '2' and " + CommonUtils.madeSqlIn(ids, "er.id"));
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("选中信息存在报名已经审核通过的数据，不能删除");
        }
        sql.delete(0, sql.length());
        sql.append("    DELETE er.*, stu.*, su.*                                                  ");
        sql.append("    FROM                                                                      ");
        sql.append("    	exam_reserve er                                                       ");
        sql.append("    INNER JOIN pe_student stu ON stu.id = er.fk_student_id                    ");
        sql.append("    INNER JOIN sso_user su ON su.id = stu.fk_sso_user_id                      ");
        sql.append("    WHERE  " + CommonUtils.madeSqlIn(ids, "er.id"));
        return myGeneralDao.executeBySQL(sql.toString()) / 3;
    }
}
