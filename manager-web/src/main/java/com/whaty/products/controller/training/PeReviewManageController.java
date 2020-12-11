package com.whaty.products.controller.training;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeReview;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.impl.PeReviewManageServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.REVIEW_ITEM_MANAGE_INFO;
import static com.whaty.constant.SqlRecordConstants.REVIEW_ADD_OR_UPDATE;


/**
 * 项目评审管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/training/peReviewManage")
@BasicOperateRecord("项目评审")
@SqlRecord(namespace = "itemReview", sql = REVIEW_ITEM_MANAGE_INFO)
public class PeReviewManageController extends TycjGridBaseControllerAdapter<PeReview> {

    @Resource(name = "peReviewManageService")
    private PeReviewManageServiceImpl peReviewManageService;

    @Override
    public GridService<PeReview> getGridService() {
        return this.peReviewManageService;
    }

    /**
     * 获取选择数据
     *
     * @return
     */
    @RequestMapping("/managers")
    public ResultDataModel getOptions() {
        return ResultDataModel.handleSuccessResult(peReviewManageService.listManager());
    }

    /**
     * 添加或修改评审
     *
     * @param peReview
     * @return
     */
    @PostMapping("/review")
    @OperateRecord("项目评审添加或修改")
    @SqlRecord(namespace = "itemReview", sql = REVIEW_ADD_OR_UPDATE)
    public ResultDataModel addOrUpdateReceived(@RequestBody PeReview peReview) {
        if(StringUtils.isBlank(peReview.getId())) {
            peReviewManageService.addReview(peReview);
        } else {
            peReviewManageService.updateReview(peReview);
        }
        return ResultDataModel.handleSuccessResult("保存成功");
    }

    /**
     * 获取接待数据用于回显
     *
     * @param id
     * @return
     */
    @GetMapping("/reviewData/{id}")
    public ResultDataModel getReviewData(@PathVariable String id) {
        return ResultDataModel.handleSuccessResult(peReviewManageService.getReviewData(id));
    }

    /**
     * 设置评审状态
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/setProjectStatus")
    @OperateRecord(value = "设置评审状态", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "itemReview", sql = REVIEW_ITEM_MANAGE_INFO)
    public ResultDataModel setProjectStatus(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        peReviewManageService.setProjectStatus(ids, paramsDataModel.getStringParameter("projectStatus"));
        return ResultDataModel.handleSuccessResult("操作成功！");
    }
}
