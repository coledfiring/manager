package com.whaty.products.controller.oltrain.train;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlCourseTeacher;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.train.impl.OLCourseSourceTeacherManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 课程资源教师管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/train/olCourseSourceTeacherManage")
@BasicOperateRecord("课程资源教师管理")
public class OLCourseSourceTeacherManageController extends TycjGridBaseControllerAdapter<OlCourseTeacher> {

    @Resource(name = "olCourseSourceTeacherManageService")
    private OLCourseSourceTeacherManageServiceImpl olCourseSourceTeacherManageService;

    @Override
    public GridService<OlCourseTeacher> getGridService() {
        return this.olCourseSourceTeacherManageService;
    }
}
