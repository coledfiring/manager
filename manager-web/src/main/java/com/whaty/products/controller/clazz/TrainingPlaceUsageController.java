package com.whaty.products.controller.clazz;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.products.service.clazz.TrainingPlaceUsageService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 培训地点使用一览
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/trainingPlaceUsage")
public class TrainingPlaceUsageController {

    @Resource(name = "trainingPlaceUsageService")
    private TrainingPlaceUsageService trainingPlaceUsageService;

    /**
     * 列举使用情况
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/usageInfo")
    public ResultDataModel listUsageInfo(@RequestBody ParamsDataModel paramsDataModel) {
        String unit = paramsDataModel.getStringParameter("unit");
        String place = paramsDataModel.getStringParameter("place");
        List<String> timeArea = (List<String>) paramsDataModel.getParameter("timeArea");
        String capacity = paramsDataModel.getStringParameter("capacity");
        String feeLevel = paramsDataModel.getStringParameter("feeLevel");
        return ResultDataModel.handleSuccessResult(this.trainingPlaceUsageService
                .listUsageInfo(unit, place, timeArea, capacity, feeLevel));
    }

    /**
     * 列举班级
     * @return
     */
    @GetMapping("/class")
    public ResultDataModel listClass(@RequestParam("type") String type) {
        return ResultDataModel.handleSuccessResult(this.trainingPlaceUsageService.listClass(type));
    }

    /**
     * 列举可以删除的班级
     * @return
     */
    @GetMapping("/canDeleteClass")
    public ResultDataModel listCanDeleteClass() {
        return ResultDataModel.handleSuccessResult(this.trainingPlaceUsageService.listCanDeleteClass());
    }

    /**
     * 列举可用的地点
     * @param classId
     * @return
     */
    @GetMapping("/fixedPlace")
    public ResultDataModel listFixedPlace(@RequestParam("classId") String classId) {
        return ResultDataModel.handleSuccessResult(this.trainingPlaceUsageService.listFixedPlace(classId));
    }

    /**
     * 列举课程表
     * @param classId
     * @return
     */
    @GetMapping("/time")
    public ResultDataModel listTime(@RequestParam("classId") String classId) {
        return ResultDataModel.handleSuccessResult(this.trainingPlaceUsageService.listTime(classId));
    }

    /**
     * 列举课程表
     * @param timeId
     * @return
     */
    @GetMapping("/flowPlace")
    public ResultDataModel listFlowPlace(@RequestParam("timeId") String timeId) {
        return ResultDataModel.handleSuccessResult(this.trainingPlaceUsageService.listFlowPlace(timeId));
    }

    /**
     * 列举临时可用地点
     * @param timeId
     * @return
     */
    @GetMapping("/temporaryPlace")
    public ResultDataModel listTemporaryPlace(@RequestParam("useDate") String useDate,
                                              @RequestParam("timeId") String timeId) {
        return ResultDataModel.handleSuccessResult(this.trainingPlaceUsageService.listTemporaryPlace(useDate, timeId));
    }

    /**
     * 安排固定地点
     * @return
     */
    @PostMapping("/arrangeFixed")
    public ResultDataModel arrangeFixed(@RequestBody ParamsDataModel paramsDataModel) {
        String classId = paramsDataModel.getStringParameter("classId");
        String placeId = paramsDataModel.getStringParameter("placeId");
        String color = paramsDataModel.getStringParameter("color");
        this.trainingPlaceUsageService.doArrangeFixed(classId, placeId, color);
        return ResultDataModel.handleSuccessResult("安排成功");
    }

    /**
     * 安排流动地点
     * @return
     */
    @PostMapping("/arrangeFlow")
    public ResultDataModel arrangeFlow(@RequestBody ParamsDataModel paramsDataModel) {
        String placeId = paramsDataModel.getStringParameter("placeId");
        List<String> time = (List<String>) paramsDataModel.getParameter("time");
        String color = paramsDataModel.getStringParameter("color");
        this.trainingPlaceUsageService.doArrangeFlow(time, placeId, color);
        return ResultDataModel.handleSuccessResult("安排成功");
    }

    /**
     * 安排临时借用
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/arrangeTemporary")
    @OperateRecord(value = "安排临时借用地点", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE, isImportant = true)
    public ResultDataModel arrangeTemporary(@RequestBody ParamsDataModel paramsDataModel) {
        String useDate = paramsDataModel.getStringParameter("useDate");
        List<String> time = (List<String>) paramsDataModel.getParameter("time");
        String placeId = paramsDataModel.getStringParameter("placeId");
        String application = paramsDataModel.getStringParameter("application");
        String color = paramsDataModel.getStringParameter("color");
        this.trainingPlaceUsageService.doArrangeTemporary(useDate, time, placeId, application, color);
        return ResultDataModel.handleSuccessResult("安排成功");
    }

    /**
     * 删除安排
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/deleteArrange")
    public ResultDataModel deleteArrange(@RequestBody ParamsDataModel paramsDataModel) {
        String id = paramsDataModel.getStringParameter("id");
        String type = paramsDataModel.getStringParameter("type");
        this.trainingPlaceUsageService.deleteArrange(id, type);
        return ResultDataModel.handleSuccessResult("删除成功");
    }

    /**
     * 删除班级地点安排
     * @return
     */
    @PostMapping("/deleteClassArrange")
    public ResultDataModel deleteClassArrange(@RequestBody ParamsDataModel paramsDataModel) {
        String classId = paramsDataModel.getStringParameter("classId");
        this.trainingPlaceUsageService.deleteClassArrange(classId);
        return ResultDataModel.handleSuccessResult("删除成功");
    }

}
