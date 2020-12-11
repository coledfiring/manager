package com.whaty.products.controller.training;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.impl.SocietyTrainingItemApplyServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 社会培训项目查询
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/training/societyTrainingItemSearch")
public class SocietyTrainingItemSearchController extends TycjGridBaseControllerAdapter<TrainingItem> {

    @Resource(name = "societyTrainingItemApplyService")
    private SocietyTrainingItemApplyServiceImpl societyTrainingItemApplyService;

    /**
     * 获取报名二维码数据
     *
     * @return
     */
    @GetMapping("/getEnrollQrCodeData")
    public ResultDataModel getEnrollQrCodeData(@RequestParam(CommonConstant.PARAM_IDS) String ids) {
        return ResultDataModel.handleSuccessResult(this.societyTrainingItemApplyService.getEnrollQrCodeData(ids));
    }

    @Override
    public GridService<TrainingItem> getGridService() {
        return this.societyTrainingItemApplyService;
    }
}
