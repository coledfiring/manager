package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.CheckPointManageServiceImpl;
import com.whaty.products.service.basic.impl.CooperateUnitTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.CHECK_POINT_BASIC_SQL;

/**
 * author weipengsen  Date 2020/6/20
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/checkPointManage")
@BasicOperateRecord("巡检点")
@SqlRecord(namespace = "flagcheckPoint", sql = CHECK_POINT_BASIC_SQL)
public class CheckPointManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "checkPointManageService")
    private CheckPointManageServiceImpl checkPointManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.checkPointManageService;
    }
}
