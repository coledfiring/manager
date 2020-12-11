package com.whaty.products.controller.oltrain.train;

import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlPeOrganization;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.train.impl.OrganizationManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 机构管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/train/organizationManage")
@BasicOperateRecord("机构管理")
public class OrganizationManageController extends TycjGridBaseControllerAdapter<OlPeOrganization> {

    @Resource(name = "organizationManageService")
    private OrganizationManageServiceImpl organizationManageService;

    @Override
    public GridService<OlPeOrganization> getGridService() {
        return this.organizationManageService;
    }
}
