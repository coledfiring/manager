package com.whaty.products.controller.oltrain.train;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlCourseTeacher;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.train.impl.OLCourseTutorTeacherManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 课程授课教师管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/train/olCourseTutorTeacherManage")
@BasicOperateRecord("课程授课教师管理")
public class OLCourseTutorTeacherManageController extends TycjGridBaseControllerAdapter<OlCourseTeacher> {

    @Resource(name = "olCourseTutorTeacherManageService")
    private OLCourseTutorTeacherManageServiceImpl olCourseTutorTeacherManageService;

    @Override
    public GridService<OlCourseTeacher> getGridService() {
        return this.olCourseTutorTeacherManageService;
    }
}
