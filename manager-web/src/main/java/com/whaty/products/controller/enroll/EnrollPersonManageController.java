package com.whaty.products.controller.enroll;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.enroll.EnrollPerson;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.enroll.impl.EnrollPersonManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 报名系统用户联系人管理controller
 *
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/enrollPersonManage")
public class EnrollPersonManageController extends TycjGridBaseControllerAdapter<EnrollPerson> {

    @Resource(name = "enrollPersonManageService")
    private EnrollPersonManageServiceImpl enrollPersonManageServiceImpl;

    @Override
    public GridService<EnrollPerson> getGridService() {
        return this.enrollPersonManageServiceImpl;
    }
}
