package com.whaty.products.service.clazz.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeClass;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.products.service.clazz.utils.ClazzUtils;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.whaty.products.service.clazz.constants.ClazzConstants.PLACE_ARRANGE_LOCK;

/**
 * 培训地点安排
 *
 * @author weipengsen
 */
@Lazy
@Service("classTrainingPlaceManageService")
public class ClassTrainingPlaceManageServiceImpl extends TycjGridServiceAdapter<PeClass> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public Map delete(GridConfig gridConfig, String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE class_course_timetable cct                            ");
        sql.append(" INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id ");
        sql.append(" SET cct.arrange_place_time = NULL,                           ");
        sql.append("  cct.arrange_place_user = NULL,                              ");
        sql.append("  cct.fk_place_id = NULL                                      ");
        sql.append(" WHERE                                                        ");
        sql.append(CommonUtils.madeSqlIn(ids, "cc.fk_class_id"));
        this.myGeneralDao.executeBySQL(sql.toString());
        this.myGeneralDao.executeBySQL("update pe_class set flag_place_type = null, place_note = null," +
                " apply_time = null, apply_user = null where " + CommonUtils.madeSqlIn(ids, "id"));
        return com.whaty.core.commons.util.CommonUtils.createSuccessInfoMap("删除成功");
    }

    /**
     * 申请培训地点
     * @param classId
     * @param placeType
     * @param placeNote
     */
    public void doApplyTrainingPlace(String classId, String placeType, String placeNote) {
        TycjParameterAssert.isAllNotBlank(classId, placeType);
        if (!this.myGeneralDao
                .checkNotEmpty("select 1 from enum_const where namespace = 'flagPlaceType' and id = ?", placeType)) {
            throw new ParameterIllegalException();
        }
        if (this.myGeneralDao
                .checkNotEmpty("select 1 from pe_class where id = ? and flag_place_type is not null", classId)) {
            throw new ServiceException("班级已申请过培训地点");
        }
        if (!this.myGeneralDao.checkNotEmpty("select 1 from class_course_timetable cct " +
                        "inner join class_course cc on cc.id = cct.fk_class_course_id where cc.fk_class_id = ?",
                        classId)) {
            throw new ServiceException("班级没有设置课程表");
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from class_course_timetable cct " +
                "inner join class_course cc on cc.id = cct.fk_class_course_id where cc.fk_class_id = ? " +
                "and ifnull(cct.training_date, '') = ''", classId)) {
            throw new ServiceException("存在没有设置时间的课程表");
        }
        this.myGeneralDao.executeBySQL("update pe_class set flag_place_type = ?, place_note = ?, apply_time = now()," +
                " apply_user = ? WHERE id = ?", placeType, placeNote, UserUtils.getCurrentUserId(), classId);
    }

    /**
     * 设置培训地点
     * @param ids
     * @param placeId
     */
    public void doSetTrainingPlace(String ids, String placeId) {
        this.doSetTrainingPlace(ids, placeId, null);
    }

    /**
     * 设置培训地点
     * @param ids
     * @param placeId
     * @param color
     */
    public void doSetTrainingPlace(String ids, String placeId, String color) {
        TycjParameterAssert.isAllNotBlank(ids, placeId);
        if (this.myGeneralDao
                .checkNotEmpty("select 1 from pe_class c left join enum_const pt on pt.id = c.flag_place_type " +
                        "where c.id = ? and ifnull(pt.code, '2') = '2'", ids)) {
            throw new ServiceException("不能操作培训地点类型为流动的班级");
        }
        try {
            PLACE_ARRANGE_LOCK.lock(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new UncheckException(e);
        }
        try {
            // 这个班所有课程时间区域
            List<Map<String, Object>> timeAreas = this.myGeneralDao
                    .getMapBySQL("SELECT date_format(cct.training_date, '%Y-%m-%d') as date, ct.id as time " +
                            "FROM class_course_timetable cct " +
                            "INNER JOIN course_time ct on ct.id = cct.fk_course_time_id " +
                            "INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id WHERE cc.fk_class_id = ?", ids);
            // 地点所有安排好的时间区域
            List<Map<String, Object>> timeMap = this.myGeneralDao
                    .getMapBySQL("select date_format(training_date, '%Y-%m-%d') as date, fk_course_time_id as time" +
                            " from class_course_timetable where fk_place_id = ?", placeId);
            List<Map<String, Object>> temporaryMap = this.myGeneralDao
                    .getMapBySQL("select date_format(use_date, '%Y-%m-%d') as date, fk_course_time_id as time" +
                            " from temporary_use_place where fk_place_id = ?", placeId);
            if (CollectionUtils.isNotEmpty(temporaryMap)) {
                timeMap.addAll(temporaryMap);
            }
            // 判断地点是否会被重合时间分配
            for (Map<String, Object> timeArea : timeAreas) {
                if (ClazzUtils.predicateTimeAreaCoincide(timeMap, (String) timeArea.get("date"),
                        (String) timeArea.get("time"))) {
                    throw new ServiceException("此地点已经安排了重合了的培训时间");
                }
            }
            System.out.println(UserUtils.getCurrentUserId());
            this.myGeneralDao.executeBySQL("update class_course_timetable cct " +
                            " INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id" +
                            " INNER JOIN pe_course pc ON pc.id = cc.fk_course_id" +
                            " INNER JOIN enum_const ec ON ec.ID = pc.flag_course_type" +
                            " SET cct.arrange_place_time = now()," +
                            " cct.arrange_place_user = ?, cct.fk_place_id = ?, " +
                            " cct.color = ? where cc.fk_class_id = ?  AND ec.name = '理论课'",
                    UserUtils.getCurrentUserId(), placeId, color, ids);
        } finally {
            PLACE_ARRANGE_LOCK.unlock();
        }
    }

    /**
     * 获取有效的培训地点
     */
    public List listValidTrainingPlace() {
        return myGeneralDao.getBySQL(ScopeHandleUtils
                .handleScopeSignOfSql("SELECT place.id AS id, place. NAME AS name FROM pe_place place " +
                "INNER JOIN enum_const ec ON ec.id = place.flag_is_valid " +
                "WHERE place.site_code = ? AND ec.code = '1'",
                UserUtils.getCurrentUserId()), SiteUtil.getSiteCode());
    }
}
