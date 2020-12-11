package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.PositionalTitleManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.POSITIONAL_TITLE_BASIC_SQL;

/**
 * 讲师职称controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/positionalTitleManage")
@BasicOperateRecord("讲师职称")
@SqlRecord(namespace = "positionalTitle", sql = POSITIONAL_TITLE_BASIC_SQL)
public class PositionalTitleManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "positionalTitleManageService")
    private PositionalTitleManageServiceImpl positionalTitleManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.positionalTitleManageService;
    }
}
