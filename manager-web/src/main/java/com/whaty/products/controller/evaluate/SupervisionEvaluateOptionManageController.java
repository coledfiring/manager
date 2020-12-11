package com.whaty.products.controller.evaluate;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.evaluate.OverallEvaluateOption;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.evaluate.impl.SupervisionEvaluateOptionManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 教学督导评价选项管理
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/evaluate/supervisionEvaluateOptionManage")
public class SupervisionEvaluateOptionManageController extends TycjGridBaseControllerAdapter<OverallEvaluateOption> {

    @Resource(name = "supervisionEvaluateOptionManageService")
    private SupervisionEvaluateOptionManageServiceImpl supervisionEvaluateOptionManageService;

    @Override
    public GridService<OverallEvaluateOption> getGridService() {
        return this.supervisionEvaluateOptionManageService;
    }
}
