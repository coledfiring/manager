package com.whaty.products.service.clazz.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.ClassCourseTimetable;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.products.service.clazz.utils.ClazzUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.whaty.products.service.clazz.constants.ClazzConstants.PLACE_ARRANGE_LOCK;

/**
 * 课程表地点安排
 *
 * @author weipengsen
 */
@Lazy
@Service("arrangeClassCourseTimetableManageService")
public class ArrangeClassCourseTimetableManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<ClassCourseTimetable> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 安排培训地点
     * @param ids
     * @param placeId
     */
    public void doSetTrainingPlace(String ids, String placeId) {
        Arrays.asList(ids.split(CommonConstant.SPLIT_ID_SIGN))
                .forEach(id -> this.doSetTrainingPlace(id, placeId, null));
    }

    /**
     * 安排培训地点
     * @param ids
     * @param placeId
     * @param color
     */
    public void doSetTrainingPlace(String ids, String placeId, String color) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                       ");
        sql.append(" 	1                                                         ");
        sql.append(" FROM                                                         ");
        sql.append(" 	class_course_timetable cct                                ");
        sql.append(" INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id ");
        sql.append(" INNER JOIN pe_class c on c.id = cc.fk_class_id               ");
        sql.append(" WHERE                                                        ");
        sql.append("   cct.id = ?                                                 ");
        sql.append(" and cct.start_time is null                                   ");
        if (this.myGeneralDao.checkNotEmpty(sql.toString(), ids)) {
            throw new ServiceException("不能操作没有安排时间的班级");
        }
        try {
            PLACE_ARRANGE_LOCK.lock(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new UncheckException(e);
        }
        try {
            if (StringUtils.isBlank(placeId)) {
                //地点为空不校验
                placeId = null;
            } else {
                //校验课程表时间
                this.checkClassCourseTimetable(placeId, ids);
            }
            this.myGeneralDao.executeBySQL("update class_course_timetable cct " +
                            " INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id" +
                            " INNER JOIN pe_course pc ON pc.id = cc.fk_course_id" +
                            " INNER JOIN enum_const ec ON ec.ID = pc.flag_course_type" +
                            " SET cct.arrange_place_time = now()," +
                            " cct.arrange_place_user = ?, cct.fk_place_id = ?, " +
                            " cct.color = ? where cct.id = ?",
                    UserUtils.getCurrentUserId(), placeId, color, ids);
        } finally {
            PLACE_ARRANGE_LOCK.unlock();
        }
    }

    /**
     * 校验课程表时间是否冲突
     *
     * @param placeId
     * @param ids
     */
    private void checkClassCourseTimetable(String placeId, String ids) {
        StringBuilder sql = new StringBuilder();
        // 地点已安排的时间区域
        List<Map<String, Object>> arrangeCourseTime = this.myGeneralDao
                .getMapBySQL("select date_format(training_date, '%Y-%m-%d') as date, fk_course_time_id as time" +
                        " from class_course_timetable where fk_place_id = ?", placeId);
        List<Map<String, Object>> temporaryMap = this.myGeneralDao
                .getMapBySQL("select date_format(use_date, '%Y-%m-%d') as date, fk_course_time_id as time" +
                        " from temporary_use_place where fk_place_id = ?", placeId);
        if (CollectionUtils.isNotEmpty(temporaryMap)) {
            arrangeCourseTime.addAll(temporaryMap);
        }
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                                              ");
        sql.append(" 	1                                                                                ");
        sql.append(" FROM                                                                                ");
        sql.append(" 	class_course_timetable cct                                                       ");
        sql.append(" INNER JOIN class_course_timetable cct1 on cct1.id = ?                               ");
        sql.append(" INNER JOIN pe_place pp ON cct.fk_place_id = pp.id                                   ");
        sql.append(" WHERE                                                                               ");
        sql.append("   (                                                                                 ");
        sql.append("     (cct.start_time < cct1.start_time AND cct.end_time > cct1.end_time) or          ");
        sql.append("     (cct.start_time <= cct1.start_time AND cct.end_time >= cct1.end_time) or        ");
        sql.append("     (cct.end_time > cct1.start_time AND cct.end_time < cct1.end_time)               ");
        sql.append("   )                                                                                 ");
        sql.append(" and cct.id <> cct1.id                                                               ");
        sql.append(" and cct.training_date = cct1.training_date                                          ");
        sql.append(" and pp.id = ?                                                                       ");
        // 判断地点是否被重复安排了时间
        if (this.myGeneralDao.checkNotEmpty(sql.toString(), ids, placeId)) {
            throw new ServiceException("此地点已经安排了重合了的培训时间");
        }
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "classId";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return null;
    }
}
