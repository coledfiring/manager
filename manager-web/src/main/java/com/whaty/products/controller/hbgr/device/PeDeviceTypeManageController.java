package com.whaty.products.controller.hbgr.device;

import com.whaty.domain.bean.hbgr.yysj.PeDevice;
import com.whaty.domain.bean.hbgr.yysj.PeDeviceType;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author weipengsen  Date 2020/6/19
 */
@Lazy
@RestController
@RequestMapping("/entity/yysj/peDeviceTypeManage")
public class PeDeviceTypeManageController extends TycjGridBaseControllerAdapter<PeDeviceType> {
}
