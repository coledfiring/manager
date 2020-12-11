package com.whaty.products.controller.oltrain.enroll;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.grid.service.GridService;
import com.whaty.domain.bean.online.OlEnrollColumnRegulation;
import com.whaty.framework.grid.supergrid.controller.AbstractChangeDataSourceGridController;
import com.whaty.products.service.oltrain.enroll.impl.OlEnrollColumnRegulationServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 报名规则
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/enroll/enrollColumnRegulation")
public class OlEnrollColumnRegulationController extends AbstractChangeDataSourceGridController<OlEnrollColumnRegulation> {

    @Resource(name = "olEnrollColumnRegulationService")
    private OlEnrollColumnRegulationServiceImpl olEnrollColumnRegulationService;

    /**
     * 设置为默认
     * @return
     */
    @PostMapping("/setToDefault")
    public ResultDataModel setToDefault(@RequestBody ParamsDataModel paramsDataModel) {
        this.olEnrollColumnRegulationService.doSetToDefault(this.getIds(paramsDataModel));
        return ResultDataModel.handleSuccessResult("设置成功");
    }

    @Override
    public GridService<OlEnrollColumnRegulation> getGridService() {
        return this.olEnrollColumnRegulationService;
    }

    @Override
    protected void changeDataSource(ParamsDataModel<OlEnrollColumnRegulation> paramsData) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
    }
}
