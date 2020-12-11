package com.whaty.products.controller.basic;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.CooperateUnit;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.CooperateUnitManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.COOPERATE_UNIT_BASIC_SQL;

/**
 * 合作单位管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/cooperateUnitManage")
@BasicOperateRecord("合作单位")
@SqlRecord(namespace = "cooperateUnit", sql = COOPERATE_UNIT_BASIC_SQL)
public class CooperateUnitManageController extends TycjGridBaseControllerAdapter<CooperateUnit> {

    @Resource(name = "cooperateUnitManageService")
    private CooperateUnitManageServiceImpl cooperateUnitManageService;

    @Override
    public GridService<CooperateUnit> getGridService() {
        return this.cooperateUnitManageService;
    }
}
