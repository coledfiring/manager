package com.whaty.products.controller.training;

import com.whaty.domain.bean.TrainingItem;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 合作培训项目查询
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/training/cooperationTrainingItemSearch")
public class CooperationTrainingItemSearchController extends TycjGridBaseControllerAdapter<TrainingItem> {
}
