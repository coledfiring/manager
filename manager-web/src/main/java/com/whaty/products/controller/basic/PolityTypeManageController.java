package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.PolityTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.POLITY_TYPE_BASIC_SQL;

/**
 * 政策类型
 *
 * @author shangyu
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/polityTypeManage")
@BasicOperateRecord("政策类型")
@SqlRecord(namespace = "polityType", sql = POLITY_TYPE_BASIC_SQL)
public class PolityTypeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "polityTypeManageService")
    private PolityTypeManageServiceImpl polityTypeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.polityTypeManageService;
    }
}
