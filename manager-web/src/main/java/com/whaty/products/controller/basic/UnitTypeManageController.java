package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.UnitTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.UNIT_TYPE_BASIC_SQL;

/**
 * 机构类型管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/unitTypeManage")
@BasicOperateRecord("机构类型")
@SqlRecord(namespace = "unitType", sql = UNIT_TYPE_BASIC_SQL)
public class UnitTypeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "unitTypeManageService")
    private UnitTypeManageServiceImpl unitTypeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.unitTypeManageService;
    }
}
