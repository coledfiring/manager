package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.CourseTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.COURSE_TYPE_BASIC_SQL;

/**
 * 课程类型管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/courseTypeManage")
@BasicOperateRecord("课程类型")
@SqlRecord(namespace = "courseType", sql = COURSE_TYPE_BASIC_SQL)
public class CourseTypeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "courseTypeManageService")
    private CourseTypeManageServiceImpl courseTypeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.courseTypeManageService;
    }
}
