package com.whaty.products.service.clazz.impl;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.ClassCourse;
import com.whaty.domain.bean.ClassCourseTimetable;
import com.whaty.domain.bean.PePlace;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.products.service.clazz.constants.ClazzConstants;
import com.whaty.products.service.clazz.utils.ClazzUtils;
import com.whaty.products.service.common.UtilService;
import com.whaty.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级课程表安排
 *
 * @author weipengsen
 */
@Lazy
@Service("arrangeClassCourseTimetableService")
public class ArrangeClassCourseTimetableServiceImpl extends AbstractTwoLevelListGridServiceImpl<ClassCourseTimetable> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.UTIL_SERVICE_BEAN_NAME)
    private UtilService utilService;

    /**
     * 列举所有附件
     * @param courseId
     * @return
     */
    public List<Object[]> listAttachFiles(String courseId) {
        return this.myGeneralDao.getBySQL("select cast(id as char), `name` from attach_file where link_id = ?", courseId);
    }

    /**
     * 设置课件
     * @param ids
     * @param attachId
     */
    public void doSetCourseware(String ids, String attachId) {
        TycjParameterAssert.isAllNotBlank(ids, attachId);
        this.myGeneralDao.executeBySQL("update class_course_timetable set fk_courseware_id = ? where id = ?",
                attachId, ids);
    }

    @Override
    public void checkBeforeAdd(ClassCourseTimetable bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        ClassCourse classCourse = (ClassCourse) this.myGeneralDao
                .getOneByHQL("from ClassCourse where peCourse.id = ? and peClass.id = ?",
                        bean.getClassCourse().getPeCourse().getId(), bean.getClassCourse().getPeClass().getId());
        if (classCourse == null) {
            throw new EntityException("班级中没有关联此课程");
        }
        this.checkBeforeAddOrUpdate(bean);
        bean.setClassCourse(classCourse);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateTime(new Date());
    }

    @Override
    public void checkBeforeUpdate(ClassCourseTimetable bean) throws EntityException {
        ClassCourse classCourse = (ClassCourse) this.myGeneralDao
                .getOneByHQL("from ClassCourse where peCourse.id = ? and peClass.id = ?",
                        bean.getClassCourse().getPeCourse().getId(), bean.getClassCourse().getPeClass().getId());
        if (classCourse == null) {
            throw new EntityException("班级中没有关联此课程");
        }
        this.checkBeforeAddOrUpdate(bean);
        bean.setClassCourse(classCourse);
    }

    /**
     * 添加或修改前检查
     * @param bean
     * @throws EntityException
     */
    private void checkBeforeAddOrUpdate(ClassCourseTimetable bean) throws EntityException {
        // 此班级所有的课表时间区域
        String sql = "select 1 from class_course_timetable cct " +
                "inner join class_course cc on cc.id = cct.fk_class_course_id " +
                "where ((start_time < ? AND end_time > ?) OR (start_time <= ? AND end_time >= ?)" +
                " OR (end_time > ? AND end_time < ?)) AND cc.fk_class_id = ? and cct.training_date = ?";
        sql += StringUtils.isBlank(bean.getId()) ? "" : " AND cct.id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty(sql, bean.getStartTime(), bean.getEndTime(), bean.getStartTime(),
                bean.getEndTime(), bean.getStartTime(), bean.getEndTime(),
                bean.getClassCourse().getPeClass().getId(), bean.getTrainingDate())) {
            throw new EntityException("此时间段已经被设置");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.myGeneralDao.checkNotEmpty("select 1 from class_course_timetable where fk_place_id is not null and "
                + CommonUtils.madeSqlIn(idList, "id"))) {
            throw new EntityException("存在已经设置了地点的课程表，无法删除");
        }
    }

    /**
     * 初始化课程表
     *
     * @param clazzId
     * @return
     */
    public int doInitCourseTimeTable(String clazzId) {
        return this.myGeneralDao
                .executeBySQL("insert into class_course_timetable(fk_class_course_id, create_by, create_date)" +
                        " select cc.id, ?, now() from class_course cc " +
                        " LEFT JOIN class_course_timetable cct ON cct.fk_class_course_id = cc.id " +
                        " WHERE cct.id IS NULL AND cc.fk_class_id = ? ", UserUtils.getCurrentUserId(), clazzId);
    }

    /**
     * 批量导入课程表模板
     *
     * @param clazzId
     * @param outputStream
     */
    public void downloadCourseTimeTableTemplate(String clazzId, OutputStream outputStream) {
        Map<String, List<String>> selectMap = new HashMap<>(6);
        String siteCode = SiteUtil.getSiteCode();
        selectMap.put("课程名称*", this.myGeneralDao
                .getBySQL("select name from pe_course WHERE site_code = ?", siteCode));
        selectMap.put("培训地点*", this.myGeneralDao.getBySQL("SELECT name FROM pe_place WHERE site_code = ? ",
                siteCode));
        this.utilService.generateExcelAndWrite(outputStream, selectMap, ClazzConstants.UPLOAD_EXCEL_COURSE_TIMETABLE,
                "日期格式为：yyyy-MM-dd；时间格式为：00：00~23：59；");
    }

    /**
     * 导入课程表
     *
     * @param clazzId
     * @param file
     * @return
     */
    public Map<String, Object> doUploadCourseTimeTableTemplate(String clazzId, File file) {
        TycjParameterAssert.isAllNotNull(clazzId);
        List<String[]> checkList = new ArrayList<>();
        List<String> sqlList = new ArrayList<>();
        String siteCode = SiteUtil.getSiteCode();
        StringBuilder sql = new StringBuilder();
        sql.append(" NOT EXISTS (                                                 ");
        sql.append("   select                                                     ");
        sql.append("     1                                                        ");
        sql.append("   from                                                       ");
        sql.append("     pe_course pc                                             ");
        sql.append("   WHERE                                                      ");
        sql.append("     pc.site_code = '" + siteCode + "'                        ");
        sql.append("   and pc.name = e.column1                                    ");
        sql.append(" )                                                            ");
        checkList.add(new String[]{"当前课程不存在;", sql.toString()});
        checkList.add(new String[]{"师资费用必须为0-5位的整数或小数;",
                "e.column5 NOT REGEXP '^[0-9]{1,5}(\\\\.[0-9]{1,2})?$'"});
        checkList.add(new String[]{"开始时间格式必须为00:00~23:59;",
                "e.column3 NOT REGEXP '^(([0-1][0-9])|(2[0-4])):([0-5][0-9])$'"});
        checkList.add(new String[]{"结束时间格式必须为00:00~23:59;",
                "e.column4 NOT REGEXP '^(([0-1][0-9])|(2[0-4])):([0-5][0-9])$'"});
        checkList.add(new String[]{"日期格式必须为yyyy-MM-dd;",
                "e.column2 NOT REGEXP '" + ValidateUtils.DATE_DETAIL_REG_STR + "'"});
        sql.delete(0, sql.length());
        sql.append(" EXISTS (                                                                 ");
        sql.append("   select                                                                 ");
        sql.append("     1                                                                    ");
        sql.append("   from                                                                   ");
        sql.append("     (select * from util_excel_table where namespace = ${namespace}) uet  ");
        sql.append("   where                                                                  ");
        sql.append("     uet.namespace = ${namespace}                                         ");
        sql.append("   and uet.id <> e.id                                                     ");
        sql.append("   and uet.column2 = e.column2                                            ");
        sql.append("   and ( (uet.column4>e.column3 and uet.column4<=e.column4) or            ");
        sql.append("       (uet.column3>=e.column3 and uet.column3<e.column4) or              ");
        sql.append("       (uet.column3<=e.column3 and uet.column4>=e.column4)                ");
        sql.append("        )                                                                 ");
        sql.append(" )                                                                        ");
        checkList.add(new String[]{"上课时间与表格内其他课程冲突;", sql.toString()});
        sql.delete(0, sql.length());
        sql.append(" EXISTS (                                                                    ");
        sql.append("   select                                                                    ");
        sql.append("     1                                                                       ");
        sql.append("   from                                                                      ");
        sql.append("     class_course_timetable cct                                              ");
        sql.append("   inner join class_course cc on cc.id=cct.fk_class_course_id                ");
        sql.append("   inner join pe_course pc on pc.id=cc.fk_course_id                          ");
        sql.append("   where                                                                     ");
        sql.append("     cc.fk_class_id = '" + clazzId + "'                                      ");
        sql.append("   and e.column2 = cct.training_date                                         ");
        sql.append("   and pc.name <> e.column1                                                  ");
        sql.append("   and ( (e.column4>cct.start_time and e.column4<=cct.end_time) or           ");
        sql.append("       (e.column3>=cct.start_time and e.column3<cct.end_time) or             ");
        sql.append("       (e.column3<=cct.start_time and e.column4>=cct.end_time)               ");
        sql.append("        )                                                                    ");
        sql.append(" )                                                                           ");
        checkList.add(new String[]{"上课时间与已导入课程冲突;", sql.toString()});
        sql.delete(0, sql.length());
        sql.append(" EXISTS (                                                                              ");
        sql.append("   SELECT                                                                              ");
        sql.append("   	1                                                                                  ");
        sql.append("   FROM                                                                                ");
        sql.append("   	class_course_timetable cct                                                         ");
        sql.append("   inner join class_course cc on cc.id=cct.fk_class_course_id                          ");
        sql.append("   INNER JOIN pe_place pp ON cct.fk_place_id = pp.id                                   ");
        sql.append("   WHERE                                                                               ");
        sql.append("     ( (e.column4>cct.start_time and e.column4<=cct.end_time) or                       ");
        sql.append("       (e.column3>=cct.start_time and e.column3<cct.end_time) or                       ");
        sql.append("       (e.column3<=cct.start_time and e.column4>=cct.end_time)                         ");
        sql.append("     )                                                                                 ");
        sql.append("   and e.column2 = cct.training_date                                                   ");
        sql.append("   and pp.site_code = '" + siteCode + "'                                               ");
        sql.append("   and pp.name = e.column6                                                             ");
        sql.append("   and cc.fk_class_id <> '" + clazzId + "'                                             ");
        sql.append(" )                                                                                     ");
        checkList.add(new String[]{"此地点已经安排了重合了的培训时间;", sql.toString()});
        sql.delete(0, sql.length());
        String userId = UserUtils.getCurrentUserId();
        sql.append(" update class_course_timetable cct                                     ");
        sql.append(" inner join class_course cc on cc.id=cct.fk_class_course_id            ");
        sql.append(" inner join pe_course pc on pc.id=cc.fk_course_id                      ");
        sql.append(" inner join util_excel_table ut on ut.column1=pc.name                  ");
        sql.append(" INNER JOIN pe_place pp ON pp.name = ut.column6                        ");
        sql.append(" and pp.site_code = '" + siteCode + "'                                 ");
        sql.append(" set cct.fk_place_id = pp.id,                                          ");
        sql.append(" cct.training_date = ut.column2,                                       ");
        sql.append(" cct.start_time = ut.column3,                                          ");
        sql.append(" cct.end_time = ut.column4,                                            ");
        sql.append(" cct.arrange_place_user = '" + userId + "',                            ");
        sql.append(" cct.arrange_place_time = now()                                        ");
        sql.append(" where                                                                 ");
        sql.append("   cc.fk_class_id = '" + clazzId + "'                                  ");
        sql.append(" AND ut.namespace = ${namespace}                                       ");
        sql.append(" AND ut.valid = '1'                                                    ");
        sqlList.add(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" insert into class_course (                                                     ");
        sql.append("   id,                                                                          ");
        sql.append("   fk_course_id,                                                                ");
        sql.append("   fk_class_id,                                                                 ");
        sql.append("   create_by,                                                                   ");
        sql.append("   create_date                                                                  ");
        sql.append(" ) select                                                                       ");
        sql.append("     replace(uuid(),'-',''),                                                    ");
        sql.append("     pc.id,                                                                     ");
        sql.append("     '" + clazzId + "',                                                         ");
        sql.append("     '" + userId + "',                                                          ");
        sql.append("     now()                                                                      ");
        sql.append("   from                                                                         ");
        sql.append("     util_excel_table ut                                                        ");
        sql.append("   inner join pe_course pc on pc.site_code = '" + siteCode + "'                 ");
        sql.append("   and ut.column1=pc.name                                                       ");
        sql.append("   left join (                                                                  ");
        sql.append("     select                                                                     ");
        sql.append("       pcc.id as id,                                                            ");
        sql.append("       pcc.name as name                                                         ");
        sql.append("     from                                                                       ");
        sql.append("       class_course cc                                                          ");
        sql.append("     inner join pe_course pcc on pcc.id = cc.fk_course_id                       ");
        sql.append("     where                                                                      ");
        sql.append("       pcc.site_code = '" + siteCode + "'                                       ");
        sql.append("     and cc.fk_class_id = '" + clazzId + "'                                     ");
        sql.append("   ) cou on cou.name = pc.name                                                  ");
        sql.append("   where                                                                        ");
        sql.append("     ifnull(cou.id,'') = ''                                                     ");
        sql.append("   AND ut.namespace = ${namespace}                                              ");
        sql.append("   AND ut.valid = '1'                                                           ");
        sql.append("   group by ut.column1                                                          ");
        sqlList.add(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" insert into pe_place (                                                         ");
        sql.append("   id,                                                                          ");
        sql.append("   fk_place_unit,                                                               ");
        sql.append("   name,                                                                        ");
        sql.append("   site_code,                                                                   ");
        sql.append("   create_by,                                                                   ");
        sql.append("   create_date,                                                                 ");
        sql.append("   flag_is_valid                                                                ");
        sql.append(" ) select                                                                       ");
        sql.append("     replace(uuid(),'-',''),                                                    ");
        sql.append(UserUtils.getCurrentUnit() == null ? " null," : "'" + UserUtils.getCurrentUnit().getId() + "',");
        sql.append("     ut.column6,                                                                ");
        sql.append("     '" + siteCode + "',                                                        ");
        sql.append("     '" + userId + "',                                                          ");
        sql.append("     now(),                                                                     ");
        sql.append("     em.id                                                                      ");
        sql.append("   from                                                                         ");
        sql.append("     util_excel_table ut                                                        ");
        sql.append("   inner join enum_const em on em.namespace = 'flagIsValid'                     ");
        sql.append("   and em.code = '1'                                                            ");
        sql.append("   left join pe_place pc on pc.site_code = '" + siteCode + "'                   ");
        sql.append("   and ut.column6=pc.name                                                       ");
        sql.append("   where                                                                        ");
        sql.append("     ifnull(pc.id,'') = ''                                                      ");
        sql.append("   AND ut.namespace = ${namespace}                                              ");
        sql.append("   AND ut.valid = '1'                                                           ");
        sql.append("   group by ut.column6                                                          ");
        sqlList.add(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" insert into class_course_timetable (                                           ");
        sql.append("   fk_class_course_id,                                                          ");
        sql.append("   create_by,                                                                   ");
        sql.append("   create_date,                                                                 ");
        sql.append("   arrange_place_user,                                                          ");
        sql.append("   arrange_place_time,                                                          ");
        sql.append("   training_date,                                                               ");
        sql.append("   start_time,                                                                  ");
        sql.append("   end_time,                                                                    ");
        sql.append("   teacher_fee,                                                                 ");
        sql.append("   fk_place_id                                                                  ");
        sql.append(" ) select                                                                       ");
        sql.append("     cc.id,                                                                     ");
        sql.append("     '" + userId + "',                                                          ");
        sql.append("     now(),                                                                     ");
        sql.append("     '" + userId + "',                                                          ");
        sql.append("     now(),                                                                     ");
        sql.append("     ut.column2,                                                                ");
        sql.append("     ut.column3,                                                                ");
        sql.append("     ut.column4,                                                                ");
        sql.append("     ut.column5,                                                                ");
        sql.append("     pp.id                                                                      ");
        sql.append("   from                                                                         ");
        sql.append("     util_excel_table ut                                                        ");
        sql.append("   inner join class_course cc on cc.fk_class_id='" + clazzId + "'               ");
        sql.append("   inner join pe_course pc on pc.id=cc.fk_course_id                             ");
        sql.append("   and ut.column1=pc.name                                                       ");
        sql.append("   INNER JOIN pe_place pp ON pp.name = ut.column6                               ");
        sql.append("   and pp.site_code = '" + siteCode + "'                                        ");
        sql.append("   left JOIN class_course_timetable cct ON cct.fk_class_course_id = cc.id       ");
        sql.append("   where                                                                        ");
        sql.append("     ifnull(cct.id,'') = ''                                                     ");
        sql.append("   AND ut.namespace = ${namespace}                                              ");
        sql.append("   AND ut.valid = '1'                                                           ");
        sqlList.add(sql.toString());
        return this.utilService.doUploadFile(file, ClazzConstants.UPLOAD_EXCEL_COURSE_TIMETABLE, checkList,
                sqlList, 1, "课程表导入成功");
    }

    /**
     * 获取课程教师信息
     *
     * @param query
     * @return
     */
    public List<Map<String, Object>> listCourseTeacher(String query) {
        return this.myGeneralDao.getMapBySQL("select pc.id as id,concat(pc.name,'/',tea.true_name) as name," +
                " tea.true_name as teaName" +
                " from pe_course pc " +
                " INNER JOIN pe_teacher tea ON tea.id = pc.fk_teacher_id " +
                " WHERE pc.site_code = ? and pc.name like '%" + query + "%'", SiteUtil.getSiteCode());
    }

    /**
     * 添加班级课程表
     *
     * @param timetable
     */
    public void doAddClassTimetable(ClassCourseTimetable timetable) {
        SsoUser user = UserUtils.getCurrentUser();
        ClassCourse classCourse = this.myGeneralDao
                .getOneByHQL(" from ClassCourse where peCourse.id = ? and peClass.id = ?",
                        timetable.getPeCourse().getId(), timetable.getPeClass().getId());

        if (classCourse == null) {
            classCourse = new ClassCourse();
            classCourse.setCreateDate(new Date());
            classCourse.setCreateBy(user);
            classCourse.setPeClass(timetable.getPeClass());
            classCourse.setPeCourse(timetable.getPeCourse());
            this.myGeneralDao.save(classCourse);
        }
        //验证

        String trainingDate = CommonUtils.changeDateToString(timetable.getTrainingDate(), "yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("select cct.start_time as startTime,cct.end_time as endTime from class_course_timetable cct " +
                "inner join class_course cc on cc.id = cct.fk_class_course_id " +
                "where cc.fk_class_id = ? and cct.training_date = ?");
        sql.append(StringUtils.isNotBlank(timetable.getId()) ? " and cct.id <> " +timetable.getId() : "");
        // 验证班级排课时间段是否重叠
        if (ClazzUtils.verificationPeriodConflict(
                this.myGeneralDao.getMapBySQL(sql.toString(), timetable.getPeClass().getId(), trainingDate),
                timetable.getStartTime(), timetable.getEndTime())) {
            throw new ServiceException("此时间段已安排其他课程");
        }
        sql.delete(0, sql.length());

        PePlace pePlace = StringUtils.isBlank(timetable.getPePlace().getId()) ? (PePlace) this.myGeneralDao
                .getOneByHQL(" from PePlace where name = ? and site_code = ?",
                        timetable.getPlaceName(), SiteUtil.getSiteCode()) : timetable.getPePlace();
        boolean flagAddPlace = pePlace == null || StringUtils.isBlank(pePlace.getId());
        if (flagAddPlace) {
            pePlace = new PePlace();
            pePlace.setName(timetable.getPlaceName());
            pePlace.setCreateBy(user);
            pePlace.setPeUnit(UserUtils.getCurrentUnit());
            pePlace.setEnumConstByFlagIsValid(this.myGeneralDao.getEnumConstByNamespaceCode(EnumConstConstants
                    .ENUM_CONST_NAMESPACE_FLAG_IS_VALID, "1"));
            pePlace.setCreateDate(new Date());
            pePlace.setSiteCode(SiteUtil.getSiteCode());
            this.myGeneralDao.save(pePlace);
        }
        // 地点为新增或课表地点类型为流动地点时不校验地点冲突
        if (!flagAddPlace ||
                this.myGeneralDao.getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_FLAG_PLACE_TYPE,
                        "2").getId().equals(timetable.getEnumConstByFlagPlaceType().getId())) {
            sql.append(" SELECT cct.start_time as startTime,cct.end_time as endTime                          ");
            sql.append(" FROM class_course_timetable cct                                                     ");
            sql.append(" INNER JOIN pe_place pp ON cct.fk_place_id = pp.id                                   ");
            sql.append(" WHERE cct.training_date = ? and pp.id = ?                                           ");
            sql.append(StringUtils.isNotBlank(timetable.getId()) ? " and cct.id <> " +timetable.getId() : "");
            if (ClazzUtils.verificationPeriodConflict(
                    this.myGeneralDao.getMapBySQL(sql.toString(), trainingDate, timetable.getPePlace().getId()),
                    timetable.getStartTime(), timetable.getEndTime())) {
                // 判断地点是否被重复安排了时间
                throw new ServiceException("此地点已经安排了重合了的培训时间");
            }
        }
        timetable.setPePlace(pePlace);
        timetable.setClassCourse(classCourse);
        timetable.setCreateBy(user);
        timetable.setCreateTime(new Date());
        timetable.setArrangePlaceTime(new Date());
        timetable.setArrangePlaceUser(user);
        this.myGeneralDao.save(timetable);
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "classId";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "classCourse.peClass.id";
    }

}
