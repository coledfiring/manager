package com.whaty.products.controller.enroll;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.ExamCourse;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.enroll.impl.ExamCourseServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 考试科目controller
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/examCourse")
public class ExamCourseController extends TycjGridBaseControllerAdapter<ExamCourse> {

    @Resource(name = "examCourseService")
    private ExamCourseServiceImpl examCourseService;

    @Override
    public GridService<ExamCourse> getGridService() {
        return this.examCourseService;
    }
}
