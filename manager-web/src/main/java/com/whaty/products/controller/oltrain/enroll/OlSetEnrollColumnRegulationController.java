package com.whaty.products.controller.oltrain.enroll;

import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.products.service.oltrain.enroll.OlSetEnrollColumnRegulationService;
import com.whaty.products.service.oltrain.enroll.domain.OlSetRegulationParams;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设置报名规则
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController
@RequestMapping("/entity/olTrain/enroll/setEnrollColumnRegulation")
public class OlSetEnrollColumnRegulationController {

    @Resource(name = "olSetEnrollColumnRegulationService")
    private OlSetEnrollColumnRegulationService olSetEnrollColumnRegulationService;

    /**
     * 获取规则
     * @param id
     * @return
     */
    @GetMapping("/regulation/{id}")
    public ResultDataModel getRegulation(@PathVariable("id") String id) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        return ResultDataModel.handleSuccessResult(this.olSetEnrollColumnRegulationService.getRegulation(id));
    }

    /**
     * 保存规则
     * @param param
     * @return
     */
    @PostMapping("/regulation")
    public ResultDataModel saveRegulation(@RequestBody OlSetRegulationParams param) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        this.olSetEnrollColumnRegulationService.saveRegulation(param);
        return ResultDataModel.handleSuccessResult();
    }

}
