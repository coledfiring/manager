package com.whaty.products.controller.analyse;

import com.whaty.domain.bean.PePlace;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 培训地点使用统计
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/placeUsageAnalyse")
public class PlaceUsageAnalyseController extends TycjGridBaseControllerAdapter<PePlace> {}
