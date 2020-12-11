package com.whaty.products.controller.hbgr.check;

import com.whaty.domain.bean.hbgr.yysj.PeCheckDetail;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author weipengsen  Date 2020/6/20
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/peCheckDetailManage")
public class PeCheckDetailManageController extends TycjGridBaseControllerAdapter<PeCheckDetail> {
}
