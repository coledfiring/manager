package com.whaty.products.controller.consumable;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.PeConsumableDetail;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.consumable.impl.PeConsumableDetailManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 易耗品物品详情管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/consumable/peConsumableDetailManage")
@BasicOperateRecord(value = "易耗品物品管理")
public class PeConsumableDetailManageController extends TycjGridBaseControllerAdapter<PeConsumableDetail> {

    @Resource(name = "peConsumableDetailManageService")
    private PeConsumableDetailManageServiceImpl peConsumableDetailManageService;

    @Override
    public GridService<PeConsumableDetail> getGridService() {
        return this.peConsumableDetailManageService;
    }

}
