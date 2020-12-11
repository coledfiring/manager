package com.whaty.products.controller.training;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.constant.TrainingConstant;
import com.whaty.products.service.training.impl.TrainingItemApportionServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.TRAINING_ITEM_APPORTION_INFO;
import static com.whaty.constant.SqlRecordConstants.TRAINING_ITEM_ARRANGE_INFO;

/**
 * 项目安排/分派管理
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/training/trainingItemApportion")
public class TrainingItemApportionController extends TycjGridBaseControllerAdapter<TrainingItem> {

    @Resource(name = "trainingItemApportionService")
    private TrainingItemApportionServiceImpl trainingItemApportionService;

    /**
     * 项目分派
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/apportionItem")
    @OperateRecord(value = "项目分派", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "apportionItem", sql = TRAINING_ITEM_APPORTION_INFO)
    public ResultDataModel apportionItem(@RequestBody ParamsDataModel paramsDataModel) {
        this.trainingItemApportionService.doApportionItem(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("分派成功");
    }

    /**
     * 项目取消分派
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/cancelApportionItem")
    @OperateRecord(value = "项目分派", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "apportionItem", sql = TRAINING_ITEM_APPORTION_INFO)
    public ResultDataModel cancelApportionItem(@RequestBody ParamsDataModel paramsDataModel) {
        this.trainingItemApportionService.doCancelApportionItem(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("取消分派成功");
    }

    /**
     * 项目申请
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/applyItem")
    public ResultDataModel applyItem(@RequestBody ParamsDataModel paramsDataModel) {
        this.trainingItemApportionService.doApplyItem(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("申请成功");
    }

    /**
     * 取消申请
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/cancelApplyItem")
    public ResultDataModel cancelApplyItem(@RequestBody ParamsDataModel paramsDataModel) {
        this.trainingItemApportionService.doCancelApplyItem(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("取消申请成功");
    }

    /**
     * 删除申请
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/deleteApplyItem")
    public ResultDataModel deleteApplyItem(@RequestBody ParamsDataModel paramsDataModel) {
        this.trainingItemApportionService.doDeleteApplyItem(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("删除申请成功");
    }

    /**
     * 获取可申请单位
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/getArrangeUnit")
    public ResultDataModel getArrangeUnit(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult(this.trainingItemApportionService
                .getArrangeUnit(this.getIds(paramsDataModel)));
    }

    /**
     * 项目安排
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/arrangeItem")
    @OperateRecord(value = "项目分派", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "arrangeItem",sql = TRAINING_ITEM_ARRANGE_INFO)
    public ResultDataModel arrangeItem(@RequestBody ParamsDataModel paramsDataModel) {
        String unitId = paramsDataModel.getStringParameter(TrainingConstant.PARAM_UNIT_ID);
        this.trainingItemApportionService.doArrangeItem(this.getIds(paramsDataModel), unitId);
        return ResultDataModel.handleSuccessResult("安排成功");
    }

    /**
     * 取消安排
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/cancelArrangeItem")
    @OperateRecord(value = "项目分派", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "arrangeItem",sql = TRAINING_ITEM_ARRANGE_INFO)
    public ResultDataModel cancelArrangeItem(@RequestBody ParamsDataModel paramsDataModel) {
        this.trainingItemApportionService.doCancelArrangeItem(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("取消安排成功");
    }

    @Override
    public GridService<TrainingItem> getGridService() {
        return this.trainingItemApportionService;
    }
}
