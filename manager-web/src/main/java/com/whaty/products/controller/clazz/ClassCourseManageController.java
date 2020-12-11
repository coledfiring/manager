package com.whaty.products.controller.clazz;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.ClassCourse;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.clazz.impl.ClassCourseManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.CLASS_COURSE_BASIC_SQL;

/**
 * 班级课程管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/classCourseManage")
@BasicOperateRecord("班级课程")
@SqlRecord(namespace = "classCourse", sql = CLASS_COURSE_BASIC_SQL)
public class ClassCourseManageController extends TycjGridBaseControllerAdapter<ClassCourse> {

    @Resource(name = "classCourseManageService")
    private ClassCourseManageServiceImpl classCourseManageService;

    @Override
    public GridService<ClassCourse> getGridService() {
        return this.classCourseManageService;
    }
}
