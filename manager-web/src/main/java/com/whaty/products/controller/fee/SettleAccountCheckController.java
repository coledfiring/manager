package com.whaty.products.controller.fee;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeClass;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.fee.impl.SettleAccountCheckServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 结算审核
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/fee/settleAccountCheck")
public class SettleAccountCheckController extends TycjGridBaseControllerAdapter<PeClass> {

    @Resource(name = "settleAccountCheckService")
    private SettleAccountCheckServiceImpl settleAccountCheckService;

    /**
     * 驳回审核
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/rejectAudit")
    public ResultDataModel rejectAudit(@RequestBody ParamsDataModel paramsDataModel) {
        String ids = this.getIds(paramsDataModel);
        this.settleAccountCheckService.doRejectAudit(ids);
        return ResultDataModel.handleSuccessResult("驳回审核成功");
    }

    @Override
    public GridService<PeClass> getGridService() {
        return this.settleAccountCheckService;
    }
}
