package com.whaty.products.service.classmaster.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.classmaster.ClassMasterWorkspaceService;
import com.whaty.products.service.classmaster.domain.ScheduleDay;
import com.whaty.products.service.classmaster.helper.ScheduleDayHelper;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 班主任工作台
 *
 * @author weipengsen
 */
@Lazy
@Service("classMasterWorkspaceService")
public class ClassMasterWorkspaceServiceImpl implements ClassMasterWorkspaceService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public List<ScheduleDay> listSchedules(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        return new ScheduleDayHelper(classId).listScheduleDay();
    }

    @Override
    public List<Map<String, Object>> listNotices(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                           ");
        sql.append(" 	b.id AS id,                                                   ");
        sql.append(" 	b.title AS title,                                             ");
        sql.append("    IF (pbu.id IS NULL, '0', '1') AS isRead,                      ");
        sql.append("    DATE_FORMAT(b.publish_date, '%Y-%m-%d') AS `sendDate`         ");
        sql.append(" FROM                                                             ");
        sql.append(" 	pe_bulletin b                                                 ");
        sql.append(" INNER JOIN enum_const esIsValid ON b.flag_isvalid = esIsValid.id ");
        sql.append(" INNER JOIN enum_const esIsTop ON b.flag_istop = esIsTop.id       ");
        sql.append(" LEFT JOIN pr_bulletin_user pbu on pbu.FK_BULLETIN_ID = b.id      ");
        sql.append(" WHERE                                                            ");
        sql.append(" 	esIsValid. CODE = '1'                                         ");
        sql.append(" AND b.site_code = ?                                              ");
        sql.append(" AND (                                                            ");
        sql.append(" 	(                                                             ");
        sql.append(" 		b.SCOPE_STRING LIKE '%student%'                           ");
        sql.append(" 		AND (                                                     ");
        sql.append(" 			b.scope_string NOT LIKE '%|class:%'                   ");
        sql.append(" 			OR b.scope_string LIKE '%" + classId + "%'            ");
        sql.append(" 		)                                                         ");
        sql.append(" 	)                                                             ");
        sql.append(" 	OR b.FK_SSO_USER_ID = ?                                       ");
        sql.append(" )                                                                ");
        sql.append(" GROUP BY                                                         ");
        sql.append(" 	b.id                                                          ");
        sql.append(" ORDER BY                                                         ");
        sql.append(" 	esIsTop.ID DESC,                                              ");
        sql.append(" 	b.publish_date DESC                                           ");
        sql.append(" LIMIT 0, 8                                                       ");
        return this.generalDao.getMapBySQL(sql.toString(), MasterSlaveRoutingDataSource.getDbType(),
                UserUtils.getCurrentUserId());
    }

    @Override
    public void updatePrepareItemStatus(String classId, String id, Boolean done) {
        TycjParameterAssert.isAllNotBlank(classId, id);
        TycjParameterAssert.isAllNotNull(done);
        String sql = "INSERT INTO teacher_prepare_item(fk_prepare_item_id, fk_manager_id, fk_class_id, " +
                "is_prepare, operate_time) SELECT pi.id, tea.id, ?, '" + (done ? "1" : "0") +
                "', now() FROM prepare_item pi " +
                "INNER JOIN pe_manager tea ON tea.fk_sso_user_id = ? WHERE pi.id = ?" +
                "ON DUPLICATE KEY UPDATE is_prepare = '" + (done ? "1" : "0") + "', operate_time = now() " +
                ", fk_manager_id = (SELECT id FROM pe_manager WHERE fk_sso_user_id = ? )";
        this.generalDao.executeBySQL(sql, classId, UserUtils.getCurrentUserId(), id, UserUtils.getCurrentUserId());
    }

    @Override
    public List<Map<String, Object>> listPrepareItems(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        return this.generalDao.getMapBySQL("SELECT pi.id AS id, pi.`name` AS `name`, ty.code as type," +
                "ifnull(tpi.is_prepare, '0') AS done FROM prepare_item pi " +
                "INNER JOIN enum_const ty on ty.id = pi.flag_prepare_type " +
                "INNER JOIN training_item ti on ti.flag_training_type = pi.flag_training_type " +
                "INNER JOIN pe_Class c on c.fk_training_item_id = ti.id " +
                "LEFT JOIN teacher_prepare_item tpi ON tpi.fk_class_id = c.id AND tpi.fk_prepare_item_id = pi.id " +
                "WHERE c.id = ? AND pi.site_code = ? ", classId, MasterSlaveRoutingDataSource.getDbType()).stream()
                .peek(e -> e.computeIfPresent("done", (k, o) -> "1".equals(o))).collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDay> listCourseTimeTable(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        return new ScheduleDayHelper(classId).listCourseTimeTable();
    }

    @Override
    public void deleteCourseTimeTable(String classId, String id) {
        TycjParameterAssert.isAllNotBlank(classId, id);
        if (!this.generalDao.checkNotEmpty("SELECT 1 FROM class_course_timetable cct " +
                "INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id " +
                "WHERE cct.id = ? AND cc.fk_class_id = ?", id, classId)) {
            throw new ServiceException("操作了不属于此班级的课程表");
        }
        this.generalDao.executeBySQL("delete from class_course_timetable where id = ?", id);
    }

    @Override
    public Map<String, Object> getCourseTimeTable(String classId, String courseTimeTableId) {
        TycjParameterAssert.isAllNotBlank(classId, courseTimeTableId);
        if (!this.generalDao.checkNotEmpty("SELECT 1 FROM class_course_timetable cct " +
                "INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id " +
                "WHERE cct.id = ? AND cc.fk_class_id = ?", courseTimeTableId, classId)) {
            throw new ServiceException("操作了不属于此班级的课程表");
        }
        Map<String, Object> courseTimeTable = this.generalDao
                .getOneMapBySQL("SELECT c.id AS course, c.name as courseName, cct.flag_course_type AS courseType, " +
                        "cct.teacher_fee AS teacherFee, date_format(cct.training_date, '%Y-%m-%d') AS trainingDate, " +
                        "cct.start_time AS startTime, cct.end_time AS endTime, cct.flag_place_type AS classPlaceType," +
                        " p. NAME AS classPlace, p. NAME AS classPlaceName, p.id AS classPlaceId " +
                        "FROM class_course_timetable cct INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id" +
                        " INNER JOIN pe_course c ON c.id = cc.fk_course_id " +
                        "INNER JOIN pe_place p ON p.id = cct.fk_place_id WHERE cct.id = ?", courseTimeTableId);
        String startTime = (String) courseTimeTable.remove("startTime");
        String endTime = (String) courseTimeTable.remove("endTime");
        courseTimeTable.put("timeRange", new String[] {startTime, endTime});
        return courseTimeTable;
    }

}
