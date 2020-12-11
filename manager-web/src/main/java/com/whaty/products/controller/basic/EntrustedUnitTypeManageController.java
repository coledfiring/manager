package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.EntrustedUnitTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.ENTRUSTED_UNIT_TYPE_BASIC_SQL;

/**
 * 委托单位类型管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/entrustedUnitTypeManage")
@BasicOperateRecord("委托单位类型")
@SqlRecord(namespace = "entrustedUnitType", sql = ENTRUSTED_UNIT_TYPE_BASIC_SQL)
public class EntrustedUnitTypeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "entrustedUnitTypeManageService")
    private EntrustedUnitTypeManageServiceImpl entrustedUnitTypeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.entrustedUnitTypeManageService;
    }
}
