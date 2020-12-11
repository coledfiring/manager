package com.whaty.products.controller.training;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.TrainingItemCourse;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.impl.TrainingItemCourseManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 培训项目课程管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/training/trainingItemCourseManage")
public class TrainingItemCourseManageController extends TycjGridBaseControllerAdapter<TrainingItemCourse> {

    @Resource(name = "trainingItemCourseManageService")
    private TrainingItemCourseManageServiceImpl trainingItemCourseManageService;

    /**
     * 未添加课程
     *
     * @return
     */
    @RequestMapping("/getNotAddedCourse")
    public ResultDataModel getNotAddedCourseList(@RequestBody ParamsDataModel paramsDataModel) {
        String searchName = paramsDataModel.getStringParameter("searchName");
        String searchTarget = paramsDataModel.getStringParameter("searchTarget");
        String searchTeacher = paramsDataModel.getStringParameter("searchTeacher");
        String parentId = paramsDataModel.getStringParameter("parentId");
        Map<String, Object> page = (Map<String, Object>) paramsDataModel.getParameter("page");
        return ResultDataModel.handleSuccessResult(this.trainingItemCourseManageService
                .getNotAddedCourseList(searchName, searchTarget, searchTeacher, parentId, page));
    }

    /**
     * 已添加课程
     *
     * @return
     */
    @GetMapping("/addedCourse")
    public ResultDataModel getAddedCourseList(@RequestParam(CommonConstant.PARAM_PARENT_ID) String parentId) {
        return ResultDataModel.handleSuccessResult(this.trainingItemCourseManageService.getAddedCourseList(parentId));
    }

    /**
     * 添加课程
     *
     * @return
     */
    @PostMapping("/addCourse")
    public ResultDataModel addedCourse(@RequestBody ParamsDataModel paramsDataModel) {
        String courseId = paramsDataModel.getStringParameter("courseId");
        String parentId = paramsDataModel.getStringParameter(CommonConstant.PARAM_PARENT_ID);
        this.trainingItemCourseManageService.doAddCourse(courseId, parentId);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 添加课程
     *
     * @return
     */
    @PostMapping("/removeCourse")
    public ResultDataModel removeCourse(@RequestBody ParamsDataModel paramsDataModel) {
        String id = paramsDataModel.getStringParameter("id");
        this.trainingItemCourseManageService.doRemoveCourse(id);
        return ResultDataModel.handleSuccessResult();
    }

    @Override
    public GridService<TrainingItemCourse> getGridService() {
        return this.trainingItemCourseManageService;
    }
}
