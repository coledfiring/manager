package com.whaty.products.controller.enroll;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.ExamBatch;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.enroll.impl.ExamBatchServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 考点管理controller
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/examBatch")
public class ExamBatchController extends TycjGridBaseControllerAdapter<ExamBatch> {

    @Resource(name = "examBatchService")
    private ExamBatchServiceImpl examBatchService;

    /**
     * 激活批次
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/activeExamBatch")
    @OperateRecord(value = "激活批次", moduleCode = OperateRecordModuleConstant.ENROLL_EXAM_CODE)
    public ResultDataModel activeExamBatch(@RequestBody ParamsDataModel paramsDataModel) {
        this.examBatchService.doActiveExamBatch(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("激活批次成功!");
    }

    @Override
    public GridService<ExamBatch> getGridService() {
        return this.examBatchService;
    }
}
