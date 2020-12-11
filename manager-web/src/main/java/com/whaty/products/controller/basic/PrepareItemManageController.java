package com.whaty.products.controller.basic;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PrepareItem;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.PrepareItemManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.PREPARE_ITEM_BASIC_SQL;

/**
 * 带班自查事务管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/prepareItemManage")
@BasicOperateRecord("带班自查事务")
@SqlRecord(namespace = "prepareItem", sql = PREPARE_ITEM_BASIC_SQL)
public class PrepareItemManageController extends TycjGridBaseControllerAdapter<PrepareItem> {

    @Resource(name = "prepareItemManageService")
    private PrepareItemManageServiceImpl prepareItemManageService;

    @Override
    public GridService<PrepareItem> getGridService() {
        return this.prepareItemManageService;
    }
}
