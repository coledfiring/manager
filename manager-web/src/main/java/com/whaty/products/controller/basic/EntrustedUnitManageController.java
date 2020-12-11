package com.whaty.products.controller.basic;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.EntrustedUnit;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.EntrustedUnitManageServiceImpl;
import com.whaty.products.service.training.domain.EntrustedUnitWithLink;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.ENTRUSTED_UNIT_BASIC_SQL;

/**
 * 委托单位管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/entrustedUnitManage")
@BasicOperateRecord("委托单位")
@SqlRecord(namespace = "entrustedUnit", sql = ENTRUSTED_UNIT_BASIC_SQL)
public class EntrustedUnitManageController extends TycjGridBaseControllerAdapter<EntrustedUnit> {

    @Resource(name = "entrustedUnitManageService")
    private EntrustedUnitManageServiceImpl entrustedUnitManageService;

    /**
     * 添加委托单位
     * @param paramsDataModel
     * @return
     */
    @PostMapping("/entrustedUnit")
    public ResultDataModel addEntrustedUnit(@RequestBody ParamsDataModel<EntrustedUnitWithLink> paramsDataModel) {
        this.entrustedUnitManageService.addEntrustedUnit(paramsDataModel.getBean());
        return ResultDataModel.handleSuccessResult("保存成功");
    }

    @Override
    public GridService<EntrustedUnit> getGridService() {
        return this.entrustedUnitManageService;
    }
}
