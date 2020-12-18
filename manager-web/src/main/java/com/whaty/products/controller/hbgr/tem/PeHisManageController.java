package com.whaty.products.controller.hbgr.tem;

import com.whaty.domain.bean.hbgr.tem.PeHisTem;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: com.whaty.products.controller.hbgr.tem
 * @author: weipengsen
 * @create: 2020-12-15
 **/
@Lazy
@RestController
@RequestMapping("/entity/energy/peHisManage")
public class PeHisManageController extends TycjGridBaseControllerAdapter<PeHisTem> {

}
