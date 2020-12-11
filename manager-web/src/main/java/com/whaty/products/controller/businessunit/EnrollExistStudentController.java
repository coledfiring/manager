package com.whaty.products.controller.businessunit;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.businessunit.EnrollExistStudentServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 已有学生报名
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/businessUnit/enrollExistStudent")
public class EnrollExistStudentController extends TycjGridBaseControllerAdapter<PeStudent> {

    @Resource(name = "enrollExistStudentService")
    private EnrollExistStudentServiceImpl enrollExistStudentService;

    /**
     * 勾选学生报名
     *
     * @return
     */
    @RequestMapping(value = "/saveExistStudentEnrollInfo")
    @OperateRecord(value = "勾选学生报名", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel saveExistStudentEnrollInfo(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        String classId = paramsDataModel.getStringParameter("parentId");
        String needRoomCode = paramsDataModel.getStringParameter("needRoomCode");
        String needFoodCode = paramsDataModel.getStringParameter("needFoodCode");
        String needMaterialCode = paramsDataModel.getStringParameter("needMaterialCode");
        return ResultDataModel.handleSuccessResult("报名成功，共操作" + enrollExistStudentService.
                saveExistStudentEnrollInfo(ids, classId, needRoomCode, needFoodCode, needMaterialCode) + "条数据");
    }

    /**
     * 勾选调整期次
     *
     * @return
     */
    @RequestMapping(value = "/updateStudentClassInfo")
    @OperateRecord(value = "勾选调整期次", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel updateStudentClassInfo(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        String classId = paramsDataModel.getStringParameter("classId");
        return ResultDataModel.handleSuccessResult("调整期次成功，共操作" +
                enrollExistStudentService.updateStudentClassInfo(ids, classId) + "条数据");
    }


    @Override
    public GridService<PeStudent> getGridService() {
        return this.enrollExistStudentService;
    }
}
