package com.whaty.products.controller.training;

import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeReview;
import com.whaty.domain.bean.PeReviewOpinion;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.training.impl.PeReviewManageServiceImpl;
import com.whaty.products.service.training.impl.PeReviewOpinionServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.Resources;

/**
 * 接待管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/training/peReviewOpinion")
public class PeReviewOpinionController extends TycjGridBaseControllerAdapter<PeReviewOpinion> {

    @Resource(name = "peReviewOpinionService")
    private PeReviewOpinionServiceImpl peReviewOpinionService;

    @Override
    public GridService<PeReviewOpinion> getGridService() {
        return this.peReviewOpinionService;
    }
}
