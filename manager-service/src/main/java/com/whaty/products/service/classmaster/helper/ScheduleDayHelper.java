package com.whaty.products.service.classmaster.helper;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjAssert;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.products.service.classmaster.domain.AbstractScheduleItem;
import com.whaty.products.service.classmaster.domain.ScheduleActivityItem;
import com.whaty.products.service.classmaster.domain.ScheduleCourseItem;
import com.whaty.products.service.classmaster.domain.ScheduleDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 日程辅助类
 *
 * @author weipengsen
 */
public class ScheduleDayHelper {

    private String classId;

    private final Map<String, List<AbstractScheduleItem>> items = new TreeMap<>();

    private final GeneralDao generalDao;

    public ScheduleDayHelper(String classId) {
        TycjAssert.isAllNotBlank(classId);
        this.classId = classId;
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.GENERAL_DAO_BEAN_NAME);
    }

    /**
     * 列举日程
     *
     * @return
     */
    public List<ScheduleDay> listScheduleDay() {
        this.putAll(this.listCourses(), ScheduleCourseItem.class);
        this.putAll(this.listHomework(), ScheduleActivityItem.class);
        this.putAll(this.listQuestion(), ScheduleActivityItem.class);
        return this.groupOriginDataByDay();
    }

    /**
     * 列举课程表
     * @return
     */
    public List<ScheduleDay> listCourseTimeTable() {
        this.putAll(this.listCourses(), ScheduleCourseItem.class);
        return this.groupOriginDataByDay();
    }

    /**
     * 将源数据归纳
     * @return
     */
    private List<ScheduleDay> groupOriginDataByDay() {
        return this.items.entrySet().stream()
                .map(e -> new ScheduleDay(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(ScheduleDay::getDay))
                .collect(Collectors.toList());
    }

    /**
     * 列举问卷
     *
     * @return
     */
    private List<Map<String, Object>> listQuestion() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                               ");
        sql.append(" 	hg.id AS id,                                                      ");
        sql.append(" 	hg.title AS title,                                                ");
        sql.append(" 	DATE_FORMAT(                                                      ");
        sql.append(" 		hg.start_time,                                                ");
        sql.append(" 		'%Y年%m月%d日'                                                 ");
        sql.append(" 	) AS `day`,                                                       ");
        sql.append("    ifnull(c.name, '项目任务') as courseName,                           ");
        sql.append(" 	DATE_FORMAT(hg.start_time, '%H:%i') AS `time`,                    ");
        sql.append(" 	if(ty.code = '1', '调查问卷', '投票') AS activityName,              ");
        sql.append(" 	concat(                                                           ");
        sql.append(" 		DATE_FORMAT(                                                  ");
        sql.append(" 			hg.start_time,                                            ");
        sql.append(" 			'%Y-%m-%d %H:%i'                                          ");
        sql.append(" 		),                                                            ");
        sql.append(" 		' ~ ',                                                        ");
        sql.append(" 		DATE_FORMAT(                                                  ");
        sql.append(" 			hg.end_time,                                              ");
        sql.append(" 			'%Y-%m-%d %H:%i'                                          ");
        sql.append(" 		)                                                             ");
        sql.append(" 	) AS timeScope                                                    ");
        sql.append(" FROM                                                                 ");
        sql.append(" 	questionnaire_group hg                                            ");
        sql.append(" inner join enum_const ty on ty.id = hg.flag_questionnaire_group_type ");
        sql.append(" LEFT JOIN pe_course c ON c.id = hg.fk_course_id                      ");
        sql.append(" WHERE                                                                ");
        sql.append(" 	hg.item_id = ?                                                    ");
        sql.append(" AND hg.is_delete = '0'                                               ");
        return this.generalDao.getMapBySQL(sql.toString(), this.classId);
    }

    /**
     * 列举作业
     *
     * @return
     */
    private List<Map<String, Object>> listHomework() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                            ");
        sql.append(" 	hg.id AS id,                                   ");
        sql.append(" 	hg.title AS title,                             ");
        sql.append(" 	DATE_FORMAT(                                   ");
        sql.append(" 		hg.start_time,                             ");
        sql.append(" 		'%Y年%m月%d日'                             ");
        sql.append(" 	) AS `day`,                                    ");
        sql.append("    ifnull(c.name, '项目任务') as courseName,        ");
        sql.append(" 	DATE_FORMAT(hg.start_time, '%H:%i') AS `time`, ");
        sql.append(" 	'作业' AS activityName,                    ");
        sql.append(" 	concat(                                        ");
        sql.append(" 		DATE_FORMAT(                               ");
        sql.append(" 			hg.start_time,                         ");
        sql.append(" 			'%Y-%m-%d %H:%i'                       ");
        sql.append(" 		),                                         ");
        sql.append(" 		' ~ ',                                     ");
        sql.append(" 		DATE_FORMAT(                               ");
        sql.append(" 			hg.end_time,                           ");
        sql.append(" 			'%Y-%m-%d %H:%i'                       ");
        sql.append(" 		)                                          ");
        sql.append(" 	) AS timeScope                                 ");
        sql.append(" FROM                                              ");
        sql.append(" 	homework_group hg                              ");
        sql.append(" LEFT JOIN pe_course c ON c.id = hg.fk_course_id   ");
        sql.append(" WHERE                                             ");
        sql.append(" 	hg.item_id = ?                                 ");
        sql.append(" AND hg.is_delete = '0'                            ");
        return this.generalDao.getMapBySQL(sql.toString(), this.classId);
    }

    /**
     * 将查询出的源数据转换格式放入容器
     *
     * @param origin
     * @param scheduleItemClass
     * @param <T>
     */
    private <T extends AbstractScheduleItem> void putAll(List<Map<String, Object>> origin, Class<T> scheduleItemClass) {
        origin.stream().map(e -> JSON.parseObject(JSON.toJSONString(e), scheduleItemClass))
                .forEach(e -> {
                    this.items.computeIfPresent(e.getDay(), (k, o) -> {o.add(e);return o;});
                    this.items.putIfAbsent(e.getDay(), new ArrayList<>(Collections.singletonList(e)));
                });
    }

    /**
     * 列举课程
     *
     * @return
     */
    private List<Map<String, Object>> listCourses() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT               ");
        sql.append(" 	cct.id as id,       ");
        sql.append(" 	date_format(cct.training_date, '%Y年%m月%d日') as `day`, ");
        sql.append(" 	concat(cct.start_time, ' ~ ', cct.end_time) as time,    ");
        sql.append(" 	c.`name` as title,                                      ");
        sql.append(" 	tea.true_name as teacherName,                           ");
        sql.append(" 	ty.name as courseType,                                  ");
        sql.append(" 	ifnull(cct.teacher_fee, 0) as teacherFee,               ");
        sql.append(" 	pp.name as address                                      ");
        sql.append(" FROM                                                       ");
        sql.append(" 	class_course cc                                         ");
        sql.append(" INNER JOIN pe_course c ON c.id = cc.fk_course_id           ");
        sql.append(" INNER JOIN pe_teacher tea ON tea.id = c.fk_teacher_id      ");
        sql.append(" INNER JOIN class_course_timetable cct on cct.fk_class_course_id = cc.id  ");
        sql.append(" INNER JOIN pe_place pp on pp.id = cct.fk_place_id          ");
        sql.append(" LEFT JOIN enum_const ty ON ty.id = cct.flag_course_type     ");
        sql.append(" WHERE                                                      ");
        sql.append(" 	cct.training_date is not null                           ");
        sql.append(" AND cc.fk_class_id = ?                                     ");
        return this.generalDao.getMapBySQL(sql.toString(), this.classId);
    }

}
