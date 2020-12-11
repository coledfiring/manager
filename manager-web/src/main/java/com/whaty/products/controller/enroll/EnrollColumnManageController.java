package com.whaty.products.controller.enroll;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.enroll.EnrollColumn;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.enroll.impl.EnrollColumnManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 报名项管理
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/superAdmin/enroll/enrollColumnManage")
public class EnrollColumnManageController extends TycjGridBaseControllerAdapter<EnrollColumn> {

    @Resource(name = "enrollColumnManageService")
    private EnrollColumnManageServiceImpl enrollColumnManageService;

    @Override
    public GridService<EnrollColumn> getGridService() {
        return this.enrollColumnManageService;
    }
}