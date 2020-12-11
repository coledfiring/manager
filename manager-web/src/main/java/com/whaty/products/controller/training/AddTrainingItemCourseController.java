package com.whaty.products.controller.training;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeCourse;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.impl.AddTrainingItemCourseServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 从课程库添加课程
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/training/addTrainingItemCourse")
public class AddTrainingItemCourseController extends TycjGridBaseControllerAdapter<PeCourse> {

    @Resource(name = "addTrainingItemCourseService")
    private AddTrainingItemCourseServiceImpl addTrainingItemCourseService;

    /**
     * 添加到培训项目
     * @return
     */
    @RequestMapping("/addToTrainingItem")
    public ResultDataModel addToTrainingItem(@RequestBody ParamsDataModel paramsDataModel) {
        String itemId = paramsDataModel.getStringParameter(CommonConstant.PARAM_PARENT_ID);
        int count = this.addTrainingItemCourseService.addToTrainingItem(this.getIds(paramsDataModel), itemId);
        return ResultDataModel.handleSuccessResult("添加成功，共添加" + count + "条数据");
    }

    @Override
    public GridService<PeCourse> getGridService() {
        return this.addTrainingItemCourseService;
    }
}
