package com.whaty.products.controller.training;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.TrainingItemApply;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.impl.TrainingItemApplyUnitServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 项目申请单位列表
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/training/trainingItemApplyUnit")
public class TrainingItemApplyUnitController extends TycjGridBaseControllerAdapter<TrainingItemApply> {
    @Resource(name = "trainingItemApplyUnitService")
    private TrainingItemApplyUnitServiceImpl trainingItemApplyUnitService;

    @Override
    public GridService<TrainingItemApply> getGridService() {
        return this.trainingItemApplyUnitService;
    }
}
