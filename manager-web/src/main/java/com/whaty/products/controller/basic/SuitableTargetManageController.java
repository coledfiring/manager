package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.SuitableTargetManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.SUITABLE_TARGET_BASIC_SQL;

/**
 * 适合对象管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/suitableTargetManage")
@BasicOperateRecord("适合对象")
@SqlRecord(namespace = "suitableTarget", sql = SUITABLE_TARGET_BASIC_SQL)
public class SuitableTargetManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "suitableTargetManageService")
    private SuitableTargetManageServiceImpl suitableTargetManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.suitableTargetManageService;
    }
}
