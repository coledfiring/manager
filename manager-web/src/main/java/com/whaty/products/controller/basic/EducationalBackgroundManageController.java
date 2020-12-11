package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.EducationalBackgroundManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.EDUCATIONAL_BACKGROUND_BASIC_SQL;

/**
 * 学历管理controller
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/educationalBackgroundManage")
@BasicOperateRecord("学历")
@SqlRecord(namespace = "educationalBackground", sql = EDUCATIONAL_BACKGROUND_BASIC_SQL)
public class EducationalBackgroundManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "educationalBackgroundManageService")
    private EducationalBackgroundManageServiceImpl educationalBackgroundManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.educationalBackgroundManageService;
    }
}
