package com.whaty.products.service.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Map;

/**
 * 单位报名信息管理service
 *
 * @author shanshuai
 */
@Lazy
@Service("enrollExistStudentService")
public class EnrollExistStudentServiceImpl extends TycjGridServiceAdapter<PeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 勾选学生报名
     * @param ids
     * @param classId
     * @return
     */
    @LogAndNotice("勾选学生报名")
    public int saveExistStudentEnrollInfo(String ids, String classId, String needRoomCode,
                                          String needFoodCode, String needMaterialCode) {
        TycjParameterAssert.isAllNotBlank(ids, classId);
        this.checkBeforeAddOrUpdateStudentClassInfo(ids, classId);
        StringBuffer sql = new StringBuffer();
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
        sql.append("    )                                                                                    ");
        sql.append("    SELECT                                                                               ");
        sql.append("    	REPLACE (UUID(), '-', ''),                                                       ");
        sql.append("    	stu.true_name,                                                                   ");
        sql.append("    	stu.card_no,                                                                     ");
        sql.append("    	stu.email,                                                                       ");
        sql.append("    	stustatus.ID,                                                                    ");
        sql.append("    	stu.mobile,                                                                      ");
        sql.append("    	'" + UserUtils.getCurrentUserId() + "',                                          ");
        sql.append("    	NOW(),                                                                           ");
        sql.append("    	'" + SiteUtil.getSiteCode() + "',                                                ");
        sql.append("    	stu.flag_gender,                                                                 ");
        sql.append("    	stu.work_unit,                                                                   ");
        sql.append("    	stu.fk_business_unit_id,                                                         ");
        sql.append("    	stu.address,                                                                     ");
        sql.append("    	stu.positional_title,                                                            ");
        sql.append("    	stu.professional_title,                                                          ");
        sql.append("    	item.id,                                                                         ");
        sql.append("    	'" + classId + "',                                                               ");
        sql.append("    	feestatus.id,                                                                    ");
        sql.append("    	ec0.id                                                                           ");
        sql.append("    FROM                                                                                 ");
        sql.append("    	pe_student stu                                                                   ");
        sql.append("    INNER JOIN enum_const stustatus ON stustatus.namespace = 'FlagStudentStatus'         ");
        sql.append("    AND stustatus. CODE = '1'                                                            ");
        sql.append("    INNER JOIN pe_class class ON class.id = '" + classId + "'                            ");
        sql.append("    INNER JOIN training_item item ON item.id = class.fk_training_item_id                 ");
        sql.append("    INNER JOIN enum_const ec0 ON ec0.NAMESPACE = 'flagFirstEnrollStatus'                 ");
        sql.append("    AND ec0. CODE = '0'                                                                  ");
        sql.append("    INNER JOIN enum_const feestatus ON feestatus.NAMESPACE = 'flagfeestatus'             ");
        sql.append("    AND feestatus. CODE = '0'                                                            ");
        sql.append("    LEFT JOIN pe_student ps ON ps.card_no = stu.card_no                                  ");
        sql.append("    WHERE                                                                                ");
        sql.append("      stu.site_code = '" + SiteUtil.getSiteCode());
        sql.append("'     AND " + CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append("      AND NOT EXISTS (                                                                   ");
        sql.append("      	SELECT                                                                           ");
        sql.append("      		1                                                                            ");
        sql.append("      	FROM                                                                             ");
        sql.append("      		pe_student ps                                                                ");
        sql.append("      	WHERE                                                                            ");
        sql.append("      		ps.card_no = stu.card_no                                                     ");
        sql.append("      	AND ps.id <> stu.id                                                              ");
        sql.append("      	AND ps.site_code = '" + SiteUtil.getSiteCode() + "'                              ");
        sql.append("      	AND ps.fk_class_id = '" + classId + "'                                           ");
        sql.append("      )                                                                                  ");
        sql.append("    AND NOT EXISTS (                                                                     ");
        sql.append("    	SELECT                                                                           ");
        sql.append("    		class.training_person_number                                                 ");
        sql.append("    	FROM                                                                             ");
        sql.append("    		pe_class class                                                               ");
        sql.append("    	LEFT JOIN pe_student stu ON stu.fk_class_id = class.id                           ");
        sql.append("    	WHERE                                                                            ");
        sql.append("    		class.site_code = '" + SiteUtil.getSiteCode() + "'                           ");
        sql.append("    	AND class.id = '" + classId + "'                                                 ");
        sql.append("    	GROUP BY                                                                         ");
        sql.append("    		class.id                                                                     ");
        sql.append("    	HAVING                                                                           ");
        sql.append("    		class.training_person_number - COUNT(stu.id) < " +
                ids.split(CommonConstant.SPLIT_ID_SIGN).length);
        sql.append("    )                                                                                    ");
        sql.append("    AND NOT EXISTS (                                                                     ");
        sql.append("    	SELECT                                                                           ");
        sql.append("    		1                                                                            ");
        sql.append("    	FROM                                                                             ");
        sql.append("    		pe_class class                                                               ");
        sql.append("    	WHERE                                                                            ");
        sql.append("    		class.site_code = '" + SiteUtil.getSiteCode() + "'                           ");
        sql.append("    	AND class.id = '" + classId + "'                                                 ");
        sql.append("    	AND now() > class.start_time                                                     ");
        sql.append("    )                                                                                    ");
        sql.append("    GROUP BY                                                                             ");
        sql.append("    	stu.id                                                                           ");
        int count = myGeneralDao.executeBySQL(sql.toString());

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
        sql.append("    ) SELECT                                                                             ");
        sql.append("    	ps.id,                                                                           ");
        sql.append("    	ifnull(item.room_fee, 0),                                                        ");
        sql.append("    	needRoom.ID,                                                                     ");
        sql.append("    	ifnull(item.material_fee, 0),                                                    ");
        sql.append("    	needMaterial.id,                                                                 ");
        sql.append("    	ifnull(item.food_fee, 0),                                                        ");
        sql.append("    	needFood.id,                                                                     ");
        sql.append("    	ifnull(item.enroll_fee, 0),                                                      ");
        sql.append("                                                                                         ");
        sql.append("    IF (                                                                                 ");
        sql.append("    	needMaterial. CODE = '1',                                                        ");
        sql.append("    	ifnull(item.material_fee, 0),                                                    ");
        sql.append("    	0                                                                                ");
        sql.append("    ) +                                                                                  ");
        sql.append("    IF (                                                                                 ");
        sql.append("    	needRoom. CODE = '1',                                                            ");
        sql.append("    	ifnull(item.room_fee, 0),                                                        ");
        sql.append("    	0                                                                                ");
        sql.append("    ) +                                                                                  ");
        sql.append("    IF (                                                                                 ");
        sql.append("    	needFood. CODE = '1',                                                            ");
        sql.append("    	ifnull(item.food_fee, 0),                                                        ");
        sql.append("    	0                                                                                ");
        sql.append("    ) + ifnull(item.enroll_fee, 0)                                                       ");
        sql.append("    FROM                                                                                 ");
        sql.append("    	pe_student ps                                                                    ");
        sql.append("    INNER JOIN pe_class class ON class.id = ?                                            ");
        sql.append("    INNER JOIN training_item item ON item.id = class.fk_training_item_id                 ");
        sql.append("    INNER JOIN enum_const needRoom ON needRoom.namespace = 'flagNeedRoom'                ");
        sql.append("    AND needRoom. CODE = ?                                                               ");
        sql.append("    INNER JOIN enum_const needFood ON needFood.namespace = 'flagNeedFood'                ");
        sql.append("    AND needFood. CODE = ?                                                               ");
        sql.append("    INNER JOIN enum_const needMaterial ON needMaterial.namespace = 'flagNeedMaterial'    ");
        sql.append("    AND needMaterial. CODE = ?                                                           ");
        sql.append("    INNER JOIN pe_student stu ON ps.card_no = stu.card_no                                ");
        sql.append("    LEFT JOIN student_enroll_fee fee ON fee.fk_student_id = ps.id                        ");
        sql.append("    WHERE " + CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append("    AND ps.fk_class_id = ?                                                               ");
        sql.append("    AND	ifnull(fee.id, '') = ''                                                          ");
        sql.append("    GROUP BY ps.id                                                                       ");
        myGeneralDao.executeBySQL(sql.toString(), classId, StringUtils.isBlank(needRoomCode) ? "0" : needRoomCode,
                StringUtils.isBlank(needFoodCode) ? "0" : needFoodCode,
                StringUtils.isBlank(needMaterialCode) ? "0" : needMaterialCode, classId);
        return count;
    }

    /**
     * 勾选调整期次
     * @param ids
     * @param classId
     * @return
     */
    @LogAndNotice("勾选调整期次")
    public int updateStudentClassInfo(String ids, String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL("SELECT 1 FROM pe_student WHERE fk_class_id = ? AND " +
                CommonUtils.madeSqlIn(ids, "id"), classId))) {
            throw new ServiceException("勾选信息存在所选择期次下的学生，请查证");
        }
        this.checkBeforeAddOrUpdateStudentClassInfo(ids, classId);
        StringBuilder sql = new StringBuilder();
        sql.append("    UPDATE pe_student stu                                                                    ");
        sql.append("    SET stu.fk_class_id = ?                                                                  ");
        sql.append("    WHERE                                                                                    ");
        sql.append("         " + CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append("    AND NOT EXISTS (                                                                         ");
        sql.append("    	SELECT                                                                               ");
        sql.append("    		1                                                                                ");
        sql.append("    	FROM                                                                                 ");
        sql.append("    		(                                                                                ");
        sql.append("    			SELECT                                                                       ");
        sql.append("    				ps.card_no cardNo,                                                       ");
        sql.append("    				ps.id id                                                                 ");
        sql.append("    			FROM                                                                         ");
        sql.append("    				pe_student ps                                                            ");
        sql.append("    			WHERE                                                                        ");
        sql.append("    				ps.site_code = '" + SiteUtil.getSiteCode() + "'                          ");
        sql.append("    			AND ps.fk_class_id = '" + classId + "'                                       ");
        sql.append("    		) aa                                                                             ");
        sql.append("    	WHERE                                                                                ");
        sql.append("    		aa.cardNo = stu.card_no                                                          ");
        sql.append("    	AND aa.id <> stu.id                                                                  ");
        sql.append("    )                                                                                        ");
        sql.append("    AND NOT EXISTS (                                                                         ");
        sql.append("    	SELECT                                                                               ");
        sql.append("    		1                                                                                ");
        sql.append("    	FROM                                                                                 ");
        sql.append("    		(                                                                                ");
        sql.append("    			SELECT                                                                       ");
        sql.append("    				class.training_person_number                                             ");
        sql.append("    			FROM                                                                         ");
        sql.append("    				pe_class class                                                           ");
        sql.append("    			LEFT JOIN pe_student stu2 ON stu2.fk_class_id = class.id                     ");
        sql.append("    			WHERE                                                                        ");
        sql.append("    				class.site_code = '" + SiteUtil.getSiteCode() + "'                       ");
        sql.append("    			AND class.id = '" + classId + "'                                             ");
        sql.append("    			GROUP BY                                                                     ");
        sql.append("    				class.id                                                                 ");
        sql.append("    			HAVING                                                                       ");
        sql.append("    				class.training_person_number < COUNT(stu2.id)                            ");
        sql.append("    		) bb                                                                             ");
        sql.append("    )                                                                                        ");
        sql.append("    AND NOT EXISTS (                                                                         ");
        sql.append("    	SELECT                                                                               ");
        sql.append("    		1                                                                                ");
        sql.append("    	FROM                                                                                 ");
        sql.append("    		pe_class class                                                                   ");
        sql.append("    	WHERE                                                                                ");
        sql.append("    		class.site_code = '" + SiteUtil.getSiteCode() + "'                               ");
        sql.append("    	AND class.id = '" + classId + "'                                                     ");
        sql.append("    	AND now() > class.start_time                                                         ");
        sql.append("    )                                                                                        ");

        return myGeneralDao.executeBySQL(sql.toString(), classId);
    }

    /**
     * 添加报名、调整批次前的校验
     * @param ids
     * @param classId
     */
    private void checkBeforeAddOrUpdateStudentClassInfo(String ids, String classId) {
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
        if (CollectionUtils.isNotEmpty(myGeneralDao.getMapBySQL(sql.toString(), SiteUtil.getSiteCode(), classId))) {
            throw new ServiceException("当前时间需要在期次开班截止时间之前,请确认");
        }
        // 已经报过该批次的不能报名
        sql.delete(0, sql.length());
        sql.append("    SELECT                                                                         ");
        sql.append("    	1                                                                          ");
        sql.append("    FROM                                                                           ");
        sql.append("    	pe_student stu                                                             ");
        sql.append("    WHERE                                                                          ");
        sql.append("    	stu.site_code = '" + SiteUtil.getSiteCode() + "'                           ");
        sql.append("    AND  " + CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append("    AND EXISTS (                                                                   ");
        sql.append("    	SELECT                                                                     ");
        sql.append("    		1                                                                      ");
        sql.append("    	FROM                                                                       ");
        sql.append("    		pe_student student                                                     ");
        sql.append("    	WHERE                                                                      ");
        sql.append("    		student.card_no = stu.card_no                                          ");
        sql.append("    	AND student.id <> stu.id                                                   ");
        sql.append("    	AND student.site_code = '" + SiteUtil.getSiteCode() + "'                   ");
        sql.append("    	AND student.fk_class_id = '" + classId + "'                                ");
        sql.append("    )                                                                              ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("报名失败：有重复报名的学生，请核实再进行调整");
        }

        // 期次人数报名剩余大于等于勾选的报名人数
        sql.delete(0, sql.length());
        sql.append("    SELECT                                                                       ");
        sql.append("    	class.training_person_number - COUNT(stu.id) canEnrollNum                ");
        sql.append("    FROM                                                                         ");
        sql.append("    	pe_class class                                                           ");
        sql.append("    LEFT JOIN pe_student stu ON stu.fk_class_id = class.id                       ");
        sql.append("    WHERE                                                                        ");
        sql.append("    	class.site_code = ?                                                      ");
        sql.append("    and class.id = ?                                                             ");
        Map<String, Object> canEnrollNumMap = myGeneralDao.
                getOneMapBySQL(sql.toString(), SiteUtil.getSiteCode(), classId);
        BigInteger canEnrollNum = (BigInteger) canEnrollNumMap.get("canEnrollNum");
        if (canEnrollNum.intValue() - ids.split(CommonConstant.SPLIT_ID_SIGN).length < 0) {
            throw new ServiceException("报名失败：本期次剩余" + canEnrollNum + "名报名余额，小于本次选择人数");
        }
    }

}
