package com.whaty.products.service.clazz.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.products.service.clazz.TrainingPlaceUsageService;
import com.whaty.products.service.clazz.domain.ClassPlaceVo;
import com.whaty.products.service.clazz.utils.ClazzUtils;
import com.whaty.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.whaty.products.service.clazz.constants.ClazzConstants.PLACE_ARRANGE_LOCK;

/**
 * 培训地点使用一览
 *
 * @author weipengsen
 */
@Lazy
@Service("trainingPlaceUsageService")
public class TrainingPlaceUsageServiceImpl implements TrainingPlaceUsageService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = "classTrainingPlaceManageService")
    private ClassTrainingPlaceManageServiceImpl classTrainingPlaceManageService;

    @Resource(name = "arrangeClassCourseTimetableManageService")
    private ArrangeClassCourseTimetableManageServiceImpl arrangeClassCourseTimetableManageService;

    @Override
    public Map<String, Object> listUsageInfo(String unit, String place, List<String> timeArea,
                                             String capacity, String feeLevel) {
        TycjParameterAssert.isAllNotNull(unit, timeArea);
        // 拿到选择的校区的所有地点
        String sql = "select p.id, p.name, capacity from pe_place p " +
                "INNER JOIN enum_const ec ON ec.id = p.flag_is_valid " +
                "where p.fk_place_unit = ? AND p.site_code = ? AND ec.id = '2' ";
        if (StringUtils.isNotBlank(place)) {
            sql += " and p.name like '%" + place + "%'";
        }
        if (StringUtils.isNotBlank(capacity)) {
            sql += " and p.capacity = " + capacity;
        }
        if (StringUtils.isNotBlank(feeLevel)) {
            sql += " and p.charges = " + feeLevel;
        }
        List<Map<String, Object>> places = this.myGeneralDao.getMapBySQL(sql, unit,
                MasterSlaveRoutingDataSource.getDbType());
        // 拿到所有的时间段
        sql = "select id, name, start_time as startTime, end_time as endTime from course_time where site_code = ? " +
                "order by start_time";
        List<Map<String, Object>> times = this.myGeneralDao.getMapBySQL(sql, MasterSlaveRoutingDataSource.getDbType());
        // 计算出开始时间到结束时间之内的所有日期
        List<Date> timeLimit = timeArea.stream().map(CommonUtils::convertUTCDate).sorted().collect(Collectors.toList());
        int difference = this.countDifferenceForDate(timeLimit.get(1), timeLimit.get(0));
        List<String> allDateInLimit = Stream.iterate(timeLimit.get(0), this::incrementDay)
                .limit(difference + 1).map(CommonUtils::changeDateToString)
                .sorted().collect(Collectors.toList());
        // 查询出校区教室的使用情况
        timeArea = timeArea.stream().map(e -> CommonUtils.changeDateToString(CommonUtils.convertUTCDate(e)))
                .collect(Collectors.toList());
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" SELECT                             ");
        sqlBuilder.append("     cast(cct.id as char) as id,    ");
        sqlBuilder.append("     date_format(cct.training_date, '%Y-%m-%d') as day,");
        sqlBuilder.append("     ct.id as timeId,               ");
        sqlBuilder.append("     ct.name as timeName,           ");
        sqlBuilder.append(" 	ct.start_time as startTime,    ");
        sqlBuilder.append(" 	ct.end_time as endTime,        ");
        sqlBuilder.append(" 	c.name as className,           ");
        sqlBuilder.append(" 	cct.color as color,            ");
        sqlBuilder.append(" 	un.name as trainingUnit,       ");
        sqlBuilder.append(" 	it.name as itemType,           ");
        sqlBuilder.append(" 	cou.name as courseName,        ");
        sqlBuilder.append(" 	tea.true_name as classMaster,  ");
        sqlBuilder.append(" 	count(stu.id) as studentNumber,");
        sqlBuilder.append(" 	pt.name as placeType,          ");
        sqlBuilder.append(" 	pt.code as placeTypeCode,      ");
        sqlBuilder.append(" 	p.id as placeId,               ");
        sqlBuilder.append("     'class' as arrangeType         ");
        sqlBuilder.append(" FROM                               ");
        sqlBuilder.append(" 	class_course_timetable cct     ");
        sqlBuilder.append(" INNER JOIN course_time ct on ct.id = cct.fk_course_time_id   ");
        sqlBuilder.append(" INNER JOIN pe_place p on p.id = cct.fk_place_id              ");
        sqlBuilder.append(" INNER JOIN class_course cc on cc.id = cct.fk_class_course_id ");
        sqlBuilder.append(" INNER JOIN pe_course cou on cou.id = cc.fk_course_id         ");
        sqlBuilder.append(" INNER JOIN pe_class c on c.id = cc.fk_class_id               ");
        sqlBuilder.append(" INNER JOIN enum_const pt on pt.id = c.flag_place_type        ");
        sqlBuilder.append(" LEFT JOIN pe_teacher tea on tea.id = c.fk_class_master_id    ");
        sqlBuilder.append(" INNER JOIN pe_unit un on un.id = c.fk_unit_id                ");
        sqlBuilder.append(" INNER JOIN training_item ti on ti.id = c.fk_training_item_id ");
        sqlBuilder.append(" INNER JOIN enum_const it on it.id = ti.flag_training_item_type");
        sqlBuilder.append(" LEFT JOIN pe_student stu on stu.fk_class_id = c.id           ");
        sqlBuilder.append(" WHERE                                                        ");
        sqlBuilder.append(" 	p.fk_place_unit = ?                                      ");
        if (StringUtils.isNotBlank(place)) {
            sqlBuilder.append(" AND p.name like '%" + place + "%'");
        }
        if (StringUtils.isNotBlank(capacity)) {
            sqlBuilder.append(" AND p.capacity = " + capacity);
        }
        if (StringUtils.isNotBlank(feeLevel)) {
            sqlBuilder.append(" and p.charges = " + feeLevel);
        }
        sqlBuilder.append(" AND date_format(cct.training_date, '%Y-%m-%d') between ? and ?");
        sqlBuilder.append(" GROUP BY cct.id                                                 ");
        List<Map<String, Object>> usageInfo = this.myGeneralDao.getMapBySQL(sqlBuilder.toString(), unit,
                timeArea.get(0), timeArea.get(1));
        sqlBuilder.delete(0, sqlBuilder.length());
        sqlBuilder.append(" SELECT                                                      ");
        sqlBuilder.append("     cast(tup.id as char) as id,                             ");
        sqlBuilder.append(" 	date_format(                                            ");
        sqlBuilder.append(" 		tup.use_date,                                       ");
        sqlBuilder.append(" 		'%Y-%m-%d'                                          ");
        sqlBuilder.append(" 	) AS `day`,                                             ");
        sqlBuilder.append(" 	ct.id AS timeId,                                        ");
        sqlBuilder.append(" 	ct. NAME AS timeName,                                   ");
        sqlBuilder.append(" 	tup.color AS color,                                     ");
        sqlBuilder.append(" 	p.id AS placeId,                                        ");
        sqlBuilder.append(" 	tup.application as application,                         ");
        sqlBuilder.append("     'temp' as arrangeType                                   ");
        sqlBuilder.append(" FROM                                                        ");
        sqlBuilder.append(" 	temporary_use_place tup                                 ");
        sqlBuilder.append(" INNER JOIN course_time ct ON ct.id = tup.fk_course_time_id  ");
        sqlBuilder.append(" INNER JOIN pe_place p ON p.id = tup.fk_place_id             ");
        sqlBuilder.append(" WHERE                                                       ");
        sqlBuilder.append(" 	p.fk_place_unit = ?                                     ");
        if (StringUtils.isNotBlank(place)) {
            sqlBuilder.append(" AND p.name like '%" + place + "%'");
        }
        if (StringUtils.isNotBlank(capacity)) {
            sqlBuilder.append(" AND p.capacity = " + capacity);
        }
        if (StringUtils.isNotBlank(feeLevel)) {
            sqlBuilder.append(" and p.charges = " + feeLevel);
        }
        sqlBuilder.append(" AND date_format(tup.use_date, '%Y-%m-%d') between ? and ?");
        List<Map<String, Object>> temporaryUse = this.myGeneralDao.getMapBySQL(sqlBuilder.toString(), unit,
                timeArea.get(0), timeArea.get(1));
        if (CollectionUtils.isNotEmpty(temporaryUse)) {
            usageInfo.addAll(temporaryUse);
        }
        Map<String, List<Map<String, Object>>> dayUsage = usageInfo.stream()
                .collect(Collectors.groupingBy(e -> (String) e.get("day")));
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("day", ClassPlaceVo.convertVO(allDateInLimit, dayUsage));
        resultMap.put("header", places);
        resultMap.put("times", times);
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> listClass(String type) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                           ");
        sql.append(" 	c.id AS id,                                                   ");
        sql.append(" 	c. NAME AS `name`,                                            ");
        sql.append(" 	it. NAME AS itemType,                                         ");
        sql.append(" 	concat(date_format(c. start_time, '%Y年%m月%d日'), ' - ',      ");
        sql.append("    date_format(c. end_time, '%Y年%m月%d日')) AS trainingTime,     ");
        sql.append(" 	tt. NAME AS trainingType                                      ");
        sql.append(" FROM                                                             ");
        sql.append(" 	pe_class c                                                    ");
        sql.append(" INNER JOIN training_item ti ON ti.id = c.fk_training_item_id     ");
        sql.append(" INNER JOIN enum_const it ON it.id = ti.flag_training_item_type   ");
        sql.append(" INNER JOIN enum_const tt ON tt.id = ti.flag_training_type        ");
        sql.append(" INNER JOIN enum_const pt on pt.id = c.flag_place_type            ");
        sql.append(" WHERE                                                            ");
        sql.append(" 	c.end_time > now()                                            ");
        sql.append(" AND pt.code = ?                                                  ");
        sql.append(" AND c.site_code = ?                                              ");
        sql.append(" AND EXISTS (                                                     ");
        sql.append(" 	SELECT                                                        ");
        sql.append(" 		1                                                         ");
        sql.append(" 	FROM                                                          ");
        sql.append(" 		class_course_timetable cct                                ");
        sql.append(" 	INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id  ");
        sql.append(" 	WHERE                                                         ");
        sql.append(" 		ifnull(cct.fk_place_id, '') = ''                          ");
        sql.append(" 	AND cc.fk_class_id = c.id                                     ");
        sql.append(" )                                                                ");
        return this.myGeneralDao.getMapBySQL(sql.toString(), type, SiteUtil.getSiteCode());
    }

    @Override
    public List<Map<String, Object>> listFixedPlace(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        // 获取班级信息
        BigInteger studentNumber = this.myGeneralDao
                .getOneBySQL("select count(1) from pe_student where fk_class_id = ?", classId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                       ");
        sql.append(" 	concat(                                                   ");
        sql.append(" 		date_format(cct.training_date, '%Y%m%d'),             ");
        sql.append(" 		ct.`name`                                             ");
        sql.append(" 	)                                                         ");
        sql.append(" FROM                                                         ");
        sql.append(" 	class_course_timetable cct                                ");
        sql.append(" INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id ");
        sql.append(" INNER JOIN course_time ct ON ct.id = cct.fk_course_time_id   ");
        sql.append(" WHERE                                                        ");
        sql.append(" 	cc.fk_class_id = ?                                        ");
        List<String> timeArea = this.myGeneralDao.getBySQL(sql.toString(), classId);
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                                    ");
        sql.append(" 	pp.id as id,                                                           ");
        sql.append(" 	concat(pp.name, '(', pp.capacity, ')') as `name`                       ");
        sql.append(" FROM                                                                      ");
        sql.append(" 	pe_place pp                                                            ");
        sql.append(" INNER JOIN enum_const ec ON ec.id = pp.flag_is_valid                      ");
        sql.append(" WHERE                                                                     ");
        sql.append(" 	pp.capacity >= ?                                                       ");
        sql.append(" and pp.site_code >= '" + SiteUtil.getSiteCode() + "'                      ");
        sql.append(" AND NOT EXISTS (                                                          ");
        sql.append(" 	SELECT                                                                 ");
        sql.append(" 		1                                                                  ");
        sql.append(" 	FROM                                                                   ");
        sql.append(" 		class_course_timetable cct                                         ");
        sql.append(" 	INNER JOIN course_time ct ON ct.id = cct.fk_course_time_id             ");
        sql.append(" 	WHERE                                                                  ");
        sql.append(" 		cct.fk_place_id = pp.id                                            ");
        sql.append(" 	AND " + CommonUtils.madeSqlIn(timeArea,
                "concat(date_format(cct.training_date, '%Y%m%d'),ct.`name`)"));
        sql.append(" )                                                                         ");
        sql.append(" AND NOT EXISTS (                                                          ");
        sql.append(" 	SELECT                                                                 ");
        sql.append(" 		1                                                                  ");
        sql.append(" 	FROM                                                                   ");
        sql.append(" 		temporary_use_place cct                                         ");
        sql.append(" 	INNER JOIN course_time ct ON ct.id = cct.fk_course_time_id             ");
        sql.append(" 	WHERE                                                                  ");
        sql.append(" 		cct.fk_place_id = pp.id                                            ");
        sql.append(" 	AND " + CommonUtils.madeSqlIn(timeArea,
                "concat(date_format(cct.use_date, '%Y%m%d'),ct.`name`)"));
        sql.append(" )                                                                         ");
        sql.append(" AND ec.id = '2'                                                           ");
        return this.myGeneralDao.getMapBySQL(sql.toString(), studentNumber);
    }

    @Override
    public void doArrangeFixed(String classId, String placeId, String color) {
        TycjParameterAssert.isAllNotBlank(classId, placeId, color);
        this.classTrainingPlaceManageService.doSetTrainingPlace(classId, placeId, color);
    }

    @Override
    public List<Map<String, Object>> listTime(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                       ");
        sql.append("    cast(cct.id as char) as id,                               ");
        sql.append(" 	concat(                                                   ");
        sql.append(" 		date_format(cct.training_date, '%Y年%m月%d日'),        ");
        sql.append(" 		ct.`start_time`,                                      ");
        sql.append(" 		' - ',                                                ");
        sql.append(" 		ct.`end_time`,                                        ");
        sql.append(" 		' ',                                                  ");
        sql.append(" 		ct.name                                               ");
        sql.append(" 	) as name,                                                ");
        sql.append("    it.name as itemType,                                      ");
        sql.append("    ty.name as teachingType                                   ");
        sql.append(" FROM                                                         ");
        sql.append(" 	class_course_timetable cct                                ");
        sql.append(" INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id ");
        sql.append(" INNER JOIN pe_course cou on cou.id = cc.fk_course_id         ");
        sql.append(" INNER JOIN enum_const ty on ty.id = cou.flag_course_type     ");
        sql.append(" INNER JOIN course_time ct ON ct.id = cct.fk_course_time_id   ");
        sql.append(" INNER JOIN pe_class c on c.id = cc.fk_class_id               ");
        sql.append(" INNER JOIN training_item ti on ti.id = c.fk_training_item_id ");
        sql.append(" INNER JOIN enum_const it on it.id = ti.flag_training_item_type");
        sql.append(" WHERE                                                        ");
        sql.append(" 	cc.fk_class_id = ?                                        ");
        sql.append(" AND cct.fk_place_id is null                                  ");
        sql.append(" AND c.site_code = ?                                          ");
        sql.append(" ORDER BY cct.training_date, ct.start_time                    ");
        return this.myGeneralDao.getMapBySQL(sql.toString(), classId, SiteUtil.getSiteCode());
    }

    @Override
    public List<Map<String, Object>> listFlowPlace(String timeId) {
        if (StringUtils.isBlank(timeId)) {
            throw new ServiceException("请先选择培训时间");
        }
        TycjParameterAssert.isAllNotBlank(timeId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                       ");
        sql.append(" 	concat(                                                   ");
        sql.append(" 		date_format(cct.training_date, '%Y%m%d'),             ");
        sql.append(" 		ct.`name`                                             ");
        sql.append(" 	) as time                                                ");
        sql.append(" FROM                                                         ");
        sql.append(" 	class_course_timetable cct                                ");
        sql.append(" INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id ");
        sql.append(" INNER JOIN course_time ct ON ct.id = cct.fk_course_time_id   ");
        sql.append(" WHERE                                                        ");
        sql.append(CommonUtils.madeSqlIn(timeId, "cct.id"));
        List<String> timeInfo = this.myGeneralDao.getBySQL(sql.toString());
        // 获取班级信息
        BigInteger studentNumber = this.myGeneralDao
                .getOneBySQL("select count(distinct stu.id) from pe_student stu " +
                    "inner join class_course cc on cc.fk_class_id = stu.fk_class_id " +
                    "inner join class_course_timetable cct on cct.fk_class_course_id = cc.id " +
                    "where " + CommonUtils.madeSqlIn(timeId, "cct.id"));
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                                    ");
        sql.append(" 	pp.id as id,                                                           ");
        sql.append(" 	concat(pp.name, '(', pp.capacity, ')') as `name`                       ");
        sql.append(" FROM                                                                      ");
        sql.append(" 	pe_place pp                                                            ");
        sql.append(" INNER JOIN enum_const ec ON ec.id = pp.flag_is_valid                      ");
        sql.append(" WHERE                                                                     ");
        sql.append(" 	pp.capacity >= ?                                                       ");
        sql.append(" AND NOT EXISTS (                                                          ");
        sql.append(" 	SELECT                                                                 ");
        sql.append(" 		1                                                                  ");
        sql.append(" 	FROM                                                                   ");
        sql.append(" 		class_course_timetable cct                                         ");
        sql.append(" 	INNER JOIN course_time ct ON ct.id = cct.fk_course_time_id             ");
        sql.append(" 	WHERE                                                                  ");
        sql.append(" 		cct.fk_place_id = pp.id                                            ");
        sql.append(" 	AND " + CommonUtils.madeSqlIn(timeInfo,
                "concat(date_format(cct.training_date, '%Y%m%d'),ct.`name`)"));
        sql.append(" )                                                                         ");
        sql.append(" AND NOT EXISTS (                                                          ");
        sql.append(" 	SELECT                                                                 ");
        sql.append(" 		1                                                                  ");
        sql.append(" 	FROM                                                                   ");
        sql.append(" 		temporary_use_place cct                                         ");
        sql.append(" 	INNER JOIN course_time ct ON ct.id = cct.fk_course_time_id             ");
        sql.append(" 	WHERE                                                                  ");
        sql.append(" 		cct.fk_place_id = pp.id                                            ");
        sql.append(" 	AND " + CommonUtils.madeSqlIn(timeInfo,
                "concat(date_format(cct.use_date, '%Y%m%d'),ct.`name`)"));
        sql.append(" )                                                                         ");
        sql.append(" AND pp.site_code = ?                                                      ");
        sql.append(" AND ec.id = '2'                                                           ");
        return this.myGeneralDao.getMapBySQL(sql.toString(), studentNumber, SiteUtil.getSiteCode());
    }

    @Override
    public void doArrangeFlow(List<String> time, String placeId, String color) {
        TycjParameterAssert.isAllNotEmpty(time);
        TycjParameterAssert.isAllNotBlank(placeId, color);
        time.forEach(e -> this.arrangeClassCourseTimetableManageService.doSetTrainingPlace(e, placeId, color));
    }

    @Override
    public List<Map<String, Object>> listTemporaryPlace(String useDate, String timeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                 ");
        sql.append(" 	p.id as id,                         ");
        sql.append(" 	concat(p.name, '(', p.capacity, ')') as `name`");
        sql.append(" FROM                                   ");
        sql.append(" 	pe_place p                          ");
        sql.append(" INNER JOIN enum_const ec ON ec.id = p.flag_is_valid ");
        sql.append(" WHERE                                  ");
        sql.append(" 	NOT EXISTS (                        ");
        sql.append(" 		SELECT                          ");
        sql.append(" 			1                           ");
        sql.append(" 		FROM                            ");
        sql.append(" 			class_course_timetable cct  ");
        sql.append(" 		WHERE                           ");
        sql.append(" 			cct.fk_place_id = p.id      ");
        sql.append(" 		AND " + CommonUtils.madeSqlIn(timeId, "cct.fk_course_time_id"));
        sql.append(" 		AND date_format(cct.training_date, '%Y-%m-%d') = ?");
        sql.append(" 	)                                   ");
        sql.append(" AND NOT EXISTS (                       ");
        sql.append(" 		SELECT                          ");
        sql.append(" 			1                           ");
        sql.append(" 		FROM                            ");
        sql.append(" 			temporary_use_place tup     ");
        sql.append(" 		WHERE                           ");
        sql.append(" 			tup.fk_place_id = p.id      ");
        sql.append(" 		AND " + CommonUtils.madeSqlIn(timeId, "tup.fk_course_time_id"));
        sql.append(" 		AND date_format(tup.use_date, '%Y-%m-%d') = ?");
        sql.append(" 	)                                   ");
        sql.append(" AND ec.id = '2'                        ");
        sql.append(" AND p.site_code = ?                    ");
        return this.myGeneralDao.getMapBySQL(sql.toString(), useDate, useDate, SiteUtil.getSiteCode());
    }

    @Override
    public void doArrangeTemporary(String useDate, List<String> time, String placeId,
                                   String application, String color) {
        TycjParameterAssert.isAllNotEmpty(time);
        TycjParameterAssert.isAllNotBlank(useDate, placeId, application, color);
        try {
            PLACE_ARRANGE_LOCK.lock(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new UncheckException(e);
        }
        try {
            List<Map<String, Object>> timeMap = this.myGeneralDao
                    .getMapBySQL("select date_format(cct.training_date, '%Y-%m-%d') as date, ct.id as time" +
                            " from class_course_timetable cct" +
                            " inner join course_time ct on ct.id = cct.fk_course_time_id" +
                            " where cct.fk_place_id = ?", placeId);

            List<Map<String, Object>> temporaryMap = this.myGeneralDao
                    .getMapBySQL("select date_format(use_date, '%Y-%m-%d') as date, fk_course_time_id as time" +
                            " from temporary_use_place where fk_place_id = ?", placeId);
            if (CollectionUtils.isNotEmpty(temporaryMap)) {
                timeMap.addAll(temporaryMap);
            }
            // 判断地点是否会被重合时间分配
            time.forEach(e -> {
                if (ClazzUtils.predicateTimeAreaCoincide(timeMap, useDate, e)) {
                    throw new ServiceException("此地点此时间段已被安排");
                }
            });
            time.forEach(e -> this.myGeneralDao
                    .executeBySQL("INSERT INTO temporary_use_place(use_date, fk_course_time_id, application, " +
                                    "fk_place_id, color, create_by, create_date ) VALUES (?, ?, ?, ?, ?, ?, now())",
                            CommonUtils.changeStringToDate(useDate), e, application, placeId, color, UserUtils.getCurrentUserId()));
        } finally {
            PLACE_ARRANGE_LOCK.unlock();
        }
    }

    @Override
    public void deleteArrange(String id, String type) {
        TycjParameterAssert.isAllNotBlank(id, type);
        try {
            PLACE_ARRANGE_LOCK.lock(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new UncheckException(e);
        }
        try {
            if (!ArrangeType.checkCanDelete(id, type)) {
                throw new ServiceException("当前地点已经开始使用或者此安排数据的类型为固定地点，无法删除");
            }
            ArrangeType.executeDelete(id, type);
        } finally {
            PLACE_ARRANGE_LOCK.unlock();
        }
    }

    @Override
    public List<Map<String, Object>> listCanDeleteClass() {
        return this.myGeneralDao.getMapBySQL(ScopeHandleUtils
                .handleScopeSignOfSql("SELECT c.id AS id, c. NAME AS `name` FROM pe_class c " +
                                "INNER JOIN class_course cc ON cc.fk_class_id = c.id " +
                                "INNER JOIN class_course_timetable cct ON cct.fk_class_course_id = cc.id " +
                                "WHERE c.start_time > now() AND cct.fk_place_id IS NOT NULL " +
                                "and [peUnit|c.fk_unit_id] " +
                                "and c.site_code = '" + SiteUtil.getSiteCode() + "' " +
                                "GROUP BY c.id", UserUtils.getCurrentUserId()));
    }

    @Override
    public void deleteClassArrange(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        try {
            PLACE_ARRANGE_LOCK.lock(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new UncheckException(e);
        }
        try {
            if (this.myGeneralDao.checkNotEmpty("select 1 from pe_class where start_time <= now() and id = ?", classId)) {
                throw new ServiceException("班级已经开始培训，无法删除");
            }
            this.myGeneralDao.executeBySQL("UPDATE class_course_timetable cct " +
                    "INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id " +
                    "SET cct.arrange_place_time = NULL, cct.arrange_place_user = NULL, cct.color = NULL, " +
                    "cct.fk_place_id = NULL WHERE cc.fk_class_id = ?", classId);
        } finally {
            PLACE_ARRANGE_LOCK.unlock();
        }
    }

    private enum ArrangeType {

        COURSE_TIME_TABLE("class",
                id -> StaticBeanUtils.getGeneralDao()
                        .checkNotEmpty("SELECT 1 FROM class_course_timetable cct " +
                                "INNER JOIN class_course cc on cc.id = cct.fk_class_course_id " +
                                "INNER JOIN pe_class c on c.id = cc.fk_class_id " +
                                "INNER JOIN enum_const pt on pt.id = c.flag_place_type " +
                                "INNER JOIN course_time ct ON ct.id = cct.fk_course_time_id " +
                                "WHERE concat( DATE_FORMAT(cct.training_date, '%Y%m%d'), ct.start_time ) " +
                                "> DATE_FORMAT(now(), '%Y%m%d%h%i%s') and cct.id = ? and pt.code = '2'", id),
                id -> StaticBeanUtils.getGeneralDao()
                        .executeBySQL("update class_course_timetable set arrange_place_time = null," +
                                " arrange_place_user = null, fk_place_id = null, color = null where id = ?", id)),
        TEMPORARY_USE("temp",
                id -> StaticBeanUtils.getGeneralDao()
                        .checkNotEmpty("SELECT 1 FROM temporary_use_place cct " +
                                "INNER JOIN course_time ct ON ct.id = cct.fk_course_time_id " +
                                "WHERE concat( DATE_FORMAT(cct.use_date, '%Y%m%d'), ct.start_time ) " +
                                "> DATE_FORMAT(now(), '%Y%m%d%h%i%s') and cct.id = ?", id),
                id -> StaticBeanUtils.getGeneralDao()
                        .executeBySQL("delete from temporary_use_place where id = ?", id)),
        ;

        private String type;

        private Function<String, Boolean> canDeleteFunction;

        private Consumer<String> deleteConsumer;

        ArrangeType(String type, Function<String, Boolean> canDeleteFunction, Consumer<String> deleteConsumer) {
            this.type = type;
            this.canDeleteFunction = canDeleteFunction;
            this.deleteConsumer = deleteConsumer;
        }

        static Boolean checkCanDelete(String id, String type) {
            return getArrangeType(type).getCanDeleteFunction().apply(id);
        }

        static void executeDelete(String id, String type) {
            getArrangeType(type).getDeleteConsumer().accept(id);
        }

        static ArrangeType getArrangeType(String type) {
            ArrangeType arrangeType = Arrays.stream(values()).filter(e -> e.getType().equals(type))
                    .findFirst().orElse(null);
            if (arrangeType == null) {
                throw new IllegalArgumentException();
            }
            return arrangeType;
        }

        public String getType() {
            return type;
        }

        public Function<String, Boolean> getCanDeleteFunction() {
            return canDeleteFunction;
        }

        public Consumer<String> getDeleteConsumer() {
            return deleteConsumer;
        }
    }

    /**
     * 递增天数
     * @param origin
     * @return
     */
    private Date incrementDay(Date origin) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(origin);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 计算两个日期相差天数
     * @param date1
     * @param date2
     * @return
     */
    private int countDifferenceForDate(Date date1, Date date2) {
        long difference = date1.getTime() - date2.getTime();
        return (int) (difference / (1000 * 3600 * 24));
    }

}
