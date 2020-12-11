package com.whaty.products.controller.oltrain.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.basic.impl.OLPositionalTitleManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 职称类型管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/basic/onlinePositionalTitleManage")
@BasicOperateRecord("职称类型")
public class OnlinePositionalTitleManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "olPositionalTitleManageService")
    private OLPositionalTitleManageServiceImpl olPositionalTitleManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.olPositionalTitleManageService;
    }
}
