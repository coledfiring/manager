package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.SchoolZoneManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.SCHOOL_ZONE_BASIC_SQL;

/**
 * 校区管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/schoolZoneManage")
@BasicOperateRecord("校区")
@SqlRecord(namespace = "schoolZone", sql = SCHOOL_ZONE_BASIC_SQL)
public class SchoolZoneManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "schoolZoneManageService")
    private SchoolZoneManageServiceImpl schoolZoneManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.schoolZoneManageService;
    }
}
