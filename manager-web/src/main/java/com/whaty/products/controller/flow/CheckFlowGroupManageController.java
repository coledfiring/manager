package com.whaty.products.controller.flow;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.flow.CheckFlowGroup;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.flow.impl.CheckFlowGroupManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 审核流程组管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/flow/checkFlowGroupManage")
public class CheckFlowGroupManageController extends TycjGridBaseControllerAdapter<CheckFlowGroup> {

    @Resource(name = "checkFlowGroupManageService")
    private CheckFlowGroupManageServiceImpl checkFlowGroupManageService;

    /**
     * 设为有效
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/setActive")
    public ResultDataModel setActive(@RequestBody ParamsDataModel paramsDataModel) {
        this.checkFlowGroupManageService.doSetActive(this.getIds(paramsDataModel), "1");
        return ResultDataModel.handleSuccessResult("设置成功");
    }

    /**
     * 设为无效
     * @param paramsData
     * @return
     */
    @RequestMapping("/setNoActive")
    public ResultDataModel setNoActive(@RequestBody ParamsDataModel paramsData) {
        this.checkFlowGroupManageService.doSetActive(this.getIds(paramsData), "0");
        return ResultDataModel.handleSuccessResult("设置成功");
    }

    @Override
    public GridService<CheckFlowGroup> getGridService() {
        return this.checkFlowGroupManageService;
    }
}
