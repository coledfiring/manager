package com.whaty.products.controller.basic;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.CourseTime;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.CourseTimeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.COURSE_TIME_BASIC_SQL;

/**
 * 班级时间段
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/courseTimeManage")
@BasicOperateRecord("时间段")
@SqlRecord(namespace = "courseTime", sql = COURSE_TIME_BASIC_SQL)
public class CourseTimeManageController extends TycjGridBaseControllerAdapter<CourseTime> {

    @Resource(name = "courseTimeManageService")
    private CourseTimeManageServiceImpl courseTimeManageService;

    @Override
    public GridService<CourseTime> getGridService() {
        return this.courseTimeManageService;
    }
}
