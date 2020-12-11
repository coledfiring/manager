package com.whaty.products.controller.clazz;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.LiveCourse;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.clazz.impl.LiveCourseManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.LIVE_COURSE_BASIC_SQL;

/**
 * 直播课程管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/liveCourseManage")
@BasicOperateRecord("直播课程")
@SqlRecord(namespace = "liveCourse", sql = LIVE_COURSE_BASIC_SQL)
public class LiveCourseManageController extends TycjGridBaseControllerAdapter<LiveCourse> {

    @Resource(name = "liveCourseManageService")
    private LiveCourseManageServiceImpl liveCourseManageService;

    @Override
    public GridService<LiveCourse> getGridService() {
        return this.liveCourseManageService;
    }
}
