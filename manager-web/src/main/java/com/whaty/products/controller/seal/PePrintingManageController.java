package com.whaty.products.controller.seal;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PePrinting;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.seal.impl.PePrintingManageServiceImpl;
import com.whaty.products.service.training.constant.TrainingConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.PRINTING_BASIC_INFO;

/**
 * 印刷管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/seal/pePrintingManage")
@BasicOperateRecord(value = "印刷管理")
@SqlRecord(namespace = "pePrinting", sql = PRINTING_BASIC_INFO)
public class PePrintingManageController extends TycjGridBaseControllerAdapter<PePrinting> {

    @Resource(name = "pePrintingManageService")
    private PePrintingManageServiceImpl pePrintingManageService;

    @Override
    public GridService<PePrinting> getGridService() {
        return this.pePrintingManageService;
    }

    /**
     * 做受理
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("doAccept")
    @OperateRecord(value = "印刷受理", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "pePrinting", sql = PRINTING_BASIC_INFO)
    public ResultDataModel doAccept(@RequestBody ParamsDataModel paramsDataModel) {
        pePrintingManageService.doAccept(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("操作成功");
    }

    /**
     * 做校验
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("doCheck")
    @OperateRecord(value = "印刷审核", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "pePrinting", sql = PRINTING_BASIC_INFO)
    public ResultDataModel doCheck(@RequestBody ParamsDataModel paramsDataModel) {
        pePrintingManageService.doCheck(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("操作成功");
    }

    /**
     * 設置費用
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("doSetPrice")
    @OperateRecord(value = "印刷设置费用", moduleCode = OperateRecordModuleConstant.COMMON_MODULE_CODE)
    @SqlRecord(namespace = "pePrinting", sql = PRINTING_BASIC_INFO)
    public ResultDataModel setPrice(@RequestBody ParamsDataModel paramsDataModel){
        pePrintingManageService.doSetPrice(this.getIds(paramsDataModel), paramsDataModel
                .getStringParameter(TrainingConstant.PARAM_PRICE));
        return ResultDataModel.handleSuccessResult("操作成功");
    }


}
