package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.CooperateUnitTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.COOPERATE_UNIT_TYPE_BASIC_SQL;

/**
 * 合作单位类型管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/cooperateUnitTypeManage")
@BasicOperateRecord("合作单位类型")
@SqlRecord(namespace = "cooperateUnitType", sql = COOPERATE_UNIT_TYPE_BASIC_SQL)
public class CooperateUnitTypeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "cooperateUnitTypeManageService")
    private CooperateUnitTypeManageServiceImpl cooperateUnitTypeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.cooperateUnitTypeManageService;
    }
}
