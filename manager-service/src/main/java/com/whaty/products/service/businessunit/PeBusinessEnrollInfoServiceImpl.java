package com.whaty.products.service.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.schedule.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * 报名信息查询service
 *
 * @author shanshuai
 */
@Lazy
@Service("peBusinessEnrollInfoService")
public class PeBusinessEnrollInfoServiceImpl extends TycjGridServiceAdapter<PeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

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
        sql.append("    GROUP BY                                                                           ");
        sql.append("    	class.id                                                                       ");
        sql.append("    HAVING                                                                             ");
        sql.append("    	class.training_person_number > COUNT(stu.id)                                   ");
        sql.append("    ORDER BY                                                                           ");
        sql.append("    	CONVERT (class. NAME USING gbk) ASC                                            ");
        return myGeneralDao.getBySQL(sql.toString(), SiteUtil.getSiteCode());
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
        return myGeneralDao.executeBySQL(sql.toString(), classId);
    }

    /**
     * 添加报名、调整批次前的校验
     * @param ids
     * @param classId
     */
    private void checkBeforeAddOrUpdateStudentClassInfo(String ids, String classId) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                         ");
        sql.append("    	1                                                                          ");
        sql.append("    FROM                                                                           ");
        sql.append("    	pe_student stu                                                             ");
        sql.append("    WHERE                                                                          ");
        sql.append("    	stu.site_code = ?                                                          ");
        sql.append("    AND  " + CommonUtils.madeSqlIn(ids, "stu.id"));
        sql.append("    AND EXISTS (                                                                   ");
        sql.append("    	SELECT                                                                     ");
        sql.append("    		1                                                                      ");
        sql.append("    	FROM                                                                       ");
        sql.append("    		pe_student student                                                     ");
        sql.append("    	WHERE                                                                      ");
        sql.append("    		student.card_no = stu.card_no                                          ");
        sql.append("    	AND student.id <> stu.id                                                   ");
        sql.append("    	AND student.site_code = ?                                                  ");
        sql.append("    	AND student.fk_class_id = ?                                                ");
        sql.append("    )                                                                              ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.
                getBySQL(sql.toString(), SiteUtil.getSiteCode(), SiteUtil.getSiteCode(), classId))) {
            throw new ServiceException("报名失败,有重复报名的学生，请核实再进行调整");
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
        Map<String, Object> canEnrollNumMap = myGeneralDao.getOneMapBySQL(sql.toString(), SiteUtil.getSiteCode(), classId);
        BigInteger canEnrollNum = (BigInteger) canEnrollNumMap.get("canEnrollNum");
        if (canEnrollNum.intValue() - ids.split(CommonConstant.SPLIT_ID_SIGN).length < 0) {
            throw new ServiceException("报名失败,本期次剩余" + canEnrollNum + "名报名余额，小于本次选择人数");
        }
    }

    /**
     * 已经缴费的学生才可以打印听课证
     * @param ids
     */
    public void checkIsCanPrintCertificateStudentInfo(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                 ");
        sql.append("    	1                                                                  ");
        sql.append("    FROM                                                                   ");
        sql.append("    	pe_student ps                                                      ");
        sql.append("    INNER JOIN enum_const ec ON ec.id = ps.flag_fee_status                 ");
        sql.append("    WHERE                                                                  ");
        sql.append("    	ec. CODE <> '1'                                                    ");
        sql.append("    AND     " + CommonUtils.madeSqlIn(ids, "ps.id"));
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只有已经缴费的学生才可以打印听课证，请确认");
        }
    }
}
