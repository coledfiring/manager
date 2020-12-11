package com.whaty.products.controller.enroll;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.enroll.EnrollColumnRegulation;
import com.whaty.framework.grid.supergrid.controller.AbstractChangeDataSourceGridController;
import com.whaty.products.service.enroll.impl.EnrollColumnRegulationServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 报名规则
 *
 * @author weipengsen
 */
@Lazy
@RestController
@RequestMapping("/entity/enroll/enrollColumnRegulation")
public class EnrollColumnRegulationController extends AbstractChangeDataSourceGridController<EnrollColumnRegulation> {

    @Resource(name = "enrollColumnRegulationService")
    private EnrollColumnRegulationServiceImpl enrollColumnRegulationService;

    /**
     * 设置为默认
     * @return
     */
    @PostMapping("/setToDefault")
    public ResultDataModel setToDefault(@RequestBody ParamsDataModel paramsDataModel) {
        this.enrollColumnRegulationService.doSetToDefault(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("设置成功");
    }

    @Override
    public GridService<EnrollColumnRegulation> getGridService() {
        return this.enrollColumnRegulationService;
    }

    @Override
    protected void changeDataSource(ParamsDataModel<EnrollColumnRegulation> paramsData) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
    }
}
