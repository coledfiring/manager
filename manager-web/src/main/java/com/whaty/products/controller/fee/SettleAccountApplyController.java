package com.whaty.products.controller.fee;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeClass;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.fee.impl.SettleAccountApplyServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

import static com.whaty.constant.SqlRecordConstants.CLAZZ_SETTLE_ACCOUNT_INFO;

/**
 * 申请结算
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/fee/settleAccountApply")
public class SettleAccountApplyController extends TycjGridBaseControllerAdapter<PeClass> {

    @Resource(name = "settleAccountApplyService")
    private SettleAccountApplyServiceImpl settleAccountApplyService;

    /**
     * 申请结算
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/applySettleAccount")
    @OperateRecord(value = "结算申请", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "settleAccount", sql = CLAZZ_SETTLE_ACCOUNT_INFO)
    public ResultDataModel applySettleAccount(@RequestBody ParamsDataModel paramsDataModel) throws Exception {
        Map<String, Object> feeMap = paramsDataModel.getParams();
        String ids = (String) feeMap.remove(CommonConstant.PARAM_IDS);
        this.settleAccountApplyService.doApplySettleAccount(ids, feeMap);
        return ResultDataModel.handleSuccessResult("申请成功");
    }

    @Override
    public GridService<PeClass> getGridService() {
        return this.settleAccountApplyService;
    }
}
