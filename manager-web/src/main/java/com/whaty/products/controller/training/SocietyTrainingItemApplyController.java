package com.whaty.products.controller.training;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
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
import com.whaty.products.service.common.constant.ComConstant;
import com.whaty.products.service.training.constant.TrainingConstant;
import com.whaty.products.service.training.impl.SocietyTrainingItemApplyServiceImpl;
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
 * 社会培训项目申报
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/training/societyTrainingItemApply")
@BasicOperateRecord("社会培训项目")
@SqlRecord(namespace = "societyTrainingItem", sql = TRAINING_ITEM_BASIC_SQL)
public class SocietyTrainingItemApplyController extends TycjGridBaseControllerAdapter<TrainingItem> {

    @Resource(name = "societyTrainingItemApplyService")
    private SocietyTrainingItemApplyServiceImpl societyTrainingItemApplyService;

    /**
     * 获取培训内容
     * @param extendId
     * @return
     */
    @RequestMapping("/getTrainingContent")
    public ResultDataModel getTrainingContent(@RequestParam(TrainingConstant.PARAM_EXTEND_ID) String extendId) {
        return ResultDataModel.handleSuccessResult(this.societyTrainingItemApplyService.getTrainingContent(extendId));
    }

    /**
     * 保存培训内容
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/saveTrainingContent")
    public ResultDataModel saveTrainingContent(@RequestBody ParamsDataModel paramsDataModel) {
        String id = this.getIds(paramsDataModel);
        String content = paramsDataModel.getStringParameter(TrainingConstant.PARAM_CONTENT);
        String code = paramsDataModel.getStringParameter(TrainingConstant.PARAM_EXTEND_TYPE_CODE);
        String namespace = paramsDataModel.getStringParameter(ComConstant.PARAM_NAMESPACE);
        this.societyTrainingItemApplyService.saveTrainingContent(id, content, code, namespace);
        return ResultDataModel.handleSuccessResult("保存成功");
    }

    /**
     * 获取培训内容
     * @param itemId
     * @param code
     * @return
     */
    @RequestMapping("/getTrainingContentByItemId")
    public ResultDataModel getTrainingContentByItemId(@RequestParam(TrainingConstant.PARAM_ITEM_ID) String itemId,
                                              @RequestParam(TrainingConstant.PARAM_EXTEND_TYPE_CODE) String code) {
        return ResultDataModel.handleSuccessResult(this.societyTrainingItemApplyService.getTrainingContentByItemId(itemId, code));
    }

    /**
     * 获取委托项目联系人
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/getItemLinkman")
    public ResultDataModel getItemLinkman(@RequestBody ParamsDataModel paramsDataModel) {
        return ResultDataModel.handleSuccessResult(this.societyTrainingItemApplyService
                .getItemLinkman(this.getIds(paramsDataModel)));
    }

    /**
     * 设置委托项目联系人
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/setItemLinkman")
    public ResultDataModel getItemLinkMan(@RequestBody ParamsDataModel paramsDataModel) {
        this.societyTrainingItemApplyService.doSetItemLinkman(this.getIds(paramsDataModel),
                paramsDataModel.getStringParameter(TrainingConstant.PARAM_LINKMAN_ID));
        return ResultDataModel.handleSuccessResult();
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
        this.societyTrainingItemApplyService.doSetEnrollEndTime(this.getIds(paramsDataModel), enrollEndTime);
        return ResultDataModel.handleSuccessResult();
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
                this.societyTrainingItemApplyService.createByFlow(paramsData.getBean(),
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
        Map target = this.societyTrainingItemApplyService
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
        this.societyTrainingItemApplyService.updateFee(paramsDataModel.getBean());
        return ResultDataModel.handleSuccessResult();
    }

    /**
     * 获取培训扩展内容项
     * @return
     */
    @GetMapping("/itemExtendType")
    public ResultDataModel getItemExtendType() {
        return ResultDataModel.handleSuccessResult(this.societyTrainingItemApplyService.listItemExtendType());
    }

    /**
     * 列举报名规则
     * @return
     */
    @RequestMapping("/enrollRegulation")
    public ResultDataModel listEnrollRegulation() {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return ResultDataModel.handleSuccessResult(this.societyTrainingItemApplyService.listEnrollRegulation());
    }

    /**
     * 设置报名规则
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/setEnrollRegulation")
    public ResultDataModel setEnrollRegulation(@RequestBody ParamsDataModel paramsDataModel) {
        String regulationId = paramsDataModel.getStringParameter("regulationId");
        int count = this.societyTrainingItemApplyService
                .doSetEnrollRegulation(this.getIds(paramsDataModel), regulationId);
        return ResultDataModel.handleSuccessResult("设置规则成功，共成功" + count + "条数据");
    }

    /**
     * 设置是否需要报名审核
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/setEnrollNeedCheck")
    public ResultDataModel setEnrollNeedCheck(@RequestBody ParamsDataModel paramsDataModel) {
        String isNeedCheckId = paramsDataModel.getStringParameter("isNeedCheckId");
        int count = this.societyTrainingItemApplyService
                .doSetEnrollNeedCheck(this.getIds(paramsDataModel), isNeedCheckId);
        return ResultDataModel.handleSuccessResult("设置成功，共成功" + count + "条数据");
    }

    @Override
    public GridService<TrainingItem> getGridService() {
        return this.societyTrainingItemApplyService;
    }
}
