package com.whaty.products.controller.clazz;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeClass;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.clazz.impl.ClassTrainingPlaceManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.CLASS_TRAINING_PLACE_BASIC_SQL;

/**
 * 班级培训地点管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/clazz/classTrainingPlaceManage")
@BasicOperateRecord("班级培训地点")
@SqlRecord(namespace = "classTrainingPlace", sql = CLASS_TRAINING_PLACE_BASIC_SQL)
public class ClassTrainingPlaceManageController extends TycjGridBaseControllerAdapter<PeClass> {

    @Resource(name = "classTrainingPlaceManageService")
    private ClassTrainingPlaceManageServiceImpl classTrainingPlaceManageService;

    /**
     * 申请培训地点
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/applyTrainingPlace")
    public ResultDataModel applyTrainingPlace(@RequestBody ParamsDataModel paramsDataModel) {
        String classId = paramsDataModel.getStringParameter("classId");
        String placeType = paramsDataModel.getStringParameter("placeType");
        String placeNote = paramsDataModel.getStringParameter("placeNote");
        this.classTrainingPlaceManageService.doApplyTrainingPlace(classId, placeType, placeNote);
        return ResultDataModel.handleSuccessResult("申请成功");
    }

    /**
     * 设置培训地点
     * @return
     */
    @RequestMapping("/setTrainingPlace")
    public ResultDataModel setTrainingPlace(@RequestBody ParamsDataModel paramsDataModel) {
        String placeId = paramsDataModel.getStringParameter("placeId");
        this.classTrainingPlaceManageService.doSetTrainingPlace(this.getIds(paramsDataModel), placeId);
        return ResultDataModel.handleSuccessResult("安排成功");
    }
    /**
     * 获取有效的培训地点
     * @return
     */
    @RequestMapping("/validPlace")
    public ResultDataModel listValidTrainingPlace() {
        return ResultDataModel.handleSuccessResult(this.classTrainingPlaceManageService.listValidTrainingPlace());
    }

    @Override
    public GridService<PeClass> getGridService() {
        return this.classTrainingPlaceManageService;
    }
}
