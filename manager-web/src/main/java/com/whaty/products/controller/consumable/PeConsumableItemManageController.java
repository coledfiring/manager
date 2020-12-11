package com.whaty.products.controller.consumable;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeConsumableItem;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.consumable.impl.PeConsumableItemManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.CONSUMABLE_ITEM_MANAGE_BASIC_SQL;

/**
 * 易耗品物品管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/consumable/peConsumableItemManage")
@BasicOperateRecord(value = "易耗品物品管理")
@SqlRecord(namespace = "peConsumableItemManage", sql = CONSUMABLE_ITEM_MANAGE_BASIC_SQL)
public class PeConsumableItemManageController extends TycjGridBaseControllerAdapter<PeConsumableItem> {

    @Resource(name = "peConsumableItemManageService")
    private PeConsumableItemManageServiceImpl peConsumableItemManageService;

    @Override
    public GridService<PeConsumableItem> getGridService() {
        return this.peConsumableItemManageService;
    }

}
