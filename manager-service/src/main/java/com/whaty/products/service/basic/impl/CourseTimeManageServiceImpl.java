package com.whaty.products.service.basic.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.CourseTime;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 课程时间段管理
 *
 * @author weipengsen
 */
@Lazy
@Service("courseTimeManageService")
public class CourseTimeManageServiceImpl extends TycjGridServiceAdapter<CourseTime> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(CourseTime bean, Map<String, Object> params) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
    }

    @Override
    public void checkBeforeUpdate(CourseTime bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setUpdateBy(UserUtils.getCurrentUser());
        bean.setUpdateDate(new Date());
    }

    private void checkBeforeAddOrUpdate(CourseTime bean) throws EntityException {
        if (bean.getStartTime().compareTo(bean.getEndTime()) > 0) {
            throw new EntityException("开始时间必须在结束时间之前");
        }
        List<Map<String, Object>> timeMap = this.myGeneralDao
                .getMapBySQL("select start_time as startTime, end_time as endTime from course_time " +
                        "where site_code = ?" + (bean.getId() == null ? "" : " and id <> '" + bean.getId() + "'"),
                        MasterSlaveRoutingDataSource.getDbType());
        if (predicateTimeAreaCoincide(timeMap, bean.getStartTime(), bean.getEndTime())) {
            throw new EntityException("存在冲突的时间");
        }
    }

    /**
     * 断言时间区域是否重合
     * @param timeMap
     * @param startTime
     * @param endTime
     * @return
     */
    private static boolean predicateTimeAreaCoincide(List<Map<String, Object>> timeMap, String startTime,
                                                    String endTime) {
        return timeMap.stream().anyMatch(e -> {
            String startTime2 = (String) e.get("startTime");
            String endTime2 = (String) e.get("endTime");
            boolean startInArea = after(startTime, startTime2) && before(startTime, endTime2);
            boolean endInArea = after(endTime, startTime2) && before(endTime, endTime2);
            boolean areaInclude = !after(startTime, startTime2) && !before(endTime, endTime2);
            return startInArea || endInArea || areaInclude;
        });
    }

    /**
     * 时间比较
     * @param time1
     * @param time2
     * @return
     */
    private static boolean after(String time1, String time2) {
        return time1.compareTo(time2) > 0;
    }

    /**
     * 时间比较
     * @param time1
     * @param time2
     * @return
     */
    private static boolean before(String time1, String time2) {
        return time2.compareTo(time1) > 0;
    }
}
