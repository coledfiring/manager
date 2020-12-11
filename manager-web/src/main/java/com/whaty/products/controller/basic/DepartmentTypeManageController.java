package com.whaty.products.controller.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.aop.operatelog.annotation.SqlRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.basic.impl.DepartmentTypeManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.whaty.constant.SqlRecordConstants.DEPARTMENT_TYPE_BASIC_SQL;

/**
 * 部门类型管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/basic/departmentTypeManage")
@BasicOperateRecord("部门类型")
@SqlRecord(namespace = "departmentType", sql = DEPARTMENT_TYPE_BASIC_SQL)
public class DepartmentTypeManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "departmentTypeManageService")
    private DepartmentTypeManageServiceImpl departmentTypeManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.departmentTypeManageService;
    }
}
