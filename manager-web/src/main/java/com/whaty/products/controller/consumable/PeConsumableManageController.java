package com.whaty.products.controller.consumable;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeConsumable;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.consumable.impl.PeConsumableManageServiceImpl;
import com.whaty.products.service.training.constant.TrainingConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.CONSUMABLE_MANAGE_BASIC_SQL;


/**
 * 易耗品管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/consumable/peConsumableManage")
@BasicOperateRecord(value = "易耗品管理")
@SqlRecord(namespace = "peConsumableManage", sql = CONSUMABLE_MANAGE_BASIC_SQL)
public class PeConsumableManageController extends TycjGridBaseControllerAdapter<PeConsumable> {

    @Resource(name = "peConsumableManageService")
    private PeConsumableManageServiceImpl peConsumableManageService;

    @Override
    public GridService<PeConsumable> getGridService() {
        return this.peConsumableManageService;
    }

    @Override
    public void initGrid(GridConfig gridConfig) {
        peConsumableManageService.listFormItems().forEach(gridConfig::addColumn);
        super.initGrid(gridConfig);
    }

    /**
     * 获取选项数据
     *
     * @return
     */
    @RequestMapping("/formItems")
    public ResultDataModel getFormItems() {
        return ResultDataModel.handleSuccessResult(peConsumableManageService.listFormItems());
    }

    /**
     * 做验收
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("doCheck")
    public ResultDataModel doCheck(@RequestBody ParamsDataModel paramsDataModel) {
        peConsumableManageService.doCheck(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("操作成功");
    }


    /**
     * 设置费用
     *
     * @param paramsDataModel
     * @return
     */
    @RequestMapping("doSetPrice")
    public ResultDataModel setPrice(@RequestBody ParamsDataModel paramsDataModel) {
        peConsumableManageService.doSetPrice(this.getIds(paramsDataModel), paramsDataModel
                .getStringParameter(TrainingConstant.PARAM_PRICE));
        return ResultDataModel.handleSuccessResult("操作成功");
    }
}
