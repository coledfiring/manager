package com.whaty.products.controller.analyse;

import com.whaty.domain.bean.PeHotel;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 宾馆使用统计
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/analyse/hotelUsageAnalyse")
public class HotelUsageAnalyseController extends TycjGridBaseControllerAdapter<PeHotel> {}
