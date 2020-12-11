package com.whaty.products.controller.training;

import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.exception.ErrorCodeEnum;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.constant.TrainingConstant;
import com.whaty.products.service.training.impl.CooperationTrainingItemApplyServiceImpl;
import com.whaty.util.CommonUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.whaty.constant.SqlRecordConstants.TRAINING_ITEM_BASIC_SQL;

/**
 * 合作培训项目申报
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/training/cooperationTrainingItemApply")
@BasicOperateRecord("委托培训项目")
@SqlRecord(namespace = "cooperationTrainingItem", sql = TRAINING_ITEM_BASIC_SQL)
public class CooperationTrainingItemApplyController extends TycjGridBaseControllerAdapter<TrainingItem> {

    @Resource(name = "cooperationTrainingItemApplyService")
    private CooperationTrainingItemApplyServiceImpl cooperationTrainingItemApplyService;

    /**
     * 获取培训内容
     * @param extendId
     * @return
     */
    @RequestMapping("/getTrainingContent")
    public ResultDataModel getTrainingContent(@RequestParam(TrainingConstant.PARAM_EXTEND_ID) String extendId) {
        return ResultDataModel.handleSuccessResult(this.cooperationTrainingItemApplyService.getTrainingContent(extendId));
    }

    /**
     * 设置报名截止时间
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/setEnrollEndTime")
    public ResultDataModel setEnrollEndTime(@RequestBody ParamsDataModel paramsDataModel) {
        Date enrollEndTime = CommonUtils.convertUTCDate(paramsDataModel
                .getStringParameter(TrainingConstant.PARAM_ENROLL_END_TIME));
        this.cooperationTrainingItemApplyService.doSetEnrollEndTime(this.getIds(paramsDataModel), enrollEndTime);
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 获取项目基本信息
     *
     * @param ids
     * @return
     */
    @GetMapping("/trainingItemBaseInfo")
    public ResultDataModel getTrainingItemBaseInfo(@RequestParam("ids") String ids) {
        return ResultDataModel.handleSuccessResult(this.cooperationTrainingItemApplyService
                .getTrainingItemBaseInfo(ids));
    }

    /**
     * 通过流程创建数据
     *
     * @param paramsData
     * @return
     * @throws EntityException
     */
    @PostMapping("/createByFlow")
    public ResultDataModel createByFlow(@RequestBody ParamsDataModel<TrainingItem> paramsData) throws ServiceException {
        this.initParams(paramsData);
        GridConfig gridConfig = this.initGrid(paramsData);
        return ResultDataModel.handleSuccessResult(Collections.singletonMap("id",
                this.cooperationTrainingItemApplyService.createByFlow(paramsData.getBean(),
                        paramsData.getParams(), gridConfig)));
    }

    /**
     * 更新基础信息
     *
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/updateBasicInfo")
    public ResultDataModel updateBasicInfo(@RequestBody ParamsDataModel<TrainingItem> paramsDataModel) {
        this.initParams(paramsDataModel);
        GridConfig gridConfig = this.initGrid(paramsDataModel);
        Map target = this.cooperationTrainingItemApplyService
                .updateBasicInfo(paramsDataModel.getBean(), gridConfig);
        return !"false".equals((target.get("success")))
                && Boolean.valueOf(String.valueOf((target.get("success")))).booleanValue() ?
                ResultDataModel.handleSuccessResult(String.valueOf(target.get("info"))) :
                ResultDataModel.handleFailureResult(ErrorCodeEnum.SYS_COMMON_CUSTOM_MSG.getCode(),
                        String.valueOf(target.get("info")));
    }

    /**
     * 更改费用预算
     *
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/updateFee")
    public ResultDataModel updateFee(@RequestBody ParamsDataModel<TrainingItem> paramsDataModel) {
        this.cooperationTrainingItemApplyService.updateFee(paramsDataModel.getBean());
        return ResultDataModel.handleSuccessResult();
    }

    @Override
    public GridService<TrainingItem> getGridService() {
        return cooperationTrainingItemApplyService;
    }
}
