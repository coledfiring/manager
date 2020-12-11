package com.whaty.products.controller.flow;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.flow.CheckFlowType;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.flow.impl.CheckFlowTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 审核流程类型管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/flow/checkFlowTypeManage")
public class CheckFlowTypeManageController extends TycjGridBaseControllerAdapter<CheckFlowType> {

    @Resource(name = "checkFlowTypeManageService")
    private CheckFlowTypeManageServiceImpl checkFlowTypeManageService;

    @Override
    public GridService<CheckFlowType> getGridService() {
        return this.checkFlowTypeManageService;
    }
}
