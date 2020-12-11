package com.whaty.products.controller.oltrain.basic;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.framework.aop.operatelog.annotation.BasicOperateRecord;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import com.whaty.products.service.oltrain.basic.impl.OLProfessionalTitleSeriesManageServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 职称系列管理controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/basic/onlineProfessionalTitleSeriesManage")
@BasicOperateRecord("职称系列")
public class OnlineProfessionalTitleSeriesManageController extends TycjGridBaseControllerAdapter<EnumConst> {

    @Resource(name = "olProfessionalTitleSeriesManageService")
    private OLProfessionalTitleSeriesManageServiceImpl olProfessionalTitleSeriesManageService;

    @Override
    public GridService<EnumConst> getGridService() {
        return this.olProfessionalTitleSeriesManageService;
    }
}
