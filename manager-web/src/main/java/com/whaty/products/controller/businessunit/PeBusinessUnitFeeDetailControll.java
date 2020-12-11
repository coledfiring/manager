package com.whaty.products.controller.businessunit;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.businessunit.PeBusinessUnitFeeDetailServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 单位学生缴费明细
 *
 * @author shanshuai
 */
@Lazy
@RestController
@RequestMapping("/entity/businessUnit/peBusinessUnitFeeDetail")
public class PeBusinessUnitFeeDetailControll extends TycjGridBaseControllerAdapter<PeStudent> {

    @Resource(name = "peBusinessUnitFeeDetailService")
    private PeBusinessUnitFeeDetailServiceImpl peBusinessUnitFeeDetailService;

    /**
     * 添加缴费学生到订单中
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("/addStudentToBusinessOrder")
    @OperateRecord(value = "添加缴费学生到订单中", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    public ResultDataModel addStudentToBusinessOrder(@RequestBody ParamsDataModel paramsDataModel) {
        String orderId = paramsDataModel.getStringParameter("parentId");
        return ResultDataModel.handleSuccessResult("添加成功，共操作" +
                peBusinessUnitFeeDetailService.addStudentToBusinessOrder(this.getIds(paramsDataModel), orderId) + "条数据");
    }

    @Override
    public GridService<PeStudent> getGridService() {
        return this.peBusinessUnitFeeDetailService;
    }
}
