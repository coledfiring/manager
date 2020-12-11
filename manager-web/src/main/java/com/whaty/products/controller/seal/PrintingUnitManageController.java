package com.whaty.products.controller.seal;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PrintingUnit;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.seal.impl.PrintingUnitManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.PRINTING_UNIT_BASIC_INFO;


/**
 * 印刷单位管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/seal/printingUnitManage")
@BasicOperateRecord(value = "印刷单位管理")
@SqlRecord(namespace = "printingUnit", sql = PRINTING_UNIT_BASIC_INFO)
public class PrintingUnitManageController extends TycjGridBaseControllerAdapter<PrintingUnit> {

    @Resource(name = "printingUnitManageService")
    private PrintingUnitManageServiceImpl printingUnitManageService;

    @Override
    public GridService<PrintingUnit> getGridService() {
        return printingUnitManageService;
    }
}

