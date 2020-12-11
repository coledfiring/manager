package com.whaty.products.controller.enroll;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.enroll.EnrollUserLinkman;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.enroll.impl.EnrollUserLinkmanManageServiceImpl;
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
@RequestMapping("/entity/enroll/enrollUserLinkmanManage")
public class EnrollUserLinkmanManageController extends TycjGridBaseControllerAdapter<EnrollUserLinkman> {

    @Resource(name = "enrollUserLinkmanManageService")
    private EnrollUserLinkmanManageServiceImpl enrollUserLinkmanManageServiceImpl;

    @Override
    public GridService<EnrollUserLinkman> getGridService() {
        return this.enrollUserLinkmanManageServiceImpl;
    }
}
