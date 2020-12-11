package com.whaty.products.controller.clazz;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.ClassCourseTimetable;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.clazz.impl.ArrangeClassCourseTimetableManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 课程表地点安排
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/arrangeClassCourseTimetableManage")
public class ArrangeClassCourseTimetableManageController extends TycjGridBaseControllerAdapter<ClassCourseTimetable> {

    @Resource(name = "arrangeClassCourseTimetableManageService")
    private ArrangeClassCourseTimetableManageServiceImpl arrangeClassCourseTimetableManageService;

    /**
     * 设置培训地点
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/setTrainingPlace")
    public ResultDataModel setTrainingPlace(@RequestBody ParamsDataModel paramsDataModel) {
        String placeId = paramsDataModel.getStringParameter("placeId");
        this.arrangeClassCourseTimetableManageService.doSetTrainingPlace(this.getIds(paramsDataModel), placeId);
        return ResultDataModel.handleSuccessResult("安排成功");
    }

    @Override
    public GridService<ClassCourseTimetable> getGridService() {
        return this.arrangeClassCourseTimetableManageService;
    }
}
