package com.whaty.products.controller.fee;

import com.whaty.domain.bean.PeClass;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 结算查询
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/fee/settleAccountSearch")
public class SettleAccountSearchController extends TycjGridBaseControllerAdapter<PeClass> {
}
