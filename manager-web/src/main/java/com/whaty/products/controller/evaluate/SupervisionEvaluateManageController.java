package com.whaty.products.controller.evaluate;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.evaluate.OverallEvaluate;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.evaluate.SupervisionEvaluateManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 教学督导评价管理
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/evaluate/supervisionEvaluateManage")
public class SupervisionEvaluateManageController extends TycjGridBaseControllerAdapter<OverallEvaluate> {

    @Resource(name = "supervisionEvaluateManageService")
    private SupervisionEvaluateManageServiceImpl supervisionEvaluateManageService;

    @Override
    public GridService<OverallEvaluate> getGridService() {
        return this.supervisionEvaluateManageService;
    }

}
